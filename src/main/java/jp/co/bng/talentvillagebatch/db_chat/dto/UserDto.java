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
@Table(value = "salesforce.user")
public class UserDto {
	/** DBシリアルNo */
	@Id
	@Column("id")
	private Integer id;

	/** SFID */
	@Column("sfid")
	private String sfid;
	
	/** faceimagepath__c */
	@Column("faceimagepath__c")
	private String faceimagepath;
	
	/** lastname */
	@Column("lastname")
	private String lastname;
	
	/** isdeleted */
	@Column("name")
	private String name;
	
	/** systemmodstamp */
	@Column("issuedflg__c")
	private Boolean issuedflg;
	
	/** businessmobilephonenumber__c */
	@Column("businessmobilephonenumber__c")
	private String businessmobilephonenumber;
	
	/** isactive */
	@Column("isactive")
	private Boolean isactive;
	
	/** systemmodstamp */
	@Column("systemmodstamp")
	private LocalDateTime systemmodstamp;
	
	/** alias */
	@Column("alias")
	private String alias;
	
	/** external_id__c */
	@Column("external_id__c")
	private String externalId;
	
	/** createddate */
	@Column("createddate")
	private LocalDateTime createddate;
	
	/** communitynickname */
	@Column("communitynickname")
	private String communitynickname;
	
	/** firstname */
	@Column("firstname")
	private String firstname;
	
	/** eeasyurl__c */
	@Column("eeasyurl__c")
	private String eeasyurl;
	
	/** email */
	@Column("email")
	private String email;
	
	/** jobboardpassword__c */
	@Column("jobboardpassword__c")
	private String jobboardpassword;
	
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
