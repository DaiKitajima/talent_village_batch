package jp.co.bng.talentvillagebatch.batch.tasklet;


import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.bng.talentvillagebatch.batch.mail.MailTempNotReadChatAnnounce;
import jp.co.bng.talentvillagebatch.batch.util.MailProperties;
import jp.co.bng.talentvillagebatch.batch.util.MailUtilInterface;
import jp.co.bng.talentvillagebatch.db_chat.dao.ChatparticipantDao;
import jp.co.bng.talentvillagebatch.db_chat.dto.ChatparticipantDto;
import lombok.extern.slf4j.Slf4j;

@StepScope
@Slf4j
public class SendGridTasklet implements Tasklet {
	@Autowired
	private ChatparticipantDao chatparticipantDao;
	@Autowired
	private MailUtilInterface mailUtil;
	@Autowired
	private MailProperties mailProperties;
	 
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
//        String message = (String) chunkContext.getStepContext().getJobParameters().get("message");
//        ExecutionContext jobContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
//        

    	// 未読チャット情報取得
    	List<ChatparticipantDto> unreadLst =  chatparticipantDao.getChatNotReadChatparticipantLst();
    	if(unreadLst!=null && unreadLst.size() != 0 ) {
    		// 対象者ごとにメール送信
    		unreadLst.forEach(taisyo -> {
    			// TODO:sendGirdでメール送信
    			try {
					this.sendChatNotReadMail(taisyo.getCandidateId());
				} catch (Exception e) {
					log.error("未読チャット通知送信時、エラー発生", e);
				}
    		});
    	}

        return RepeatStatus.FINISHED;
    }
    
	private void sendChatNotReadMail(String candidateId) throws Exception {
		// メールテンプレートより情報設定
		MailTempNotReadChatAnnounce params = new MailTempNotReadChatAnnounce();
		
		// 案件詳細閲覧メールをコンサルタントへ送信
		String enable = "mail.consultant.job.read.enable";
		String title = "mail.consultant.job.read.title";
		String content = "mail.consultant.job.read.content";
		try {
			mailUtil.sendTemlateMail(title, content, null, params, enable);
		} catch (Exception e) {
			log.error("メール送信異常");
			throw new Exception("メール送信異常", e);
		}
	}
}
