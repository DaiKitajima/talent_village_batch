package jp.co.bng.talentvillagebatch.batch.util;

import jp.co.bng.talentvillagebatch.batch.mail.MailTemplateBase;

public interface MailUtilInterface {

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
	void sendTemlateMail(String propTitleKey, String propContentKey, String propSignatureKey,
			MailTemplateBase templateClass, String sendEnableKey) throws Exception;

}