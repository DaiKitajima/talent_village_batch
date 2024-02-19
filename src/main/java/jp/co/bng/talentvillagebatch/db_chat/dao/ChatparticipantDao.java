package jp.co.bng.talentvillagebatch.db_chat.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jp.co.bng.talentvillagebatch.db_chat.dto.ChatparticipantDto;
import lombok.extern.slf4j.Slf4j;

/**
 * チャット参加者情報DBアクセスDAO
 * @author shu.shun
 *
 */
@Repository
@Slf4j
public class ChatparticipantDao extends DbBaseDao {
	
	/**
	 * SFIDより、チャット参加者情報取得
	 */
	public ChatparticipantDto getChatparticipantBySfid(String sfid) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("    consultant_id__c           as  consultantId ");
		sql.append("    , name ");
		sql.append("    , channel_id__c            as  channelId ");
		sql.append("    , candidate_id__c          as  candidateId ");
		sql.append("    , isdeleted ");
		sql.append("    , systemmodstamp ");
		sql.append("    , client_id__c             as  clientId ");
		sql.append("    , external_id__c           as  externalId ");
		sql.append("    , createddate ");
		sql.append("    , sfid ");
		sql.append("    , id ");
		sql.append("    , _hc_lastop               as  hcLastop ");
		sql.append("    , _hc_err                  as  hcErr ");
		sql.append("    , last_read_at__c          as  lastReadAt ");
		sql.append("FROM ");
		sql.append("    salesforce.chatparticipant__c ");
		sql.append("WHERE ");
		sql.append("    sfid = ? ");
		
		ChatparticipantDto participant = new ChatparticipantDto();
		try {
			// クエリ実行
			RowMapper<ChatparticipantDto> rm = new BeanPropertyRowMapper<ChatparticipantDto>(ChatparticipantDto.class);
			participant = jdbcTemplate.queryForObject(sql.toString(), rm , sfid );
		} catch (EmptyResultDataAccessException ex) {
			log.warn("検索結果0件");
			throw ex;
		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return participant;
	}
	
	/**
	 * チャンネルIDより、チャット参加者情報取得
	 */
	public List<ChatparticipantDto> getChatparticipantLstByChannelId(String channelId) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("    consultant_id__c           as  consultantId ");
		sql.append("    , name ");
		sql.append("    , channel_id__c            as  channelId ");
		sql.append("    , candidate_id__c          as  candidateId ");
		sql.append("    , isdeleted ");
		sql.append("    , systemmodstamp ");
		sql.append("    , client_id__c             as  clientId ");
		sql.append("    , external_id__c           as  externalId ");
		sql.append("    , createddate ");
		sql.append("    , sfid ");
		sql.append("    , id ");
		sql.append("    , _hc_lastop               as  hcLastop ");
		sql.append("    , _hc_err                  as  hcErr ");
		sql.append("    , last_read_at__c          as  lastReadAt ");
		sql.append("FROM ");
		sql.append("    salesforce.chatparticipant__c ");
		sql.append("WHERE ");
		sql.append("    channel_id__c = ? ");
		
		List<ChatparticipantDto> participantLst = new ArrayList<ChatparticipantDto>();
		try {
			// クエリ実行
			RowMapper<ChatparticipantDto> rm = new BeanPropertyRowMapper<ChatparticipantDto>(ChatparticipantDto.class);
			participantLst = jdbcTemplate.query(sql.toString(), rm , channelId );

		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return participantLst;
	}
	
	/**
	 * 新着チャット未読対象参加者情報取得
	 */
	public List<ChatparticipantDto> getChatNotReadChatparticipantLst() {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		// SQL文作成
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("      part.channel_id__c            as  channelId ");
//		sql.append("    , part.name ");
//		sql.append("    , part.consultant_id__c         as  consultantId ");
		sql.append("    , part.candidate_id__c          as  candidateId ");
//		sql.append("    , part.isdeleted ");
//		sql.append("    , part.systemmodstamp ");
//		sql.append("    , part.client_id__c             as  clientId ");
//		sql.append("    , part.external_id__c           as  externalId ");
//		sql.append("    , part.createddate ");
//		sql.append("    , part.sfid ");
//		sql.append("    , part.id ");
//		sql.append("    , part._hc_lastop               as  hcLastop ");
//		sql.append("    , part._hc_err                  as  hcErr ");
//		sql.append("    , part.last_read_at__c          as  lastReadAt ");
		sql.append("FROM ");
		sql.append("    salesforce.chatparticipant__c as part ");
		sql.append("INNER JOIN job_board.chat as chat ON part.channel_id__c = chat.channel_id ");
		sql.append("WHERE ");
		sql.append("    part.last_read_at__c <  chat.post_at ");				// 最終閲覧日時より新しい投稿
		sql.append("AND chat.participant_id <> part.candidate_id__c ");			// 投稿者自身は対象外
		sql.append("GROUP BY channelId,candidateId ");
		
		List<ChatparticipantDto> participantLst = new ArrayList<ChatparticipantDto>();
		try {
			// クエリ実行
			RowMapper<ChatparticipantDto> rm = new BeanPropertyRowMapper<ChatparticipantDto>(ChatparticipantDto.class);
			participantLst = jdbcTemplate.query(sql.toString(), rm  );

		} catch (Exception ex) {
			log.error("DB接続処理でエラーが発生しました。");
			throw ex;
		}

		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return participantLst;
	}
}
