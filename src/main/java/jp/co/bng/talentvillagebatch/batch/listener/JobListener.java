package jp.co.bng.talentvillagebatch.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobListener extends JobExecutionListenerSupport {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		super.beforeJob(jobExecution);
		log.info("ジョブ開始");
	} 
	 
	@Override
	public void afterJob(JobExecution jobExecution) {
		super.afterJob(jobExecution);
		log.info("ジョブ終了");
	}

}
