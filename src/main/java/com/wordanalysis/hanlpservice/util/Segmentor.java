package com.wordanalysis.hanlpservice.util;

import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;

@ApiModel("分词器")
public class Segmentor {

    public static JSONObject get_Tokens(String sentence, int min_seg_length, int segment_control, String filePath){
        //标准分词器
        StandardTokenizer standard_segment = new StandardTokenizer();
        standard_segment.SEGMENT.enableOffset(true).enableIndexMode(min_seg_length).enableCustomDictionaryForcing(true).enableAllNamedEntityRecognize(true);
        //N-最短路径分词
        Segment nShortSegment = new NShortSegment().enableOffset(true).enableIndexMode(min_seg_length).enableCustomDictionaryForcing(true).enableAllNamedEntityRecognize(true);
        //最短路径分词器
        Segment shortestSegment = new DijkstraSegment().enableOffset(true).enableIndexMode(min_seg_length).enableCustomDictionaryForcing(true).enableAllNamedEntityRecognize(true);

        //进行词法分析
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
        return sent_obj;
    }

}
