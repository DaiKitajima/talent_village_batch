package jp.co.bng.talentvillagebatch.batch.util;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StringUtil {

	/** 暗号化方式AES */
	private static final String ALGORITHM = "AES";

	/** 禁則文字定義 */
	private static Map<Character, Character> escapeMap = new HashMap<>();

	/** サニタイジング処理 */
	private static final Map<Character, Character>  SANITIZING_CHAR_MAP;
	
	/** イニシャライザ */
	static {

		// パスワード禁足文字MAPの定義
		escapeMap.put( '/', 'a');
		escapeMap.put('=', 'b');
		escapeMap.put('<', 'c');
		escapeMap.put('>', 'd');
		escapeMap.put('(', 'e');
		escapeMap.put(')', 'f');
		escapeMap.put('%', 'g');
		escapeMap.put('\'', 'h');
		escapeMap.put('\"', 'i');
		escapeMap.put('\\', 'j');
		escapeMap.put('~', 'k');
		escapeMap.put('^', 'l');
		escapeMap.put('|', 'm');

		// サニタイジング対象文字の定義
		Map<Character, Character> sanitizingMap = new HashMap<>();
		sanitizingMap.put('&', '＆');
		sanitizingMap.put('<', '＜');
		sanitizingMap.put('>', '＞');
		sanitizingMap.put('"', '”');
		sanitizingMap.put('\'', '’');


		SANITIZING_CHAR_MAP = Collections.unmodifiableMap(sanitizingMap);
	}
	
	/**
	 * 引数で受け取った文字列をAESアルゴリズムで暗号化する
	 * @param targetStr
	 * @return
	 */
	public static String getEncryptedStr(String targetStr) {

		Cipher cipher = null;

		// 引数がNULLまたは、空文字の場合は、NULLを返却する
		if (StringUtil.isEmpty(targetStr)) {
			return null;
		}

		// 暗号処理
		String encryptedStr = null;

		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec("YKo83n14SWf7o8G5".getBytes(), ALGORITHM));
			encryptedStr = new String(Base64.getEncoder().encode(cipher.doFinal(targetStr.getBytes())));

			// 禁則文字を任意の文字に置換する
			encryptedStr = escapeCharacter(encryptedStr);

		} catch (Exception e) {
			
		}
		return encryptedStr;
	}
	
	/**
	 * 禁則文字を規定ルールの文字に変換して、処理結果を返却する
	 * @param encryptedStr
	 * @return
	 */
	private static String escapeCharacter(String encryptedStr) {

		StringBuilder sb = new StringBuilder();

		for (int ct = 0; ct < encryptedStr.length(); ct++) {
			char ch = encryptedStr.charAt(ct);

			if (escapeMap.containsKey(ch)) {
				// 規定ルールで変換する
				ch = escapeMap.get(ch);
			}

			sb.append(String.valueOf(ch));
		}

		return sb.toString();
	}

	/**
	 * 引数で受け取った文字列をAESアルゴリズムで復号化する
	 * @param targetStr
	 * @return
	 */

	// AESアルゴリズムで暗号化された文字列を復号する
	public static String getDecriptedStr(String targetStr) {

		// 引数チェック
		if (StringUtil.isEmpty(targetStr)) {
			return null;
		}
		// 復号処理

		Cipher cipher = null;

		String encryptedStr = null;

		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(targetStr.getBytes(), ALGORITHM));
			encryptedStr = new String(Base64.getDecoder().decode(cipher.doFinal(targetStr.getBytes())));
		} catch (Exception e) {
			
		}

		return encryptedStr;
	}

	/**
	 * 引数の文字列を、サニタイジング処理を実施し、
	 * 置換後の文字列を返却する
	 * @param targetStr
	 * @return
	 */
	public static String sanitizeStr(String targetStr) {

		// 引数に値がない場合は、空文字を返却する
		if (targetStr == null || "".equals(targetStr)) {
			return "";
		}

		StringBuilder sanitizedStr = new StringBuilder();

		// サニタイジング対象の文字を置換する
		for (int ct = 0; ct < targetStr.length(); ct++) {
			char ch = targetStr.charAt(ct);

			if (SANITIZING_CHAR_MAP.containsKey(ch)) {
				ch = SANITIZING_CHAR_MAP.get(ch);
			}

			sanitizedStr.append(String.valueOf(ch));
		}

		return sanitizedStr.toString();

	}
	
	/**
	 * 文字列のnull、空チェック
	 * nullまたは空文字の場合trueを返す
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(Object str) {
		
		boolean result = false;
		
		if (str == null || "".equals(str)) {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 文字列のnull、空チェック
	 * nullまたは空文字の場合falseを返す
	 * 
	 * @param targetStr
	 * @return
	 */
	public static boolean isNotEmpty(String targetStr) {

		boolean result = false;

		if (targetStr != null && "".equals(targetStr) == false) {
			result = true;
		}

		return result;
	}
	
	/**
	 * 規則の文字列をカンマ区切りでリストへ変換
	 * 
	 * @param sourceStr
	 * @return リスト
	 */
	public static List<String> toStringListByBunKatsu(String sourceStr, String sep) {

		if (sourceStr == null || "".equals(sourceStr)) {
			return null;
		}

		return Stream.of(sourceStr.split(sep)).toList() ;
	}
	
	/**
	 * count回数の指定文字列を戻す
	 * 
	 * @param sourceStr
	 * @param count
	 * @return sourceStr * count
	 */
	public static String toCountString(String sourceStr, Integer count) {

		if (sourceStr == null || "".equals(sourceStr)) {
			return null;
		}

        StringBuilder sb = new StringBuilder();
        sb.append("");
        
        for (int i = 0; i < count; i++) {
            sb.append(sourceStr);
        }
        
		return sb.toString() ;
	}
}
