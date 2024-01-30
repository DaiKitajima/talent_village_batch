package jp.co.bng.talentvillagebatch.db_chat.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DbChatSfDto {
	/** no__c　 5000文字超えた場合、インクリメント1*/
	private Integer noC;

	/** content__c　 チャットコンテンツ */
	private String contentC;

	/** 名前 */
	private String name;

	/** channel_id__c　チャット・チャンネルID */
	private String channelIdC;

	/** 削除フラグ */
	private Boolean isdeleted;

	/** システムタイムスタンプ */
	private LocalDateTime systemmodstamp;
	
	/** external_id__c 外部ID*/
	private String externalIdC;

	/** 作成日時 */
	private LocalDateTime createddate;

	/** SF上保存ID */
	private String sfid;

	/** ID */
	private Integer id;

	/** _hc_lastop */
	private Integer hcLastop;

	/** _hc_err */
	private Integer hcErr;
}
