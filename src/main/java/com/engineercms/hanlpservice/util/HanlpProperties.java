package com.engineercms.hanlpservice.util;

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
    private String root;
    private String CoreDictionaryPathl;
    private String BiGramDictionaryPath;
    private String CoreStopWordDictionaryPath;
    private String CoreSynonymDictionaryDictionaryPath;
    private String PersonDictionaryPath;
    private String PersonDictionaryTrPath;
    private String tcDictionaryRoot;
    private String CRFSegmentModelPath;
    private String HMMSegmentModelPath;
    private boolean ShowTermNature;
    private String IOAdapter;
    private String PerceptronCWSModelPath;
    private String PerceptronPOSModelPath;
    private String PerceptronNERModelPath;
    private String CRFCWSModelPath;
    private String CRFPOSModelPath;
    private String CRFNERModelPath;
}
