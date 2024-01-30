package jp.co.bng.talentvillagebatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TalentVillageBatchApplication {

	public static void main(String[] args) {
		/* SpringApplication.run(TalentVillageBatchApplication.class, args); */
		SpringApplication app = new SpringApplication(TalentVillageBatchApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);	// 常駐しないよう設定
        app.run(args);
	}

}
