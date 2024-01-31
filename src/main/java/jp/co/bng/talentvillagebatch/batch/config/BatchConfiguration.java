package jp.co.bng.talentvillagebatch.batch.config;

import java.util.Date;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import jp.co.bng.talentvillagebatch.batch.chunk.ChatProcessor;
import jp.co.bng.talentvillagebatch.batch.chunk.ChatReader;
import jp.co.bng.talentvillagebatch.batch.listener.JobListener;
import jp.co.bng.talentvillagebatch.db_chat.dto.DbChatDto;
import jp.co.bng.talentvillagebatch.db_chat.dto.DbChatSfDto;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private JobLauncher jobLauncher;
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	// Job起動
	@Scheduled(cron="${scheduled.cron.task}")
	public ExitStatus start() throws Exception {

       JobExecution jobExet = jobLauncher.run(
    		   									chatHistorySynchronizeJob(null,null,null),
    		   									new JobParametersBuilder()
    		   											.addDate("batchStartDate", new Date())
    		   											.toJobParameters()
    		   								);
       return jobExet.getExitStatus();
   }
	
	// ====================================================================
	// チャット履歴同期Job定義
	// ====================================================================
	@Bean
	public Job chatHistorySynchronizeJob(JobListener jobListener, Step stepChatSync, Step stepSendGrid) {
		return jobBuilderFactory.get("chatHistorySynchronizeJob")
//			.incrementer(new RunIdIncrementer())
			.listener(jobListener)
			.start(stepChatSync)
//			.next(stepSendGrid)
			.build();
	}
	
	// ====================================================================
	// チャット履歴同期Step定義
	// ====================================================================
	@Bean
	public Step stepChatSync(ChatReader chatReader, 
			ChatProcessor chatProcessor, 
			ClassifierCompositeItemWriter<DbChatSfDto> compositeChatWriter) {
		return stepBuilderFactory.get("stepChatSync")
				.<DbChatDto, DbChatSfDto> chunk(1)
				.reader(chatReader)
				.processor(chatProcessor)
				.writer(compositeChatWriter)
				.build();
	}
	
	// ====================================================================
	// 履歴同期後送信Step定義
	// ====================================================================
//	@Bean
//	public Step stepSendGrid(ChatReader chatReader, 
//			ChatProcessor chatProcessor, 
//			ClassifierCompositeItemWriter<DbChatDto> compositeChatWriter) {
//		return stepBuilderFactory.get("stepSendGrid")
//				.reader(chatReader)
//				.processor(chatProcessor)
//				.writer(compositeChatWriter)
//				.build();
//	}
}
