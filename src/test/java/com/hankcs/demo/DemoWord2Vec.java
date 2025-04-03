/*
 * <author>Hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2017-11-02 12:09</create-date>
 *
 * <copyright file="Demo.java" company="码农场">
 * Copyright (c) 2017, 码农场. All Right Reserved, http://www.hankcs.com/
 * This source is subject to Hankcs. Please contact Hankcs to get more information.
 * </copyright>
 */
package com.hankcs.demo;

import com.hankcs.hanlp.corpus.MSR;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.tokenizer.BasicTokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 演示词向量的训练与应用
 *
 * @author hankcs
 */
public class DemoWord2Vec
{
    private static final String TRAIN_FILE_NAME = MSR.TRAIN_PATH;
    private static final String MODEL_FILE_NAME = "data/test/word2vec.txt";

    public static void main(String[] args) throws IOException
    {
        System.out.printf ("[%s] loading module ...%n", sdf.format (System.currentTimeMillis ()));
        WordVectorModel model = trainOrLoadModel();
        System.out.printf ("[%s] module loaded.%n", sdf.format (System.currentTimeMillis ()));
        // 文档向量
        DocVectorModel docVectorModel = new DocVectorModel(model);
/*
        String[] documents = new String[]{
*/
/*
            "山东苹果丰收",
            "农民在江苏种水稻",
            "奥运会女排夺冠",
            "世界锦标赛胜出",
            "中国足球失败",
*//*

            "客厅的窗帘",
            "天气预报",
            "气象情况",
            "查询天气情况",
            "日期查询", "年月日",
            "时间查询",
            "星期",
            "投影仪",
            "客厅的投影仪",
            "动物的叫声", "动物",
            "人物的介绍", "人物身份",
            "任务简介",
            "时分秒",
            "设置闹钟"
        };
        for (int i = 0; i < documents.length; i++)
        {
            docVectorModel.addDocument(i, documents[i]);
        }
*/
        List<String> documents = new ArrayList<> (Arrays.asList ("客厅的窗帘",
            "天气预报",
            "气象情况",
            "查询天气情况",
            "日期查询", "年月日",
            "时间查询",
            "星期",
            "投影仪",
            "客厅的投影仪",
            "动物的叫声", "动物",
            "人物的介绍", "人物身份",
            "任务简介",
            "时分秒",
            "设置闹钟")
        );
        for (int i = 0; i < documents.size (); i ++) {
            docVectorModel.addDocument (i, documents.get (i));
        }

        String[] targets = {
            "猫是怎么叫的", "喵是怎么叫的", "喵是什么", "喵喵", "小狗",
            "会下雨吗", "今天的天气怎么样",
            "报时",
            "今天几号", "今天是星期几",
            "设置一个二十分钟后的闹钟",
            "无关紧要的农民呢",
            "你是谁"
        };


        Arrays.stream (targets).forEach (text -> {
            List<Map.Entry<Integer, Float>> map = docVectorModel.nearest (text);
            System.out.printf ("[%20s] -> %n", text);
            System.out.println ("-------------------------------------------------------------------------------");
            for (Map.Entry<Integer, Float> e : map) {
                System.out.printf (" %20s -> %f%n", documents.get(e.getKey ()), e.getValue ());
            }
            System.out.println ();
        });

        BufferedReader reader = new BufferedReader (new InputStreamReader (System.in));
        String line;
        System.out.print ("Input> ");
        while ((line = reader.readLine ()) != null) {
            if ("quit".equals (line)) {
                System.out.println ("bye!");
                break;
            }
            if ("show:dict".equals (line)) {
                for (String document : documents) {
                    System.out.println (document);
                }
                System.out.print ("Input> ");
                continue;
            } else if (line.startsWith ("dict:")) {
                int pos = line.indexOf (":");
                String doc = line.substring (pos + 1);
                docVectorModel.addDocument (documents.size (), doc);
                documents.add (doc);
                System.out.printf ("doc::[%s] add.%n", doc);
                System.out.print ("Input> ");
                continue;
            }

            List<Map.Entry<Integer, Float>> map = docVectorModel.nearest (line);
            System.out.printf ("[%20s] -> %n", line);
            System.out.println ("-------------------------------------------------------------------------------");
            for (Map.Entry<Integer, Float> e : map) {
                System.out.printf (" %20s -> %f%n", documents.get (e.getKey ()), e.getValue ());
            }
            System.out.println ();
            System.out.print ("Input> ");
        }
    }

    static void printNearest(String word, WordVectorModel model)
    {
        System.out.printf("\n                                                Word     Cosine\n------------------------------------------------------------------------\n");
        for (Map.Entry<String, Float> entry : model.nearest(word))
        {
            System.out.printf("%50s\t\t%f\n", entry.getKey(), entry.getValue());
        }
    }

    static void printNearestDocument(String document, String[] documents, DocVectorModel model)
    {
        printHeader(document);
        for (Map.Entry<Integer, Float> entry : model.nearest(document))
        {
            System.out.printf("%50s\t\t%f\n", documents[entry.getKey()], entry.getValue());
        }
    }

    private static void printHeader(String query)
    {
        System.out.printf("\n%50s          Cosine\n------------------------------------------------------------------------\n", query);
    }

    static WordVectorModel trainOrLoadModel() throws IOException
    {
/*
        if (!IOUtil.isFileExisted(MODEL_FILE_NAME))
        {
            if (!IOUtil.isFileExisted(TRAIN_FILE_NAME))
            {
                System.err.println("语料不存在，请阅读文档了解语料获取与格式：https://github.com/hankcs/HanLP/wiki/word2vec");
                System.exit(1);
            }
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME);
        }
*/

        return loadModel();
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");

    static WordVectorModel loadModel() throws IOException
    {
//        System.err.printf ("[%s] loading module ...%n", sdf.format (System.currentTimeMillis ()));
//        return new WordVectorModel(MODEL_FILE_NAME);
        return new WordVectorModel ("E:/downloads/HanLP/cc.zh.300.vec");
    }
}
