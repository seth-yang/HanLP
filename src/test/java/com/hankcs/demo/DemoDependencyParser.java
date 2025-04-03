/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/7 20:14</create-date>
 *
 * <copyright file="DemoPosTagging.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dependency.IDependencyParser;
import com.hankcs.hanlp.dependency.perceptron.parser.KBeamArcEagerDependencyParser;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.utility.TestUtility;

import java.io.IOException;

/**
 * 依存句法分析（神经网络句法模型需要-Xms1g -Xmx1g -Xmn512m）
 *
 * @author hankcs
 */
public class DemoDependencyParser extends TestUtility
{
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
//        CoNLLSentence sentence = HanLP.parseDependency("徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。");
//        String raw = "徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。";
        long s = System.currentTimeMillis ();
        CustomDictionary.add ("几点");
        System.err.printf ("init takes %d ms.%n", System.currentTimeMillis () - s);
//        String raw = "现代汉语的各类结构中，定中结构是一种非常复杂的语言形式：它与词类的关系错综复杂，造成了它可能与主谓结构、状中结构内部同形。";
//        CoNLLSentence sentence = HanLP.parseDependency(raw);
//        System.out.printf ("raw: %s%n", raw);
        // 也可以用基于ArcEager转移系统的依存句法分析器
        s = System.currentTimeMillis ();
        IDependencyParser parser = new KBeamArcEagerDependencyParser();
        System.err.printf ("create paser takes %d ms.%n", System.currentTimeMillis () - s);
        for (int j = 0; j < 5; j ++) {
            long start = System.currentTimeMillis ();
            CoNLLSentence sentence = parser.parse ("徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。");
            long end = System.currentTimeMillis ();
            System.err.printf ("[%d] - parse sentence takes %d ms.%n", j, end - start);
            System.out.println (sentence);
            // 可以方便地遍历它
            for (CoNLLWord word : sentence) {
                System.out.printf ("%s[%s] --(%s)--> %s[%s]\n", word.LEMMA, word.CPOSTAG, word.DEPREL, word.HEAD.LEMMA, word.HEAD.CPOSTAG);
            }

            System.out.println ("-------------------- result 1 ----------------------");
            // 也可以直接拿到数组，任意顺序或逆序遍历
            CoNLLWord[] wordArray = sentence.getWordArray ();
            for (int i = wordArray.length - 1; i >= 0; i--) {
                CoNLLWord word = wordArray[i];
                System.out.printf ("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
            }

            System.out.println ("-------------------- result 2 ----------------------");
            // 还可以直接遍历子树，从某棵子树的某个节点一路遍历到虚根
            CoNLLWord head = wordArray[wordArray.length - 1];
            while ((head = head.HEAD) != null) {
                if (head == CoNLLWord.ROOT) System.out.println (head.LEMMA);
                else System.out.printf ("%s --(%s)--> ", head.LEMMA, head.DEPREL);
            }
        }
    }
}
