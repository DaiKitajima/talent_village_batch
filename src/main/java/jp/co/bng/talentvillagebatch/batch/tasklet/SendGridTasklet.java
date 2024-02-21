package jp.co.bng.talentvillagebatch.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.bng.talentvillagebatch.batch.mail.MailTempNotReadChatAnnounce;
import jp.co.bng.talentvillagebatch.batch.util.MailUtilInterface;
import jp.co.bng.talentvillagebatch.db_chat.dao.ChannelDao;
import jp.co.bng.talentvillagebatch.db_chat.dao.ChatparticipantDao;
import jp.co.bng.talentvillagebatch.db_chat.dao.PersonAccountDao;
import jp.co.bng.talentvillagebatch.db_chat.dto.ChannelDto;
import jp.co.bng.talentvillagebatch.db_chat.dto.ChatparticipantDto;
import jp.co.bng.talentvillagebatch.db_chat.dto.PersonAccountDto;
import lombok.extern.slf4j.Slf4j;

@StepScope
@Slf4j
public class SendGridTasklet implements Tasklet {
	@Autowired
	private ChatparticipantDao chatparticipantDao;
	@Autowired
	private PersonAccountDao personAccountDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private MailUtilInterface mailUtil;
	 
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {      

    	// 未読チャット情報取得
    	List<ChatparticipantDto> unreadChatLst =  chatparticipantDao.getChatNotReadChatparticipantLst();
    	if(unreadChatLst!=null && unreadChatLst.size() != 0 ) {
    		// 対象者ごとにメール送信
    		unreadChatLst.forEach(taisyo -> {
    			// sendGirdでメール送信
    			try {
					this.sendChatNotReadMail(taisyo);
				} catch (Exception e) {
					log.error("未読チャット通知送信時、エラー発生CandidateId:{}", taisyo.getCandidateId(), e);
				}
    		});
    	}

        return RepeatStatus.FINISHED;
    }
    
	private void sendChatNotReadMail(ChatparticipantDto participant) throws Exception {
		// チャンネル情報取得
		ChannelDto channel = channelDao.getChannelBySfid(participant.getChannelId());
		// 候補者情報取得
		PersonAccountDto candidate = personAccountDao.getPersonAccountBySfid(participant.getCandidateId());
		
		// メールテンプレートより情報設定
		MailTempNotReadChatAnnounce params = new MailTempNotReadChatAnnounce();
		List<String> mailLst = new ArrayList<String>();
		mailLst.add(candidate.getMail());
		params.setSendToAddressList(mailLst);
		params.setCandidateId(candidate.getSfid());
		params.setCandidateName(candidate.getName());
		params.setChatName(channel.getName());
		
		// 未読通知メール設定
		String enable = "mail.chat.notread.announce.enable";
		String title = "mail.chat.notread.announce.title";
		String content = "mail.chat.notread.announce.content";
		String signature = "mail.signature.content";
		try {
			mailUtil.sendTemlateMail(title, content, signature, params, enable);
		} catch (Exception e) {
			log.error("メール送信異常");
			throw new Exception("メール送信異常", e);
		}
	}
}
