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
@Table(value = "salesforce.channel__c")
public class ChannelDto {
	/** DBシリアルNo */
	@Id
	@Column("id")
	private Integer id;

	/** SFID */
	@Column("sfid")
	private String sfid;
	
	/** name */
	@Column("name")
	private String name;
	
	/** is_archived__c */
	@Column("is_archived__c")
	private Boolean isArchived;
	
	/** isdeleted */
	@Column("isdeleted")
	private Boolean isdeleted;
	
	/** systemmodstamp */
	@Column("systemmodstamp")
	private LocalDateTime systemmodstamp;
	
	/** 外部ID */
	@Column("external_id__c")
	private String externalId;
	
	/** is_individual__c */
	@Column("is_individual__c")
	private Boolean isIndividual;
	
	/** createddate */
	@Column("createddate")
	private LocalDateTime createddate;
	
	/** slack_id__c */
	@Column("slack_id__c")
	private String slackId;
	
	/** _hc_lastop */
	@Column("_hc_lastop")
	private String hcLastop;
	
	/** _hc_err */
	@Column("_hc_err")
	private String hcErr;
	
}
