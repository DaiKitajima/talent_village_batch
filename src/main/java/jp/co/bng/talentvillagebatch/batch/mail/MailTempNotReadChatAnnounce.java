package jp.co.bng.talentvillagebatch.batch.mail;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shu.shun TODO:仮置き、メールテンプレート待ち
 *
 */
public class MailTempNotReadChatAnnounce extends MailTemplateBase {

	/** 閲覧日時 */
	@Getter @Setter
	private String sendDateTime;
	
	/** 候補者ID */
	@Getter @Setter
	private String candidateId;
	
	/** 候補者氏名 */
	@Getter @Setter
	private String candidateName;
	
	/** 案件ID */
	@Getter @Setter
	private String opportunityId;

	/** 企業名 */
	@Getter @Setter
	private String companyName;
	
	/** 職種（案件名） */
	@Getter @Setter
	private String opportunityName;

	/** 求人内容 */
	@Getter @Setter
	private String companyIntroduction;
	
}
