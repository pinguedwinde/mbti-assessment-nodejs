package fr.lunatech.mbtiassessment.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.io.File;

import static fr.lunatech.mbtiassessment.service.util.FileConstants.DIRECTORY_CREATED;
import static fr.lunatech.mbtiassessment.service.util.FileConstants.USER_FOLDER;

@Configuration
public class UsersDirectoryConfig implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(UsersDirectoryConfig.class);

    @Override
    public void run(ApplicationArguments args) {
        if (new File(USER_FOLDER).mkdirs())
            logger.info(DIRECTORY_CREATED + USER_FOLDER);
    }
}
