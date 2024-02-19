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
import jp.co.bng.talentvillagebatch.batch.tasklet.SendGridTasklet;
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
	
	// Job起動(履歴同期)
	@Scheduled(cron="${scheduled.cron.task1}")
	public ExitStatus startJob1() throws Exception {

       JobExecution jobExet = jobLauncher.run(
    		   									chatHistorySynchronizeJob(null,null),
    		   									new JobParametersBuilder()
    		   											.addDate("batchJob1StartDate", new Date())
    		   											.toJobParameters()
    		   								);
       return jobExet.getExitStatus();
   }
	
	// Job起動(新着チャット通知)
	@Scheduled(cron="${scheduled.cron.task2}")
	public ExitStatus startJob2() throws Exception {

       JobExecution jobExet = jobLauncher.run(
    		   									newChatAnnounceJob(null,null),
    		   									new JobParametersBuilder()
    		   											.addDate("batchJob2StartDate", new Date())
    		   											.toJobParameters()
    		   								);
       return jobExet.getExitStatus();
   }
	
	// ====================================================================
	// 新着チャット通知Job定義
	// ====================================================================
	@Bean
	public Job newChatAnnounceJob(JobListener jobListener, Step stepNewChatAnnounce) {
		return jobBuilderFactory.get("newChatAnnounceJob")
				.listener(jobListener)
				.start(stepNewChatAnnounce)
				.build();
	}

	// ====================================================================
	// チャット履歴同期Job定義
	// ====================================================================
	@Bean
	public Job chatHistorySynchronizeJob(JobListener jobListener, Step stepChatSync) {
		return jobBuilderFactory.get("chatHistorySynchronizeJob")
//			.incrementer(new RunIdIncrementer())
			.listener(jobListener)
			.start(stepChatSync)
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
	// 新着チャット通知Step定義
	// ====================================================================
	@Bean
	public Step stepNewChatAnnounce() {
		return stepBuilderFactory.get("stepNewChatAnnounce")
				.tasklet(new SendGridTasklet())
				.build();
	}
}
