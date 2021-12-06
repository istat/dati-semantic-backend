package it.teamdigitale.ndc.config;

import it.teamdigitale.ndc.harvester.HarvesterService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private HarvesterService harvesterService;
    @Value("#{'${harvester.repositories}'.split(',')}")
    private List<String> repos;

    @Bean
    public Job harvestSemanticAssetsJob() {
        return jobBuilderFactory.get("harvestSemanticAssetsJob")
                .start(harvestStep())
                .build();
    }

    @Bean
    public Step harvestStep() {
        return stepBuilderFactory.get("harvestStep")
                .tasklet(harvestRepositoryProcessor())
                .build();
    }

    @Bean
    public HarvestRepositoryProcessor harvestRepositoryProcessor() {
        return new HarvestRepositoryProcessor(harvesterService, repos);
    }
}