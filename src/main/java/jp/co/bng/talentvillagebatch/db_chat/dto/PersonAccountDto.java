package jp.co.bng.talentvillagebatch.db_chat.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.ObjectUtils;

import jp.co.bng.talentvillagebatch.batch.util.DateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "salesforce.personaccount_jb__c")
public class PersonAccountDto {
	/** DBシリアルNo */
	@Id
	@Column("id")
	private Integer id;

	/** ID */
	@Column("sfid")
	private String sfid;
	
	/** SF専用オブジェクトのID */
	@Column("jobseekerid__c")
	private String externalId;
	
	/** 候補者名 */
	@Column("name")
	private String personAccountName;

	/** Google識別子 */
	@Column("googleidentifier__c")
	private String googleId;

	/** Google識別子(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("googleidentifierchangedate__c")
	private LocalDateTime googleIdAtUTC;

	public LocalDateTime getGoogleIdAt() {
		return DateTimeConverter.toSystemDefault(googleIdAtUTC);
	}

	public void setGoogleIdAt(LocalDateTime local) {
		googleIdAtUTC = DateTimeConverter.toUtc(local);
	}

	/** Facebook識別子 */
	@Column("facebookidentifier__c")
	private String facebookId;

	/** Facebook識別子(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("facebookidentifierchangedate__c")
	private LocalDateTime facebookIdAtUTC;

	public LocalDateTime getFacebookIdAt() {
		return DateTimeConverter.toSystemDefault(facebookIdAtUTC);
	}

	public void setFacebookIdAt(LocalDateTime local) {
		facebookIdAtUTC = DateTimeConverter.toUtc(local);
	}

	/** LinkedIn識別子 */
	@Column("linkedinidentifier__c")
	private String linkedInId;

	/** LinkedIn識別子(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("linkedinidentifierchangedate__c")
	private LocalDateTime linkedInIdAtUTC;

	public LocalDateTime getLinkedInIdAt() {
		return DateTimeConverter.toSystemDefault(linkedInIdAtUTC);
	}

	public void setLinkedInIdAt(LocalDateTime local) {
		linkedInIdAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 最終ログイン日時 
	 *  @deprecated 直接操作禁止
	 */
	@Column("jobboardlogindate_latest__c")
	private LocalDateTime latestLoginUTC;

	public LocalDateTime getLatestLogin() {
		return DateTimeConverter.toSystemDefault(latestLoginUTC);
	}

	public void setLatestLogin(LocalDateTime local) {
		latestLoginUTC = DateTimeConverter.toUtc(local);
	}

	/** 前回ログイン日時 
	 *  @deprecated 直接操作禁止
	 */
	@Column("jobboardlogindate_previous__c")
	private LocalDateTime previousLoginUTC;

	public LocalDateTime getPreviousLogin() {
		return DateTimeConverter.toSystemDefault(previousLoginUTC);
	}

	public void setPreviousLogin(LocalDateTime local) {
		previousLoginUTC = DateTimeConverter.toUtc(local);
	}

	/** ログイン回数 */
	@Column("jobboardlogincount__c")
	private Integer loginCount;

	/** アカウントステータス */
	@Column("accountstatus__c")
	private String accountStatus;

//	public CandidateAccountStatus accountStatusEnum() {
//		return CandidateAccountStatus.ofLabel(getAccountStatus());
//	}

	/** アカウント発行日時 */
	@Column("accountissuedate__c")
	private LocalDate accountIssueAt;

	/** メールアドレス(書込み) */
	@Column("email_jb__c")
	private String mailWrite;

	/** メールアドレス(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("emailinputdate_jb__c")
	private LocalDateTime mailWriteAtUTC;

	public LocalDateTime getMailWriteAt() {
		return DateTimeConverter.toSystemDefault(mailWriteAtUTC);
	}

	public void setMailWriteAt(LocalDateTime local) {
		mailWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** メールアドレス */
	@Column("emailsync__c")
	private String mail;

	/** パスワード */
	@Column("password__c")
	private String password;

	/** パスワード(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("passwordchangedate__c")
	private LocalDateTime passwordWriteAtUTC;

	public LocalDateTime getPasswordWriteAt() {
		return DateTimeConverter.toSystemDefault(passwordWriteAtUTC);
	}

	public void setPasswordWriteAt(LocalDateTime local) {
		passwordWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 姓(書込み) */
	@Column("lastname_jb__c")
	private String lastNameWrite;

	/** 姓(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("lastnameinputdate_jb__c")
	private LocalDateTime lastNameWriteAtUTC;

	public LocalDateTime getLastNameWriteAt() {
		return DateTimeConverter.toSystemDefault(lastNameWriteAtUTC);
	}

	public void setLastNameWriteAt(LocalDateTime local) {
		lastNameWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 姓 */
	@Column("lastnamesync__c")
	private String lastName;

	/** 名(書込み) */
	@Column("firstname_jb__c")
	private String firstNameWrite;

	/** 名(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("firstnameinputdate_jb__c")
	private LocalDateTime firstNameWriteAtUTC;

	public LocalDateTime getFirstNameWriteAt() {
		return DateTimeConverter.toSystemDefault(firstNameWriteAtUTC);
	}

	public void setFirstNameWriteAt(LocalDateTime local) {
		firstNameWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 名 */
	@Column("firstnamesync__c")
	private String firstName;

	/** 姓カナ(書込み) */
	@Column("lastnamekana_jb__c")
	private String lastKanaWrite;

	/** 姓カナ(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("lastnamekanainputdate_jb__c")
	private LocalDateTime lastKanaWriteAtUTC;

	public LocalDateTime getLastKanaWriteAt() {
		return DateTimeConverter.toSystemDefault(lastKanaWriteAtUTC);
	}

	public void setLastKanaWriteAt(LocalDateTime local) {
		lastKanaWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 姓カナ */
	@Column("lastnamekanasync__c")
	private String lastKana;

	/** 名カナ(書込み) */
	@Column("firstnamekana_jb__c")
	private String firstKanaWrite;

	/** 名カナ(書込み)(入力日時)
	 *  @deprecated 直接操作禁止
	 */
	@Column("firstnamekanainputdate_jb__c")
	private LocalDateTime firstKanaWriteAtUTC;

	public LocalDateTime getFirstKanaWriteAt() {
		return DateTimeConverter.toSystemDefault(firstKanaWriteAtUTC);
	}

	public void setFirstKanaWriteAt(LocalDateTime local) {
		firstKanaWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 名カナ */
	@Column("firstnamekanasync__c")
	private String firstKana;

	/** 国(書込み) */
	@Column("country_jb__c")
	private String countryWrite;

	/** 国 */
	@Column("country_sync__c")
	private String country;

	/** 郵便番号(書込み) */
	@Column("postalcode_jb__c")
	private String postalCodeWrite;

	/** 郵便番号 */
	@Column("postalcode_sync__c")
	private String postalCode;

	/** 町名・番地(書込み) */
	@Column("street_jb__c")
	private String streetWrite;

	/** 町名・番地 */
	@Column("street_sync__c")
	private String street;

	/** 市・区(書込み) */
	@Column("city_jb__c")
	private String cityWrite;

	/** 市・区 */
	@Column("city_sync__c")
	private String city;

	/** 都道府県(書込み) */
	@Column("state_jb__c")
	private String stateWrite;

	/** 都道府県 */
	@Column("state_sync__c")
	private String state;

	/** 住所(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("addressinputdate_jb__c")
	private LocalDateTime addressWriteAtUTC;

	public LocalDateTime getAddressWriteAt() {
		return DateTimeConverter.toSystemDefault(addressWriteAtUTC);
	}

	public void setAddressWriteAt(LocalDateTime local) {
		addressWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 活動状況(書込み) */
	@Column("activitystatus_jb__c")
	private String activityStatusWrite;

	/** 活動状況(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("activitystatusinputdate_jb__c")
	private LocalDateTime activityStatusWriteAtUTC;

	public LocalDateTime getActivityStatusWriteAt() {
		return DateTimeConverter.toSystemDefault(activityStatusWriteAtUTC);
	}

	public void setActivityStatusWriteAt(LocalDateTime local) {
		activityStatusWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 活動状況 */
	@Column("activitystatussync__c")
	private String activityStatus;

	/** 前回活動状況(書込み) */
	@Column("activitystatus_previous_jb__c")
	private String previousActivityStatusWrite;

	/** 前回活動状況(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("activitystatusinputdate_previous_jb__c")
	private LocalDateTime previousActivityStatusWriteAtUTC;

	public LocalDateTime getPreviousActivityStatusWriteAt() {
		return DateTimeConverter.toSystemDefault(previousActivityStatusWriteAtUTC);
	}

	public void setPreviousActivityStatusWriteAt(LocalDateTime local) {
		previousActivityStatusWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 案件意向(書込み) */
	@Column("inclination_jb__c")
	private String intentionWrite;

	/** 案件意向(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("inclinationinputdate_jb__c")
	private LocalDateTime intentionWriteAtUTC;

	public LocalDateTime getIntentionWriteAt() {
		return DateTimeConverter.toSystemDefault(intentionWriteAtUTC);
	}

	public void setIntentionWriteAt(LocalDateTime local) {
		intentionWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 案件意向 */
	@Column("inclinationsync__c")
	private String intention;

	/** 案件意向をリスト型で取得 */
	public List<String> getIntentionList() {
		if (ObjectUtils.isEmpty(getIntention())) {
			return new ArrayList<String>();
		}
		return Arrays.asList(getIntention().split(";"));
	}

	/** 前回案件意向(書込み) */
	@Column("inclination_previous_jb__c")
	private String previousIntentionWrite;

	/** 前回案件意向(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("inclinationinputdate_previous_jb__c")
	private LocalDateTime previousIntentionWriteAtUTC;

	public LocalDateTime getPreviousIntentionWriteAt() {
		return DateTimeConverter.toSystemDefault(previousIntentionWriteAtUTC);
	}

	public void setPreviousIntentionWriteAt(LocalDateTime local) {
		previousIntentionWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 電話番号(書込み) */
	@Column("phonenumber_jb__c")
	private String phoneNumberWrite;

	/** 電話番号(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("phonenumberinputdate_jb__c")
	private LocalDateTime phoneNumberWriteAtUTC;

	public LocalDateTime getPhoneNumberWriteAt() {
		return DateTimeConverter.toSystemDefault(phoneNumberWriteAtUTC);
	}

	public void setPhoneNumberWriteAt(LocalDateTime local) {
		phoneNumberWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 電話番号 */
	@Column("phonenumbersync__c")
	private String phoneNumber;

	/** 連絡可能時間(書込み) */
	@Column("contactabletime_jb__c")
	private String contactableTimeWrite;

	/** 連絡可能時間(書込み)(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("contactabletimeinputdate_jb__c")
	private LocalDateTime contactableTimeWriteAtUTC;

	public LocalDateTime getContactableTimeWriteAt() {
		return DateTimeConverter.toSystemDefault(contactableTimeWriteAtUTC);
	}

	public void setContactableTimeWriteAt(LocalDateTime local) {
		contactableTimeWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 連絡可能時間 */
	@Column("contactabletimesync__c")
	private String contactableTime;

	/** 希望連絡手段 */
	@Column("preferredcontactmethod__c")
	private String preferredContactMethod;

	/** 希望連絡手段(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("preferredcontactmethodchangedate__c")
	private LocalDateTime preferredContactMethodWriteAtUTC;

	public LocalDateTime getPreferredContactMethodWriteAt() {
		return DateTimeConverter.toSystemDefault(preferredContactMethodWriteAtUTC);
	}

	public void setPreferredContactMethodWriteAt(LocalDateTime local) {
		preferredContactMethodWriteAtUTC = DateTimeConverter.toUtc(local);
	}

	/** 担当コンサルタント */
	@Column("consultant__c")
	private String contactConsultant;

	/** 担当コンサルタント(入力日時) 
	 *  @deprecated 直接操作禁止
	 */
	@Column("consultantchangedate__c")
	private LocalDateTime contactConsultantAtUTC;

	public LocalDateTime getContactConsultantAt() {
		return DateTimeConverter.toSystemDefault(contactConsultantAtUTC);
	}

	public void setContactConsultantAt(LocalDateTime local) {
		contactConsultantAtUTC = DateTimeConverter.toUtc(local);
	}
	
	/** 非暗号化初期パスワード
	 *  SFで初回ログインを行っていないユーザに対してリマインドを送るために使用する。 */
	@Column("password_plain__c")
	private String rawPassword;

//	/**
//	 * 対象の候補者が有効（ログイン可能）かどうかを判定
//	 * 
//	 * @param candidate
//	 * @return
//	 */
//	public boolean isEnabled() {
//		CandidateAccountStatus sts = CandidateAccountStatus.ofLabel(this.getAccountStatus());
//		// アカウント状態が無効でも退会でもない場合のみ有効とみなす
//		// ※未発行の場合は有効とみなす（初回ソーシャルログイン時に自動的に発行扱いになる仕様のため）
//		return (!CandidateAccountStatus.DISABLED.equals(sts) && !CandidateAccountStatus.WITHDRAWN.equals(sts));
//	}

	/**
	 * 候補者の名前取得（姓＋名）
	 */
	public String getName() {
		return ObjectUtils.isEmpty(lastName) || ObjectUtils.isEmpty(firstName) ? "" : lastName + firstName;
	}
}
