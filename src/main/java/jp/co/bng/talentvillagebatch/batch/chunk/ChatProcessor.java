package jp.co.bng.talentvillagebatch.batch.chunk;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.bng.talentvillagebatch.batch.constant.CommonConstant;
import jp.co.bng.talentvillagebatch.batch.constant.UserType;
import jp.co.bng.talentvillagebatch.batch.util.StringUtil;
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
		Integer chatSfId = null;  // chat_sf_cのID
		String content = this.getFixedMessage(chat);  // チャット内容
		
		// チャンネル・チャット更新の場合
		if(dbChatSfDao.isExist(chat.getChannelId())) {
			// SF上、既存チャンネルのチャット内容取得
			List<DbChatSfDto> chatSfLst = dbChatSfDao.getChatSfByChannelId(chat.getChannelId());
			// 新規チャット内容
			if(chat.getVersion() == 0 ){
				Optional<DbChatSfDto> lastChat = chatSfLst.stream().max(Comparator.comparing(DbChatSfDto::getNoC));
				String updateContent = lastChat.get().getContentC() + this.getFixedMessage(chat);
				// 既存内容 + 更新内容が40000文字より大きい場合、枝番増やす。そうではない場合、既存内容の最大枝番を設定
				if(updateContent.length() > CommonConstant.MESSAGE_CONTENT_RECORD_SIZE) {
					no = (lastChat.get().getNoC()==null ? 0 : lastChat.get().getNoC())  + 1 ;
					content = this.getFixedMessage(chat);
				}else {
					no = (lastChat.get().getNoC()==null ? 0 : lastChat.get().getNoC());
					content = updateContent;
					chatSfId = lastChat.get().getId();
				}
			}else {  // 既存チャット内容の編集
				List<DbChatSfDto> targetChatLst = chatSfLst.stream().filter(chatSf -> chatSf.getContentC().contains(chat.getTimeStamp())).toList();
				if( targetChatLst != null && targetChatLst.size() !=0 ){
					no = targetChatLst.get(0).getNoC();
					chatSfId = targetChatLst.get(0).getId();
					content = this.getUpdateMessage(targetChatLst.get(0).getContentC(), chat);
				}
			}
		}
		
		result.put("NO", no);
		result.put("CONTENT", content);
		result.put("CHATSF_ID", chatSfId);
		
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		return result;
	}
	
	/* 既存チャット内容編集の場合、編集内容と既存内容を合わせて整形して取得 ※1万文字バッファ持ってる為、容量オーバー考慮せず
	 * 整形メッセージの例文：
			[2024/02/02 14:20:00]モモ 太郎(候補者：sfid-cand000001)
			はい、全て完了した。
			1989999.060700@0
			    [2024/02/02 14:20:10]モモ 太郎(候補者：sfid-cand000001）
			    はい、全て完了した。⇒バッチ作成（talent-village-batch）60%
			    1989999.060700@1
			        [2024/02/02 14:20:20]モモ 太郎(候補者：sfid-cand000001)
			        はい、全て完了した。
			        ⇒バッチ作成（talent-village-batch）60%
			        1989999.060700@2
			            【削除済み】[2024/02/02 14:20:30]モモ 太郎(候補者：sfid-cand000001)
			            はい、全て完了した。
			            ⇒バッチ作成（talent-village-batch）60%
			            1989999.060700@3@deleted
    */
	private String getUpdateMessage(String oldContent, DbChatDto chat) {
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(oldContent.split(CommonConstant.CHAT_LINE_SEP)));
		// 最新既存チャットのインデックス取得
		int index = list.size() -1; // ディフォルトは末尾に設定
		for(int i= list.size() -1;i>=0;i--){
			if(list.get(i).contains(chat.getTimeStamp())) {
				index = i;
				break;
			} 
		}
		list.add(index + 1 , this.getFixedMessage(chat));
		
		return list.stream().collect(Collectors.joining(CommonConstant.CHAT_LINE_SEP));
	}

	/* 新規チャット内容を整形して取得 
	 * 整形メッセージの例文： 
	 * [2024/02/02 14:10:00]モモ 太郎（コンサルタント：sfid-cnsl000001）
	 * 各位、作業進捗を報告してください。
	 * 1988888.060688@0
	 * 
	*/
	private String getFixedMessage(DbChatDto chat) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		// 候補者名前取得 TODO:企業担当者の場合、フェーズ２で対応
		String name = "";
		String type = "";
		if(UserType.CONSULTANT.toString().equals(chat.getParticipantType())) {
			UserDto user = userDao.getUserBySfid(chat.getParticipantId());
			name = user.getLastname() + user.getFirstname();
			type = UserType.CONSULTANT.getLabel();
		}else if(UserType.CANDIDATE.toString().equals(chat.getParticipantType())) {
			PersonAccountDto account = personAccountDao.getPersonAccountBySfid(chat.getParticipantId());
			name = account.getLastName() + account.getFirstName();
			type = UserType.CANDIDATE.getLabel();
		}
		
		String indent = StringUtil.toCountString(CommonConstant.CHAT_INDENT, chat.getVersion());
		StringBuilder fixedMessage = new StringBuilder();
		fixedMessage.append(indent + (chat.isDeleted()? "【削除済み】": "" ) + "["+ chat.getPostAt() + "]" + name + "(" + type + CommonConstant.CHAT_COMMA + chat.getParticipantId() + ")" + CommonConstant.CHAT_LINE_SEP);
		for(String content : chat.getMessage().split(CommonConstant.CHAT_LINE_SEP)){
			fixedMessage.append(indent + content + CommonConstant.CHAT_LINE_SEP);
		}
		fixedMessage.append(indent + chat.getTimeStamp() + "@" + chat.getVersion() + (chat.isDeleted()? "@deleted": ""  + CommonConstant.CHAT_LINE_SEP) );
		
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		return fixedMessage.toString();
	}
	
	/* SlackID取得 */
	private String getSlackIdByChannelId(String channelId) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		ChannelDto channel = channelDao.getChannelBySfid(channelId);
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		return channel.getSlackId();
	}
}
