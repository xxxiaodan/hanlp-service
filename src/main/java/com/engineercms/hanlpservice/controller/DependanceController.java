package com.engineercms.hanlpservice.controller;

import com.engineercms.hanlpservice.util.HanlpProperties;
import com.engineercms.hanlpservice.util.Res;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
@Api(tags="依存句法解析")
@RestController("/dependance")
@EnableConfigurationProperties(HanlpProperties.class)
public class DependanceController {
    @Autowired
    private HanlpProperties hanlpProperties;


    @ApiOperation("最大熵依存句法分析器")
    @PostMapping("/max")
    public Res max(@ApiParam("待处理数据") @RequestParam String sentence) {
        if (StringUtils.isEmpty(sentence)) return Res.error("请求数据不能为空");
        if (StringUtils.isEmpty(hanlpProperties.getRoot())) return Res.error("配置文件中的词典路径不能为空");
        if (!fileExists(hanlpProperties.getRoot())) return Res.error("配置文件中的词典路径不存在");
        CoNLLSentence coNLLWords = HanLP.parseDependency(sentence);
        return Res.ok().put("result",coNLLWords);
    }


    private boolean fileExists(String filePath){
        if (StringUtils.isEmpty(filePath)) return false;
        File file = new File(filePath);
        return file.exists();
    }
}
