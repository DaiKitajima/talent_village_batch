package jp.co.bng.talentvillagebatch.db_chat.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.co.bng.talentvillagebatch.db_chat.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

/**
 * コンサルタント情報DBアクセスDAO
 * @author shu.shun
 *
 */
@Repository
@Slf4j
public class UserDao extends DbBaseDao {
	
	/**
	 * SFIDより、コンサルタント情報取得
	 */
	public UserDto getUserBySfid(String sfid) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("      faceimagepath__c             as faceimagepath ");
		sql.append("    , lastname ");
		sql.append("    , name ");
		sql.append("    , issuedflg__c                 as issuedflg ");
		sql.append("    , businessmobilephonenumber__c as businessmobilephonenumber ");
		sql.append("    , isactive ");
		sql.append("    , systemmodstamp ");
		sql.append("    , 'alias' ");
		sql.append("    , external_id__c               as externalId ");
		sql.append("    , createddate ");
		sql.append("    , communitynickname ");
		sql.append("    , firstname ");
		sql.append("    , eeasyurl__c                  as eeasyurl ");
		sql.append("    , email ");
		sql.append("    , jobboardpassword__c          as jobboardpassword ");
		sql.append("    , sfid ");
		sql.append("    , id ");
		sql.append("    , _hc_lastop                   as hcLastop ");
		sql.append("    , _hc_err                      as hcErr ");
		sql.append("    , slack_id__c                  as slackId ");
		sql.append("FROM ");
		sql.append("    salesforce.'user' ");
		sql.append("WHERE ");
		sql.append("    sfid = ? ");

		
		UserDto user = new UserDto();
		try {
			// クエリ実行
			RowMapper<UserDto> rm = new BeanPropertyRowMapper<UserDto>(UserDto.class);
			user = jdbcTemplate.queryForObject(sql.toString(), rm , sfid );
		} catch (EmptyResultDataAccessException ex) {
			log.warn("検索結果0件");
			throw ex;
		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return user;
	}
}
