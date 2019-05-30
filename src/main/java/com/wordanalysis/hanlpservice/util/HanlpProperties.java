package com.wordanalysis.hanlpservice.util;

import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.model.CRFSegmentModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
@Configuration
@PropertySource("classpath:hanlp.properties")
@Data
@Component
@ConfigurationProperties(ignoreUnknownFields = true)
public class HanlpProperties {

    //private String root;
    //private String CRFCWSModelPath;
    //private String CRFPOSModelPath;
    //private String CRFNERModelPath;
    //private String CRFSegmentModelPath;
    private String MyDictionaryPath;

    //public String getRoot() { return root; }

    //public String getCRFCWSModelPath() { return root+CRFCWSModelPath; }

    //public String getCRFPOSModelPath() { return root+CRFPOSModelPath; }

    //public String getCRFNERModelPath() { return root+CRFPOSModelPath; }

    //public String getCRFSegmentModelPath() { return root+CRFSegmentModelPath; }

    public String getMyDictionaryPath() { return MyDictionaryPath; }
}
