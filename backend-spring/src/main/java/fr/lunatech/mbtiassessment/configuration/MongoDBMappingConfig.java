package fr.lunatech.mbtiassessment.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoDBMappingConfig implements InitializingBean {

    private final MappingMongoConverter mappingMongoConverter;

    public MongoDBMappingConfig(MappingMongoConverter mappingMongoConverter) {
        this.mappingMongoConverter = mappingMongoConverter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
