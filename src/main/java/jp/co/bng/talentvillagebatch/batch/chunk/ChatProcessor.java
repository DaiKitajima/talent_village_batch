package jp.co.bng.talentvillagebatch.batch.chunk;


import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.bng.talentvillagebatch.batch.constant.CommonConstant;
import jp.co.bng.talentvillagebatch.batch.constant.UserType;
import jp.co.bng.talentvillagebatch.db_chat.dao.ChannelDao;
import jp.co.bng.talentvillagebatch.db_chat.dao.DbChatSfDao;
import jp.co.bng.talentvillagebatch.db_chat.dao.PersonAccountDao;
import jp.co.bng.talentvillagebatch.db_chat.dao.UserDao;
import jp.co.bng.talentvillagebatch.db_chat.dto.ChannelDto;
import jp.co.bng.talentvillagebatch.db_chat.dto.DbChatDto;
import jp.co.bng.talentvillagebatch.db_chat.dto.DbChatSfDto;
import jp.co.bng.talentvillagebatch.db_chat.dto.PersonAccountDto;
import jp.co.bng.talentvillagebatch.db_chat.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

@StepScope
@Slf4j
public class ChatProcessor implements ItemProcessor<DbChatDto, DbChatSfDto> {
	@Autowired
	private DbChatSfDao dbChatSfDao;
	
	@Autowired
	private PersonAccountDao personAccountDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ChannelDao channelDao;
	
	@Override
	public DbChatSfDto process(DbChatDto dbChatDto) throws Exception {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		DbChatSfDto dbChatSfDto = new DbChatSfDto();
		Map<String, Object> modifiedCont = this.getNextNoAndContent(dbChatDto);
		// ID設定
		if(modifiedCont.get("CHATSF_ID") != null ) {
			dbChatSfDto.setId((Integer) modifiedCont.get("CHATSF_ID"));
		}
		// 枝番設定
		dbChatSfDto.setNoC((Integer) modifiedCont.get("NO"));
		// まとめた内容
		dbChatSfDto.setContentC((String) modifiedCont.get("CONTENT"));
		dbChatSfDto.setName(null);
		dbChatSfDto.setChannelIdC(dbChatDto.getChannelId());
		dbChatSfDto.setIsdeleted(null);
		dbChatSfDto.setSystemmodstamp(null);
		// チャンネルのSlackID＋枝番で生成
		dbChatSfDto.setExternalIdC(this.getSlackIdByChannelId(dbChatDto.getChannelId()) + String.format(".%03d", (Integer)modifiedCont.get("NO")));
		dbChatSfDto.setCreateddate(null);
		// sfid自動生成の為、設定不要
		// dbChatSfDto.setSfid(null);
		dbChatSfDto.setHcLastop(null);
		dbChatSfDto.setHcErr(null);
		
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		return dbChatSfDto;
	}

	/* チャット存在有無より、枝番とチャット内容を整理 */
	private Map<String,Object> getNextNoAndContent(DbChatDto chat) {
		
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		Integer no = 0;  // 枝番
		Integer chatSf_Id = null;  // chat_sf_cのID
		String content = this.getFixedMessage(chat);  // チャット内容
		
		// チャット更新の場合
		if(dbChatSfDao.isExist(chat.getChannelId())) {
			List<DbChatSfDto> chatSfLst = dbChatSfDao.getChatSfByChannelId(chat.getChannelId());
			Optional<DbChatSfDto> lastChat = chatSfLst.stream().max(Comparator.comparing(DbChatSfDto::getNoC));
			String updateContent = lastChat.get().getContentC() + CommonConstant.LINE_SEP + this.getFixedMessage(chat);
			// 既存内容 + 更新内容が5000文字より大きい場合、枝番増やす。そうではない場合、既存内容の最大枝番を設定
			if(updateContent.length() > CommonConstant.MESSAGE_CONTENT_MAX_SIZE) {
				no = (lastChat.get().getNoC()==null ? 0 : lastChat.get().getNoC())  + 1 ;
				content = this.getFixedMessage(chat);
			}else {
				no = (lastChat.get().getNoC()==null ? 0 : lastChat.get().getNoC());
				content = updateContent;
				chatSf_Id = lastChat.get().getId();
			}

		}
		
		result.put("NO", no);
		result.put("CONTENT", content);
		result.put("CHATSF_ID", chatSf_Id);
		
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		return result;
	}
	
	/* 整形したメッセージを取得 */
	private String getFixedMessage(DbChatDto chat) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		// 候補者名前取得 TODO:企業担当者の場合、フェーズ２で対応
		String name = "";
		if(UserType.CONSULTANT.toString().equals(chat.getParticipantType())) {
			UserDto user = userDao.getUserBySfid(chat.getParticipantId());
			name = user.getLastname() + user.getFirstname();
		}else if(UserType.CANDIDATE.toString().equals(chat.getParticipantType())) {
			PersonAccountDto account = personAccountDao.getPersonAccountBySfid(chat.getParticipantId());
			name = account.getLastName() + account.getFirstName();
		}
		
		StringBuilder fixedMessage = new StringBuilder();
		// TODO: 整形仕様待ち
		fixedMessage.append(chat.getPostAt() + " " + name +": " + chat.getMessage() );
		
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		return fixedMessage.toString();
	}
	
	/* 外部ID取得 */
	private String getSlackIdByChannelId(String channelId) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		ChannelDto channel = channelDao.getChannelBySfid(channelId);
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		return channel.getSlackId();
	}
}
