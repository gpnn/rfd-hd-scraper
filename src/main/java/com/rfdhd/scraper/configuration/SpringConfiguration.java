package com.rfdhd.scraper.configuration;

import ch.qos.logback.classic.Logger;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Authentication;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.model.configuration.EmailSettings;
import com.rfdhd.scraper.services.Scraper;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.FileNotFoundException;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
@ComponentScan("com.rfdhd.scraper")
public class SpringConfiguration {

    private static Logger logger = (Logger) LoggerFactory.getLogger(SpringConfiguration.class);
    private Configuration configuration = null;

    @Bean
    FilePaths getFilePaths() {
        configuration = getConfiguration();
        String rootFolder = configuration.getRootFolder();

        FilePaths filePaths = new FilePaths();

        filePaths.setScrapingsJson(rootFolder + "scrapings.json");
        filePaths.setDailyDigestJson(rootFolder + "dailyDigest.json");
        filePaths.setArchiveJson(rootFolder + "archive.json");

        logger.info("Setting scrapingsJson to: {}", filePaths.getScrapingsJson());
        logger.info("Setting dailyDigestJson to: {}", filePaths.getDailyDigestJson());
        logger.info("Setting archiveJson to: {}", filePaths.getArchiveJson());

        return filePaths;
    }

    @Bean
    Configuration getConfiguration() {

        if (configuration == null) {
            ConfigurationLoader configurationLoader = new ConfigurationLoader();
            try {
                logger.info("Loading configuration.json");
                configuration = configurationLoader.loadConfiguration();
            } catch (NoConfigurationException | FileNotFoundException e) {
                logger.error("Error with loading configuration | {}", e.getMessage());
                logger.error("Exiting app.");
                System.exit(1);
            }

            configuration.updateUsernameAndPassword();

            return configuration;
        }

        return configuration;
    }

    @Bean
    Scraper getScraper() {
        configuration = getConfiguration();

        return new Scraper(configuration.getPages());
    }

    @Bean
    JavaMailSender getJavaMailSender() {
        configuration = getConfiguration();
        EmailSettings emailSettings = configuration.getEmailSettings();
        Authentication authentication = emailSettings.getAuthentication();

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailSettings.getHost());
        mailSender.setPort(emailSettings.getPort());
        mailSender.setProtocol(emailSettings.getProtocol());

        mailSender.setUsername(authentication.getUsername());
        mailSender.setPassword(authentication.getPassword());

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.timeout", 5000);
        props.put("mail.smtp.connectiontimeout", 5000);
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

}
