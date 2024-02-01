package jp.co.bng.talentvillagebatch.db_chat.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.co.bng.talentvillagebatch.db_chat.dto.DbChatDto;
import lombok.extern.slf4j.Slf4j;

/**
 * チャットDBアクセスDAO
 * @author shu.shun
 *
 */
@Repository
@Slf4j
public class DbChatDao extends DbBaseDao {
	/**
	 * テーブル内最終更新データを取得
	 * @return DB内最新データ
	 */
//	public LocalDateTime getLatestUpdateDateTime() {
//		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
//		
//		// SQL文作成
//		StringBuilder sql = new StringBuilder();
//
//		sql.append("SELECT ");
//		sql.append("    update_date ");
//		sql.append("FROM ");
//		sql.append("    m_client_company ");
//		sql.append("ORDER BY ");
//		sql.append("    update_date DESC ");
//		sql.append("LIMIT ");
//		sql.append("    1 ");
//		
//		LocalDateTime ret = null;
//		try {
//			ret = jdbcTemplate.queryForObject(sql.toString(), LocalDateTime.class);
//		} catch (EmptyResultDataAccessException ex) {
//			log.warn("検索結果0件");
//			throw ex;
//		} catch (Exception ex) {
//			log.error("DB接続処理でエラーが発生しました。");
//			throw ex;
//		}
//
//		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
//		
//		return ret;
//	}
	
//	/**
//	 * HrbcIdをキーにした存在判定
//	 * @param hrbcId
//	 * @return
//	 */
//	public boolean isExist(Integer hrbcId) {
//		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
//		
//		// SQL文作成
//		StringBuilder sql = new StringBuilder();
//		
//		sql.append("SELECT ");
//		sql.append("    EXISTS( ");
//		sql.append("        SELECT * FROM m_client_company ");
//		sql.append("        WHERE hrbc_id = ? ");
//		sql.append("    ) AS isExist ");
//		
//		boolean ret;
//		try {
//			ret = jdbcTemplate.queryForObject(sql.toString(), Boolean.class, hrbcId);
//		} catch (Exception ex) {
//			log.error("DB接続処理でエラーが発生しました。");
//			throw ex;
//		}
//
//		log.debug("hrbcId: {} , isExist: {}", hrbcId, ret);
//		
//		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
//		
//		return ret;
//	}
	
	/**
	 * dbLatestUpdateよりも新しい更新日時を持つデータをすべて取得する。
	 * @return DB内最新データ
	 */
	public List<DbChatDto> getLatestChatList(LocalDateTime dbLatestUpdate) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("    id ");
		sql.append("    , time_stamp ");
		sql.append("    , channel_id ");
		sql.append("    , participant_type ");
		sql.append("    , participant_id ");
		sql.append("    , message ");
		sql.append("    , post_at ");
		sql.append("    , version ");
		sql.append("    , is_deleted ");
		sql.append("FROM ");
		sql.append("    job_board.chat ");
		sql.append("WHERE ");
		sql.append("    post_at >= ? ");
		sql.append("ORDER BY ");
		sql.append("    channel_id,time_stamp,version asc");
		

		List<DbChatDto> chatLst = null;
		try {
			// クエリ実行
			RowMapper<DbChatDto> rm = new BeanPropertyRowMapper<DbChatDto>(DbChatDto.class);
			chatLst = jdbcTemplate.query(sql.toString(), rm , dbLatestUpdate );
		} catch (EmptyResultDataAccessException ex) {
			log.warn("検索結果0件");
			throw ex;
		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return chatLst;
	}
}
