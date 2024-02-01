package jp.co.bng.talentvillagebatch.batch.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommonConstant {
	
	/* チャット内容纏め用：改行符  */
	public final static String CHAT_LINE_SEP = System.lineSeparator();
	
	/* チャット内容纏め用：カンマ  */
	public final static String CHAT_COMMA = ":";
	
	/* チャット内容纏め用：インデント  */
	public final static String CHAT_INDENT = "  ";
	
	/* チャット内容纏め用：項目分割符  */
	public final static String CHAT_SUBJECT_SEP = " ";
	
	/* SF上、1レコードのチャット内容サイズ  */
	public final static Integer MESSAGE_CONTENT_RECORD_SIZE = 40000;
}
