package jp.co.bng.talentvillagebatch.batch.mail;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * メールテンプレート基底クラス
 * @author d.kitajima
 *
 */
public abstract class MailTemplateBase {
	/** 宛先(To)アドレスリスト */
	@Getter @Setter
	private List<String> sendToAddressList;
	/** 宛先(Cc)アドレスリスト */
	@Getter @Setter
	private List<String> sendCcAddressList;

	/** 署名用アドレス */
	@Getter @Setter
	private String senderAddress;

}
