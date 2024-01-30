package jp.co.bng.talentvillagebatch.batch.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommonConstant {
	
	/* 改行符  */
	public final static String LINE_SEP = System.lineSeparator();
	
	/* SF上、チャット内容纏めの最大サイズ：ディフォルト5000文字設定  */
	public final static Integer MESSAGE_CONTENT_MAX_SIZE = 5000;
}
