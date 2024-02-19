package jp.co.bng.talentvillagebatch.batch.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * メール設定プロパティアクセスクラス
 * @author s.yamazaki
 *
 */
@Setter
@Getter
@Component
@ConfigurationProperties("mail.setting")
public class MailProperties {
	private SmtpProperties smtp;
	private AuthProperties auth;
	private String sender;
	private String[] info;
	private String[] support;
	private String[] jobBoard;

	@Setter
	@Getter
	public static class SmtpProperties{
		private String host;
		private Integer port;
	}
	@Setter
	@Getter
	public static class AuthProperties{
		private String user;
		private String password;
	}
}
