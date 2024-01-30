package jp.co.bng.talentvillagebatch.db_chat.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.co.bng.talentvillagebatch.db_chat.dto.DbChatSfDto;
import lombok.extern.slf4j.Slf4j;

/**
 * チャットDBアクセスDAO
 * @author shu.shun
 *
 */
@Repository
@Slf4j
public class DbChatSfDao extends DbBaseDao {
	/**
	 * テーブル内最終更新データを取得
	 * @return DB内最新データ
	 */
	public LocalDateTime getLatestUpdateDateTime() {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();

//		sql.append("SELECT ");
//		sql.append("    update_date ");
//		sql.append("FROM ");
//		sql.append("    m_client_company ");
//		sql.append("ORDER BY ");
//		sql.append("    update_date DESC ");
//		sql.append("LIMIT ");
//		sql.append("    1 ");
		
		LocalDateTime ret = null;
		try {
			ret = jdbcTemplate.queryForObject(sql.toString(), LocalDateTime.class);
		} catch (EmptyResultDataAccessException ex) {
			log.warn("検索結果0件");
			throw ex;
		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return ret;
	}
	
	/**
	 * チャンネルIDをキーにした存在判定
	 * @param channelId
	 * @return
	 */
	public boolean isExist(String channelId) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ");
		sql.append("    EXISTS( ");
		sql.append("        SELECT * FROM salesforce.chat_sf__c ");
		sql.append("        WHERE channel_id__c = ? ");
		sql.append("    ) AS isExist ");
		
		boolean ret;
		try {
			ret = jdbcTemplate.queryForObject(sql.toString(), Boolean.class, channelId);
		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.debug("channelId: {} , isExist: {}", channelId, ret);
		
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return ret;
	}
	
	/**
	 * チャンネルIDをキーにしたレコード取得
	 * @param channelId
	 * @return
	 */
	public List<DbChatSfDto>  getChatSfByChannelId(String channelId) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ");
		sql.append("      no__c                as noC ");
		sql.append("    , content__c           as contentC ");
		sql.append("    , name                 as name ");
		sql.append("    , channel_id__c        as channelIdC ");
		sql.append("    , isdeleted            as isdeleted ");
		sql.append("    , systemmodstamp       as systemmodstamp ");
		sql.append("    , external_id__c       as externalIdC ");
		sql.append("    , createddate          as createddate ");
		sql.append("    , sfid                 as sfid ");
		sql.append("    , id                   as id ");
		sql.append("    , _hc_lastop           as hcLastop ");
		sql.append("    , _hc_err              as hcErr ");
		sql.append("FROM ");
		sql.append("    salesforce.chat_sf__c ");
		sql.append("WHERE ");
		sql.append("    channel_id__c = ? ");
		sql.append("ORDER BY ");
		sql.append("    no__c ");
		
		List<DbChatSfDto> chatSfLst = null;
		try {
			// クエリ実行
			RowMapper<DbChatSfDto> rm = new BeanPropertyRowMapper<DbChatSfDto>(DbChatSfDto.class);
			chatSfLst = jdbcTemplate.query(sql.toString(), rm , channelId );
		} catch (Exception ex) {
			log.error("チャット内容取得処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return chatSfLst;
	}
}
