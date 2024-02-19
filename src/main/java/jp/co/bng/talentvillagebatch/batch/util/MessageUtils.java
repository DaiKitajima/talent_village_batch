package jp.co.bng.talentvillagebatch.batch.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;

/**
 * メッセージ取得Utilクラス
 * @author d.kitajima
 *
 */
@Slf4j
public class MessageUtils {

	//private static ResourceBundle messageJaProperties;

	//private static ResourceBundle validationJaProperties;

	private static ResourceBundle mailStteingProperties;

	/** Message_ja.properties用 */
	//public static final String PROPERTIES_KIND_MESSAGE_JA = "Message_ja";

	//public static final String PROPERTIES_KIND_VALIDATION_JA = "ValidationMessages";

	/** mail_setting.properties用 */
	public static final String PROPERTIES_KIND_MAIL_SETTING_JA = "mail_setting";

	static {
		//messageJaProperties = ResourceBundle.getBundle(PROPERTIES_KIND_MESSAGE_JA, Locale.JAPAN);
		//validationJaProperties = ResourceBundle.getBundle(PROPERTIES_KIND_VALIDATION_JA, Locale.JAPAN);
		mailStteingProperties = ResourceBundle.getBundle(PROPERTIES_KIND_MAIL_SETTING_JA, Locale.JAPAN);
	}

	/**
	 * 指定のPropertiesファイルをKey検索して一致したValue(String)を返却する
	 * @param propertiesKind
	 * @param key
	 * @return
	 */
	public static String getValue(String propertiesKind, String key) {

		ResourceBundle targetBundle = getTargetBundle(propertiesKind);

		return targetBundle.getString(key);
	}

	/**
	 * 指定のPropertiesファイルをKey検索して一致したValue(int)を返却する
	 * @param propertiesKind
	 * @param key
	 * @return
	 */
	public static int getValueInt(String propertiesKind, String key) {

		ResourceBundle targetBundle = getTargetBundle(propertiesKind);

		String targetStr = targetBundle.getString(key);

		int targetInt = Integer.parseInt(targetStr);

		return targetInt;
	}

	/**
	 * 指定のPropertiesファイルをKey検索して一致したValueを返却する
	 * 可変値あり
	 * @param propertiesKind
	 * @param key
	 * @return
	 */
	public static String getValue(String propertiesKind, String key, Object[] params) {

		ResourceBundle targetBundle = getTargetBundle(propertiesKind);
		String baseMessage = targetBundle.getString(key);
		return MessageFormat.format(baseMessage, params);
	}


	private static ResourceBundle getTargetBundle(String propertiesKind) {

//		if (PROPERTIES_KIND_MESSAGE_JA.equals(propertiesKind)) {
//			//return messageJaProperties;
//
//		} else if (PROPERTIES_KIND_VALIDATION_JA.equals(propertiesKind)) {
//			//return validationJaProperties;
//		} else if (PROPERTIES_KIND_MAIL_SETTING_JA.equals(propertiesKind)) {
//			return mailStteingProperties;
//		}
		if (PROPERTIES_KIND_MAIL_SETTING_JA.equals(propertiesKind)) {
			return mailStteingProperties;
		}
		log.error("Propertiesファイル取得に失敗しました。");

		return null;
	}

}
