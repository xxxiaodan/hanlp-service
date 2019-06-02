package com.wordanalysis.hanlpservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.wordanalysis.hanlpservice.util.HanlpProperties;
import com.wordanalysis.hanlpservice.util.Res;
import com.wordanalysis.hanlpservice.util.Segmentor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
@Api(tags="词法分析器")
@RestController("/WordAnalysis")
@EnableConfigurationProperties(HanlpProperties.class)
public class WordAnalysisController {
    @Autowired
    private HanlpProperties hanlpProperties;

    @ApiOperation("HanlpService-file")
    @RequestMapping(value="/HanlpService-file",method= RequestMethod.POST)
    public Res wordAnalysis_file(@ApiParam("待处理数据（文件）") @RequestParam MultipartFile text_file,
                             @ApiParam("文本长度上限(默认：500Kb)") @RequestParam(defaultValue = "500") Long max_input,
                             @ApiParam("最小分词粒度(≥1),默认2") @RequestParam(defaultValue="2") int min_seg_length,
                             @ApiParam("分词器(0:默认，标准分词器；1：最短路径分词器；2：N-最短路径分词)") @RequestParam(defaultValue="0") int segment_control,
                             @ApiParam("用户词典") @RequestParam(defaultValue="null") MultipartFile customdict_file) throws IOException, InterruptedException {
        if (text_file.isEmpty() || customdict_file.getName()==null) return Res.error("上传文件为空");
        if (text_file.getSize() > max_input*1024) return Res.error("上传文件超过上限(<=" + max_input + "Kb)");

        String text = new String(text_file.getBytes());
        String sentence = text.replace("\r\n","");

        //删除CustomDictionary.txt.bin文件
        File binFile = new File(hanlpProperties.getMyDictionaryPath()+"CustomDictionary.txt.bin");
        if (binFile.exists()){
            binFile.delete();
            System.out.println("CustomDictionary.txt.bin deleted!");
        }

        //将词典文件储存到临时文件夹
        String filePath = hanlpProperties.getMyDictionaryPath() + customdict_file.getOriginalFilename();
        File tempFile = new File(filePath);
        FileUtils.copyInputStreamToFile(customdict_file.getInputStream(), tempFile);

        JSONObject sent_obj = Segmentor.get_Tokens(sentence, min_seg_length, segment_control, filePath);

        //删除临时文件；
        tempFile.delete();

        return Res.ok().put("result",sent_obj);
    }

    @ApiOperation("HanlpService-text")
    @RequestMapping(value="/HanlpService-text",method= RequestMethod.POST)
    public Res wordAnalysis_text(@ApiParam("待处理数据(文本)") @RequestParam  String sentence,
                                 @ApiParam("文本长度上限(默认：200)") @RequestParam(defaultValue = "200") int max_input,
                                 @ApiParam("最小分词粒度(≥1),默认2") @RequestParam(defaultValue="2") int min_seg_length,
                                 @ApiParam("分词器(0:默认，标准分词器；1：最短路径分词器；2：N-最短路径分词)") @RequestParam(defaultValue="0") int segment_control,
                                 @ApiParam("用户词典") @RequestParam(defaultValue="null") MultipartFile customdict_file) throws IOException, InterruptedException {

        if (sentence.length() > max_input) return Res.error("输入文本长度超过上限" + "(≤" + max_input + ")");
        sentence = sentence.replace("\r\n","");

        //删除CustomDictionary.txt.bin文件
        File binFile = new File(hanlpProperties.getMyDictionaryPath()+"CustomDictionary.txt.bin");
        if (binFile.exists()){
            binFile.delete();
            System.out.println("CustomDictionary.txt.bin deleted!");
        }

        //将词典文件储存到临时文件夹
        String filePath = hanlpProperties.getMyDictionaryPath() + customdict_file.getOriginalFilename();
        File tempFile = new File(filePath);
        FileUtils.copyInputStreamToFile(customdict_file.getInputStream(), tempFile);

        JSONObject sent_obj = Segmentor.get_Tokens(sentence, min_seg_length, segment_control, filePath);

        //删除临时文件；
        tempFile.delete();

        return Res.ok().put("result",sent_obj);
    }

}

