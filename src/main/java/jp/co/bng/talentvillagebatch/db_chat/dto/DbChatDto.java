package jp.co.bng.talentvillagebatch.db_chat.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DbChatDto {
	/** ID */
	private Integer id;

	/** Slackタイムスタンプ */
	private String timeStamp;

	/** チャンネルID */
	private String channelId;

	/** 候補者種別 */
	private String participantType;

	/** 候補者ID */
	private String participantId;

	/** 内容 */
	private String message;
	
	/** 投稿（編集）日時 */
	private LocalDateTime postAt;

	/** バージョン */
	private Integer version;

	/** 削除フラグ */
	private boolean isDeleted;

}
