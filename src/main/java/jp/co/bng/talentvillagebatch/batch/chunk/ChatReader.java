package jp.co.bng.talentvillagebatch.batch.chunk;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.bng.talentvillagebatch.db_chat.dao.DbChatDao;
import jp.co.bng.talentvillagebatch.db_chat.dto.DbChatDto;
import lombok.extern.slf4j.Slf4j;

@StepScope
@Slf4j
public class ChatReader implements ItemReader<DbChatDto> {
	@Autowired
	private JobExplorer jobExplorer;
	@Autowired
	private DbChatDao dbChatDao;
	
	private LocalDateTime dbLatestUpdate;
	private int readerIndex = 0;
	private List<DbChatDto> chats = null;
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		
		try {
			// 前回Job実行開始日時取得
			dbLatestUpdate = this.fetchPreviousJobCompletetionDate();
		} catch (Exception ex) {
			dbLatestUpdate = LocalDateTime.of(1970, 1, 1, 00, 00);	//unixtime-zero
		}
		
		log.debug("dbLatestUpdate: {}", dbLatestUpdate);
		
		// BATCH_STEP_EXECUTION_CONTEXTテーブルへの保存のためにステップコンテキストへの変数格納
		ExecutionContext stepContext = stepExecution.getExecutionContext();
		stepContext.put("dbLatestUpdate", dbLatestUpdate.toString());
		
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	public DbChatDto read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());
		log.debug("readerIndex: {}", readerIndex);
		
		// 初回実行時、データの一括ロード
		if (chats == null) {			
			// 新規チャット情報全件取得
			chats = this.fetchChats(dbLatestUpdate);
		}
		
		DbChatDto chat = null;
 
		if (readerIndex < chats.size()) {
			chat = chats.get(readerIndex);
			readerIndex++;
		}
		else {
			readerIndex = 0;
			chats = null;
		}
 
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
        return chat;
	}


	/**
	 * 必要データを一括で全件取得。
	 * dbLatestUpdateよりも新しい更新日時を持つデータをすべて取得する。
	 * 
	 * @param dbLatestUpdate
	 * @return
	 * @throws Exception
	 */
	private List<DbChatDto> fetchChats(LocalDateTime dbLatestUpdate) throws Exception {
		log.info("START:" + new Object(){}.getClass().getEnclosingMethod().getName());

		List<DbChatDto> ret = new ArrayList<DbChatDto>();
		
		ret = dbChatDao.getLatestChatList(dbLatestUpdate);
		
		log.debug("totalCount: {}", ret.size());
		
		log.info("END:" + new Object(){}.getClass().getEnclosingMethod().getName());
		return ret;
	}
	
	
	/**
	 * 前回Job実行開始日時取得
	 * 
	 * @return
	 * @throws Exception
	 */
	private LocalDateTime fetchPreviousJobCompletetionDate() {
		Date previousJobCompleteDate = null;
	    try {
	        Integer jobInstanceCount = jobExplorer.getJobInstanceCount("chatHistorySynchronizeJob");
	        List<JobInstance> jobInstances = jobExplorer.getJobInstances("chatHistorySynchronizeJob", 0, jobInstanceCount);
	        List<JobExecution> jobExecutions = jobInstances.stream().map(jobExplorer::getJobExecutions)
	                .flatMap(List<JobExecution>::stream)
	                .filter(param -> param.getExitStatus().equals(ExitStatus.COMPLETED)).collect(Collectors.toList());
	        if (jobExecutions != null && jobExecutions.size() != 0 ) {
	            Optional<JobExecution> jobExecutionOptional = jobExecutions.stream().sorted((JobExecution execution1, JobExecution execution2) -> execution2.getStartTime()
	                    .compareTo(execution1.getStartTime())).findFirst();
	            if (jobExecutionOptional.isPresent()) {
	                previousJobCompleteDate = jobExecutionOptional.get().getStartTime();
	            }
	        }
	    } catch (NoSuchJobException exception) {
	        log.error("Exception occurred {}", exception.getMessage());
	    }
	    return previousJobCompleteDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
}
