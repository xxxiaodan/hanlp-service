package com.wordanalysis.hanlpservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.wordanalysis.hanlpservice.util.HanlpProperties;
import com.wordanalysis.hanlpservice.util.Res;
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


    @ApiOperation("HanlpService")
    @RequestMapping(value="/HanlpService",method= RequestMethod.POST)
    public Res wordAnalysis(@ApiParam("待处理数据") @RequestParam MultipartFile text_file,@ApiParam("最小分词粒度(≥1),默认2") @RequestParam(defaultValue="2") int min_seg_length,
                            @ApiParam("分词器(0:默认，标准分词器；1：最短路径分词器；2：N-最短路径分词)") @RequestParam(defaultValue="0") int segment_control,
                            @ApiParam("用户词典") @RequestParam MultipartFile customdict_file) throws IOException, InterruptedException {
        if (text_file.isEmpty() || customdict_file.getName()==null) return Res.error("上传文件为空");

        String text = new String(text_file.getBytes());
        String sentence = text.replace("\r\n","");

        //删除CustomDictionary.txt.bin文件
        File binFile = new File(hanlpProperties.getMyDictionaryPath()+"CustomDictionary.txt.bin");
        if (binFile.exists()|| text_file.isEmpty() || customdict_file.getName()==null){
            binFile.delete();
            System.out.println("CustomDictionary.txt.bin deleted!");
        }

        String filePath = hanlpProperties.getMyDictionaryPath() + customdict_file.getOriginalFilename();
        File tempFile = new File(filePath);
        FileUtils.copyInputStreamToFile(customdict_file.getInputStream(), tempFile);


        //标准分词器
        StandardTokenizer standard_segment = new StandardTokenizer();
        standard_segment.SEGMENT.enableOffset(true).enableIndexMode(min_seg_length).enableCustomDictionaryForcing(true).enableAllNamedEntityRecognize(true);
        //N-最短路径分词
        Segment nShortSegment = new NShortSegment().enableOffset(true).enableIndexMode(min_seg_length).enableCustomDictionaryForcing(true).enableAllNamedEntityRecognize(true);
        //最短路径分词器
        Segment shortestSegment = new DijkstraSegment().enableOffset(true).enableIndexMode(min_seg_length).enableCustomDictionaryForcing(true).enableAllNamedEntityRecognize(true);

        //分词
        JSONObject sent_obj = new JSONObject();
        List<JSONObject> item_list = new ArrayList<JSONObject>();
        sent_obj.put("text", sentence);
        String segmenter;
        List<Term> termList;
        switch (segment_control) {
            case 0:
                termList = standard_segment.segment(sentence);
                segmenter = "标准分词器";
                break;
            case 1:
                termList = shortestSegment.seg(sentence);
                segmenter = "最短路径分词器";
                break;
            case 2:
                termList = nShortSegment.seg(sentence);
                segmenter = "N-最短路径分词";
                break;
            default:
                termList = standard_segment.segment(sentence);
                segmenter = "标准分词器";
        }

        sent_obj.put("segmenter", segmenter);
        sent_obj.put("segment_min_size", min_seg_length);
        for (Term term : termList)
        {
            JSONObject word_obj = new JSONObject();
            //词汇
            word_obj.put("item", term.word);
            //单词长度
            word_obj.put("byte_length", term.length());
            //在文中的起始位置
            String offset = "[" + term.offset + ":" + (term.offset + term.word.length()) + "]";
            word_obj.put("byte_offset", offset);
            //词性
            word_obj.put("pos", term.nature.toString());
            //命名实体识别
            String entity;
            String pos;
            if (term.nature.toString().length() >= 3){
                pos = term.nature.toString().substring(0,2);
            }else {
                pos = term.nature.toString();
            }
            switch(pos) {
                case "nr":
                    entity = "PER";
                    break;
                case "ns":
                    entity = "LOC";
                    break;
                case "nt":
                    entity = "ORG";
                    break;
                case "t":
                    entity = "TIME";
                    break;
                default:
                    entity = "";
            }
            word_obj.put("ner", entity);
            item_list.add(word_obj);
        }
        sent_obj.put("items", item_list);
        //System.out.println(termList);

        //删除临时文件；
        tempFile.delete();

        return Res.ok().put("result",sent_obj);
    }
}

