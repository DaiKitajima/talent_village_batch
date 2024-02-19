package jp.co.bng.talentvillagebatch.db_chat.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "salesforce.chatparticipant__c")
public class ChatparticipantDto {
	/** DBシリアルNo */
	@Id
	@Column("id")
	private Integer id;

	/** ID */
	@Column("sfid")
	private String sfid;
	
	@Column("consultant_id__c")
	private String consultantId;
	
	@Column("name")
	private String name;
	
	@Column("channel_id__c")
	private String channelId;
	
	@Column("candidate_id__c")
	private String candidateId;
	
	@Column("isdeleted")
	private boolean isdeleted;
	
	@Column("systemmodstamp")
	private LocalDateTime systemmodstamp;
	
	@Column("client_id__c")
	private String clientId;
	
	@Column("external_id__c")
	private String externalId;
	
	@Column("createddate")
	private LocalDateTime createddate;
	
	@Column("_hc_lastop")
	private String hcLastop;
	
	@Column("_hc_err")
	private String hcErr;
	
	@Column("last_read_at__c")
	private LocalDateTime lastReadAt;
}
