package jp.co.bng.talentvillagebatch.batch.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import jp.co.bng.talentvillagebatch.batch.mail.MailTemplateBase;
import lombok.extern.slf4j.Slf4j;


/**
 * メール共通処理用Utilクラス
 * メールサーバとSMTPによる直接のやり取りを実装する。
 * 接続情報として以下の情報がapplication.propertiesに必須となる。
 * なお、いかの項目は環境変数の値を中継しているだけなので、事実上設定が必要なのは環境変数。
 * ・mail.setting.smtp.host
 * ・mail.setting.smtp.port
 * ・mail.setting.auth.user
 * ・mail.setting.auth.password
 * @author Webridge
 *
 */
@Component
@ConditionalOnProperty(name="mail.setting.communication-strategy",havingValue="smtp")
@Slf4j
public class MailUtilSmtp implements MailUtilInterface {
	@Autowired
	private MailProperties mailProperties;

	/**
	 * メール送信処理
	 * @param password
	 * @param decideUrl
	 * @throws Exception
	 */
	private void sendMail(String mailTitle, String mailContent, MailTemplateBase templateClass) throws Exception {

		Email email = new SimpleEmail();
		email.setHostName(mailProperties.getSmtp().getHost());
		email.setSmtpPort(mailProperties.getSmtp().getPort());

		email.setAuthenticator(new DefaultAuthenticator(
				mailProperties.getAuth().getUser(),
				mailProperties.getAuth().getPassword()));

		//email.setSSLOnConnect(true);
		email.setStartTLSEnabled(true);
		// 送信者が指定されていなければプロパティからデフォルト値を設定
		email.setFrom(ObjectUtils.isEmpty(templateClass.getSenderAddress()) ? 
				mailProperties.getSender() : 
				templateClass.getSenderAddress());
		email.addBcc(ObjectUtils.isEmpty(templateClass.getSenderAddress()) ? 
				mailProperties.getSender() : 
				templateClass.getSenderAddress());
		email.setCharset("UTF-8");
		email.setSubject(mailTitle);
		
		email.setContent(mailContent.replace("\r\n", "<br/>"), "text/html");
//		StringBuilder mailBody = new StringBuilder();
//		mailBody.append(mailContent);
//
//		email.setMsg(mailBody.toString());

		// templateClassからメールアドレス抽出
		if(templateClass.getSendToAddressList() != null && templateClass.getSendToAddressList().size() != 0 ){
			for(String address : templateClass.getSendToAddressList().stream().distinct().toList()) {
				if (address != null && "".equals(address) == false) {
					email.addTo(address);
				}
			}
		}
		// プロパティからCCで送信する送り先メールアドレス抽出
		if(templateClass.getSendCcAddressList() != null && templateClass.getSendCcAddressList().size() != 0 ){
			for(String address : templateClass.getSendCcAddressList().stream().distinct().toList()) {
				if (address != null && "".equals(address) == false) {
					email.addCc(address);
				}
			}
		}
	
		email.send();
	}

	/**
	 * タイトル、本文のプロパティファイルのキー値と、置換対象テンプレートクラスより、
	 * 該当パターンのメール送信を行う。
	 * @param propTitleKey
	 * @param propContentKey
	 * @param propSignatureKey
	 * @param templateClass
	 * @param sendEnableKey
	 * @throws Exception
	 */
	@Override
	public void sendTemlateMail(
			String propTitleKey, 
			String propContentKey,
			String propSignatureKey, 
			MailTemplateBase templateClass, 
			String sendEnableKey) throws Exception {

		try {
			if(!isEnable(sendEnableKey)) {
				log.info("送信有効フラグ[{}]によりメール送信がキャンセルされました。", sendEnableKey);
				return;
			}
			String mailTitle = getReplacedMailText(templateClass, propTitleKey);
			String mailContent = getReplacedMailText(templateClass, propContentKey);

			if(propSignatureKey != null) {
				String mailSignature = getReplacedMailText(templateClass, propSignatureKey);
				mailContent = mailContent + mailSignature;
			}

			// メール送信実行
			sendMail(mailTitle, mailContent, templateClass);

		} catch (Exception e) {

			throw e;
		}

	}
	
	private boolean isEnable(String sendEnableKey) {
		// キーが渡されなかった場合は有効とみなす
		if(ObjectUtils.isEmpty(sendEnableKey)) return true;
		// メール設定に定義がない場合は有効とみなす
		String enableParam = MessageUtils.getValue(MessageUtils.PROPERTIES_KIND_MAIL_SETTING_JA, sendEnableKey);
		if(ObjectUtils.isEmpty(enableParam)) return true;
		// 設定値が取得できた場合は設定値に従って有効無効を判定する
		return Boolean.valueOf(enableParam);
	}

	/**
	 * テンプレート内、置換文字列を引数で取得したテンプレートクラスの内容で置換をする
	 *
	 * @param templateClass
	 * @param propKey
	 * @return
	 * @throws Exception
	 */
	private String getReplacedMailText(MailTemplateBase templateClass, String propKey) throws Exception {

		// プロパティファイルから、テンプレートを取得する
		String targetStr = MessageUtils.getValue(MessageUtils.PROPERTIES_KIND_MAIL_SETTING_JA, propKey);

		if(targetStr == null || "".equals(targetStr)) {
			log.error("メールテンプレートの設定がされていないため、メール送信ができませんでした。");
			throw new Exception("メールテンプレートの設定がされていないため、メール送信ができませんでした。");
		}

		// 親クラスのフィールドを取得
		Field superClassFields[] = templateClass.getClass().getSuperclass().getDeclaredFields();
		// 子クラスのフィールドを取得
		Field subClassFields[] = templateClass.getClass().getDeclaredFields();

		// 取得したField型の配列を1つにまとめる
		Field targetFields[] = new Field[superClassFields.length + subClassFields.length];
		System.arraycopy(superClassFields, 0, targetFields, 0, superClassFields.length);
		System.arraycopy(subClassFields, 0, targetFields, superClassFields.length, subClassFields.length);

		// コピー処理完了後メモリを考慮しNULLを代入する
		superClassFields = null;
		subClassFields = null;

		String fieldValue = null;

		for (Field field : targetFields) {

			// Privateフィールドへのアクセスを可能にする
			field.setAccessible(true);

			// 取得したフィールドが定数であれば、置換対象外とする
			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}

			// 取得したフィールドが「String」以外の場合はスキップする
			if (field.getType() != String.class) {
				continue;
			}

			// 置換対象文字列
			final String replaceKeyWord = "@@@" + field.getName() + "@@@";

			// 置換対象文字列がない場合は、スキップする
			if (targetStr.contains(replaceKeyWord) == false) {
				continue;
			}

			fieldValue = (String)field.get(templateClass);
			// 対象フィールドがNULLだった場合は、LOG出力してスキップする
			if (fieldValue == null) {
				log.warn("メールテンプレートクラスの" + field.getName() + "がNULLのため、空文字で置換処理します。");
				fieldValue = "";
			}
			targetStr = targetStr.replace(replaceKeyWord, fieldValue);

		}

		return targetStr;
	}
}
