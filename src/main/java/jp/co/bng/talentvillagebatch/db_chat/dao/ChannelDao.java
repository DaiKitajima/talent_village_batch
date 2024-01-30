package jp.co.bng.talentvillagebatch.db_chat.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.co.bng.talentvillagebatch.db_chat.dto.ChannelDto;
import lombok.extern.slf4j.Slf4j;

/**
 * 候補者情報DBアクセスDAO
 * @author shu.shun
 *
 */
@Repository
@Slf4j
public class ChannelDao extends DbBaseDao {
	
	/**
	 * sfidよりチャンネル情報取得
	 */
	public ChannelDto getChannelBySfid(String sfid) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("      name ");
		sql.append("    , is_archived__c as isArchived ");
		sql.append("    , isdeleted ");
		sql.append("    , systemmodstamp ");
		sql.append("    , external_id__c as externalId ");
		sql.append("    , is_individual__c as isIndividual ");
		sql.append("    , createddate ");
		sql.append("    , slack_id__c as slackId ");
		sql.append("    , sfid ");
		sql.append("    , id ");
		sql.append("    , _hc_lastop as hcLastop ");
		sql.append("    , _hc_err as hcErr ");
		sql.append("FROM ");
		sql.append("    salesforce.channel__c ");
		sql.append("WHERE ");
		sql.append("    sfid = ? ");

		
		ChannelDto channel = new ChannelDto();
		try {
			// クエリ実行
			RowMapper<ChannelDto> rm = new BeanPropertyRowMapper<ChannelDto>(ChannelDto.class);
			channel = jdbcTemplate.queryForObject(sql.toString(), rm , sfid );
		} catch (EmptyResultDataAccessException ex) {
			log.warn("検索結果0件");
			throw ex;
		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return channel;
	}
}
