package jp.co.bng.talentvillagebatch.db_chat.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.co.bng.talentvillagebatch.db_chat.dto.PersonAccountDto;
import lombok.extern.slf4j.Slf4j;

/**
 * 候補者情報DBアクセスDAO
 * @author shu.shun
 *
 */
@Repository
@Slf4j
public class PersonAccountDao extends DbBaseDao {
	
	/**
	 * SFIDより、候補者情報取得
	 */
	public PersonAccountDto getPersonAccountBySfid(String sfid) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("	id 									as id, ");
		sql.append("	sfid								as sfid, ");
		sql.append("	name								as name, ");
		sql.append("	googleidentifier__c					as googleId, ");
		sql.append("	googleidentifierchangedate__c		as googleIdAt, ");
		sql.append("	facebookidentifier__c				as facebookId, ");
		sql.append("	facebookidentifierchangedate__c		as facebookIdAt, ");
		sql.append("	linkedinidentifier__c				as linkedInId, ");
		sql.append("	linkedinidentifierchangedate__c		as linkedInIdAt, ");
		sql.append("	jobboardlogindate_latest__c			as latestLogin, ");
		sql.append("	jobboardlogindate_previous__c		as previousLogin, ");
		sql.append("	jobboardlogincount__c				as loginCount, ");
		sql.append("	accountstatus__c					as accountStatus, ");
		sql.append("	accountissuedate__c					as accountIssueAt, ");
		sql.append("	email_jb__c							as mailWrite, ");
		sql.append("	emailinputdate_jb__c				as mailWriteAt, ");
		sql.append("	emailsync__c						as mail, ");
		sql.append("	password__c							as password, ");
		sql.append("	passwordchangedate__c				as passwordWriteAt, ");
		sql.append("	lastname_jb__c						as lastNameWrite, ");
		sql.append("	lastnameinputdate_jb__c				as lastNameWriteAt, ");
		sql.append("	lastnamesync__c						as lastName, ");
		sql.append("	firstname_jb__c						as firstNameWrite, ");
		sql.append("	firstnameinputdate_jb__c			as firstNameWriteAt, ");
		sql.append("	firstnamesync__c					as firstName, ");
		sql.append("	lastnamekana_jb__c					as lastKanaWrite, ");
		sql.append("	lastnamekanainputdate_jb__c			as lastKanaWriteAt, ");
		sql.append("	lastnamekanasync__c					as lastKana, ");
		sql.append("	firstnamekana_jb__c					as firstKanaWrite, ");
		sql.append("	firstnamekanainputdate_jb__c		as firstKanaWriteAt, ");
		sql.append("	firstnamekanasync__c				as firstKana, ");
		sql.append(" ");
		sql.append("	addressinputdate_jb__c				as addressWriteAt, ");
		sql.append("	country_jb__c						as countryWrite, ");
		sql.append("	country_sync__c						as country, ");
		sql.append("	postalcode_jb__c					as postalCodeWrite, ");
		sql.append("	postalcode_sync__c					as postalCode, ");
		sql.append("	street_jb__c						as streetWrite, ");
		sql.append("	street_sync__c						as street, ");
		sql.append("	city_jb__c							as cityWrite, ");
		sql.append("	city_sync__c						as city, ");
		sql.append("	state_jb__c							as stateWrite, ");
		sql.append("	state_sync__c						as state, ");
		sql.append(" ");
		sql.append("	activitystatus_jb__c				as activityStatusWrite, ");
		sql.append("	activitystatusinputdate_jb__c		as activityStatusWriteAt, ");
		sql.append("	activitystatussync__c				as activityStatus, ");
		sql.append("	activitystatus_previous_jb__c		as previousActivityStatusWrite, ");
		sql.append("	activitystatusinputdate_previous_jb__c	as previousActivityStatusWriteAt, ");
		sql.append("	inclination_jb__c					as intentionWrite, ");
		sql.append("	inclinationinputdate_jb__c			as intentionWriteAt, ");
		sql.append("	inclinationsync__c					as intention, ");
		sql.append("	inclination_previous_jb__c			as previousIntentionWrite, ");
		sql.append("	inclinationinputdate_previous_jb__c	as previousIntentionWriteAt, ");
		sql.append("	phonenumber_jb__c					as phoneNumberWrite, ");
		sql.append("	phonenumberinputdate_jb__c			as phoneNumberWriteAt, ");
		sql.append("	phonenumbersync__c					as phoneNumber, ");
		sql.append("	contactabletime_jb__c				as contactableTimeWrite, ");
		sql.append("	contactabletimeinputdate_jb__c		as contactableTimeWriteAt, ");
		sql.append("	contactabletimesync__c				as contactableTime, ");
		sql.append("	preferredcontactmethod__c			as preferredContactMethod, ");
		sql.append("	preferredcontactmethodchangedate__c	as preferredContactMethodWriteAt, ");
		sql.append("	consultant__c						as contactConsultant, ");
		sql.append("	consultantchangedate__c				as contactConsultantAt ");
		sql.append("FROM ");
		sql.append("    salesforce.personaccount_jb__c ");
		sql.append("WHERE ");
		sql.append("    sfid = ? ");
		
		PersonAccountDto account = new PersonAccountDto();
		try {
			// クエリ実行
			RowMapper<PersonAccountDto> rm = new BeanPropertyRowMapper<PersonAccountDto>(PersonAccountDto.class);
			account = jdbcTemplate.queryForObject(sql.toString(), rm , sfid );
		} catch (EmptyResultDataAccessException ex) {
			log.warn("検索結果0件");
			throw ex;
		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return account;
	}
}
