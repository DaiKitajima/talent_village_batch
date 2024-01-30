package jp.co.bng.talentvillagebatch.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jp.co.bng.talentvillagebatch.batch.chunk.ChatProcessor;
import jp.co.bng.talentvillagebatch.batch.chunk.ChatReader;
import jp.co.bng.talentvillagebatch.db_chat.dao.DbChatSfDao;
import jp.co.bng.talentvillagebatch.db_chat.dto.DbChatSfDto;

@Configuration
public class ChatChunkConfiguration {
	@Autowired
	private DbChatSfDao dbChatSfDao;
	
	@Bean
	public ChatReader chatReader() {
		return new ChatReader();
	}

	@Bean
	public ChatProcessor chatProcessor() {
		return new ChatProcessor();
	}
	@Bean
	public JdbcBatchItemWriter<DbChatSfDto> jobInsertWriter(DataSource dataSource) {
		StringBuilder query = new StringBuilder();

		query.append("INSERT ");
		query.append("INTO salesforce.chat_sf__c( ");
		query.append("    no__c ");
		query.append("    , content__c ");
		query.append("    , name ");
		query.append("    , channel_id__c ");
		query.append("    , isdeleted ");
		query.append("    , systemmodstamp ");
		query.append("    , external_id__c ");
		query.append("    , createddate ");
		query.append("    , sfid ");
//		query.append("    , id ");
		query.append("    , _hc_lastop ");
		query.append("    , _hc_err ");
		query.append(") ");
		query.append("VALUES ( ");
		query.append("      :noC ");
		query.append("    , :contentC ");
		query.append("    , :name ");
		query.append("    , :channelIdC ");
		query.append("    , :isdeleted ");
		query.append("    , :systemmodstamp ");
		query.append("    , :externalIdC ");
		query.append("    , :createddate ");
		query.append("    , :sfid ");
//		query.append("    , :id ");
		query.append("    , :hcLastop ");
		query.append("    , :hcErr ");
		query.append(") ");
		
		return new JdbcBatchItemWriterBuilder<DbChatSfDto>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql(query.toString())
			.dataSource(dataSource)
			.build();
	}

	@Bean
	public JdbcBatchItemWriter<DbChatSfDto> jobUpdateWriter(DataSource dataSource) {
		StringBuilder query = new StringBuilder();
		
		query.append("UPDATE salesforce.chat_sf__c ");
		query.append("SET ");
		query.append("      no__c          = :noC ");
		query.append("    , content__c     = :contentC ");
		query.append("    , name           = :name ");
		query.append("    , channel_id__c  = :channelIdC ");
		query.append("    , isdeleted      = :isdeleted ");
		query.append("    , systemmodstamp = :systemmodstamp ");
		query.append("    , external_id__c = :externalIdC ");
		query.append("    , createddate    = :createddate ");
		query.append("    , sfid           = :sfid ");
		query.append("    , _hc_lastop     = :hcLastop ");
		query.append("    , _hc_err        = :hcErr ");
		query.append("WHERE ");
		query.append("    id = :id ");
		
		return new JdbcBatchItemWriterBuilder<DbChatSfDto>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql(query.toString())
			.dataSource(dataSource)
			.build();
	}
	
	@Bean
	public ClassifierCompositeItemWriter<DbChatSfDto> compositeChatWriter(DataSource dataSource){
		ClassifierCompositeItemWriter<DbChatSfDto> compositeWriter = new ClassifierCompositeItemWriter<DbChatSfDto>();
        compositeWriter.setClassifier(dto -> {
            if (dto.getId() != null ) {
                return jobUpdateWriter(dataSource);
            }
            return jobInsertWriter(dataSource);
        });
        
        return compositeWriter;
	}
}
