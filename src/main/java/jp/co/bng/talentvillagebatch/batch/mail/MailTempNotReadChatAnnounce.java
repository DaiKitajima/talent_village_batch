package jp.co.bng.talentvillagebatch.batch.mail;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shu.shun TODO:仮置き、メールテンプレート待ち
 *
 */
public class MailTempNotReadChatAnnounce extends MailTemplateBase {
	
	/** 候補者ID */
	@Getter @Setter
	private String candidateId;
	
	/** 候補者氏名 */
	@Getter @Setter
	private String candidateName;
	
	/** チャット名 */
	@Getter @Setter
	private String chatName;
}
