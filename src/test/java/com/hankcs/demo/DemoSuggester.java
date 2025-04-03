/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/9 13:27</create-date>
 *
 * <copyright file="DemoSuggestor.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.mining.word.WordInfo;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.BasicTokenizer;

import java.util.Arrays;
import java.util.List;

/**
 * 文本推荐(句子级别，从一系列句子中挑出与输入句子最相似的那一个)
 * @author hankcs
 */
public class DemoSuggester
{
    public static void main(String[] args)
    {
        Suggester suggester = new Suggester();
        String[] titleArray = {
/*
            "威廉王子发表演说 呼吁保护野生动物",
            "魅惑天后许佳慧不爱“预谋” 独唱《许某某》",
            "《时代》年度人物最终入围名单出炉 普京马云入选",
            "“黑格比”横扫菲：菲吸取“海燕”经验及早疏散",
            "日本保密法将正式生效 日媒指其损害国民知情权",
            "英报告说空气污染带来“公共健康危机”",
*/
            "客厅的窗帘",
            "天气预报",
            "气象情况",
            "查询天气情况",
            "日期查询", "年月日",
            "时间查询",
            "星期",
            "投影仪",
            "客厅的投影仪",
            "动物的叫声", "动物介绍",
            "人物的介绍", "人物身份",
            "任务简介",
            "时分秒",
            "设置闹钟",
            "美人鱼有腿吗"
        };
        for (String title : titleArray)
        {
            suggester.addSentence(title);
            List<String> words = HanLP.extractKeyword (title, 100);
            System.out.printf ("[%s] => %s%n", title, words);
        }
        System.out.println ("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        String[] targets = {
            "猫是怎么叫的", "喵是怎么叫的", "喵是什么", "喵喵", "小狗", "告诉我猫是怎么叫的", "大象的叫声",
            "大象是动物吗", "美人鱼有腿吗",
            "会下雨吗", "今天的天气怎么样",
            "报时",
            "今天几号", "今天是星期几",
            "设置一个二十分钟后的闹钟",
            "无关紧要的农民呢",
            "你是谁",
            "谁",
            "人"
        };

        System.out.println (HanLP.segment ("你是谁"));
        Arrays.stream (targets).forEach (text -> {
            List<String> strings = HanLP.extractKeyword (text, 100);
            System.out.printf ("[%s] -> %s%n", text, strings);
        });
        System.out.println ("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        Arrays.stream (targets).forEach (text -> {
            System.out.printf ("[%s] -> %s%n", text, suggester.suggest (text, 1));
        });
    }
}
