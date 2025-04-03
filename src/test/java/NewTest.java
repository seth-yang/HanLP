import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dictionary.CustomDictionary;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewTest {
    private static final Pattern P = Pattern.compile ("(《.*?》)");
    public static void main (String[] args) throws IOException, ClassNotFoundException {
        HanLP.Config.Normalization = true;
        CustomDictionary.add ("中国制造");
        CustomDictionary.add ("中国制");
        CustomDictionary.add ("新冠");
        CustomDictionary.add ("几点", "tg 1024");
        CustomDictionary.add ("唐家三少");
        CustomDictionary.add ("晓美焰");

        String[] texts = {
            "大象跑了", "长颈鹿正在吃树叶", "妈妈给她的小孩喂牛奶", "今天会下雨吗", "现在几点了",
            "大象是动物吗", "你是谁", "动物园", "小狗怎么叫的", "猫的叫声",
            "一加一等于几", "帮我计算一下五乘以十二", "现在播放的是什么", "上一首",
            "暂停",
            "睡觉", "退出", "退下",
            "定一个二十分钟后的闹钟", "二十分钟的闹钟",
            "帮我订一张从北京到上海的机票",
            "我想订一张从北京到上海的机票",
            "我要订一张从北京到上海的机票",
            "回家", "导航回家",
            "定位我现在的位置",
            "2013年亚洲冠军联赛恒广州恒大比赛时间",
            "帮我查一下赣州到厦门的汽车",
            "导航到望江西路",
            "把张玉娟的手机号码发送给吴伟",
            "打电话给钟跃民",
            "经xXx的电话号码发给Lc发信息给盛吉",
            "将你在哪发送给纲吉",
            "发信息给老妈说我在吃饭",
            "我要听稻香",
            "访问浏览器",
            "中国制用英文怎么说",
            "发送报文到服务器",
            "简单介绍一下蝙蝠",
            "非典和新冠都是蝙蝠引起的吗",
            "晓美焰来到北京立方庭参观自然语义科技公司。",
            "晓美焰参观位于北京立方庭的自然语义科技公司。",
            "去动物园怎么走",
            "搜索唐家三少的小说",
            "播放周杰伦的歌",
            "黄英的山歌很好听",
            "司马相如和卓文君的故事",
            "宋祖英演唱了一首《走进新时代》",
            "这部电影叫《复仇者联盟》",
            "陈独秀和李大钊创办了《新青年》",
            "鲁迅的《呐喊》是什么",
            "老板，来一块两块一块的豆腐块",
            "这批工单1964", "查一下测试的 T3 AD值"
        };

//        IDependencyParser parser = new KBeamArcEagerDependencyParser ();
        for (String text : texts) {
            Matcher m = P.matcher (text);
            if (m.find ()) {
                CustomDictionary.add (m.group (1));
            }
            CoNLLSentence sentence = HanLP.parseDependency (text);
//            CoNLLSentence sentence = parser.parse (text);
            Map<Integer, CoNLLWord> map = new HashMap<> ();
            // 核心词
            CoNLLWord core = null;
            System.out.println (text);
            System.out.println ("-----------------------------------------------");
            for (CoNLLWord word : sentence) {
                System.out.printf ("%s[%s]--(%s)--%s%n", word.LEMMA, word.POSTAG, word.DEPREL, word.HEAD.LEMMA);
                map.put (word.ID, word);
                if (word.HEAD.ID == 0) {
                    core = word;
                }
            }
            if (core != null) {
                CoNLLWord[] result = new CoNLLWord[] {null, core, null};
                for (CoNLLWord word : map.values ()) {
                    if (word.HEAD == core) { // 当前词汇和核心词有联系
                        if ("主谓关系".equals (word.DEPREL)) {
                            result[0] = word;
                        } else if ("动宾关系".equals (word.DEPREL)) {
                            result[2] = word;
                        } else if ("定中关系".equals (word.DEPREL)) {
                            result[0] = word;
                        } else if ("状中结构".equals (word.DEPREL) && result[0] == null) {
                            result[0] = word;
                        }
                    } else if ("并列关系".equals (word.DEPREL) && word.HEAD == result[0]) {
                        result[0] = word;
                    }
                }
                System.out.printf ("[%s] -> ", text);
                for (CoNLLWord world : result) {
                    String part = null;
                    if (world != null) {
                        if ("并列关系".equals (world.DEPREL)) {
                            part = world.HEAD.LEMMA + '、' + world.LEMMA;
                        } else {
                            part = world.LEMMA;
                        }
                    }
                    System.out.printf ("<%s>", part);
                }
                System.out.println ();
            }
            System.out.println ("-----------------------------------------------");
            System.out.println ();
        }
    }

    private static Map<Integer, CoNLLWord> map (CoNLLSentence sentence) {
        Map<Integer, CoNLLWord> map = new HashMap<> ();
        for (CoNLLWord word : sentence) {
            map.put (word.ID, word);
        }
        return map;
    }

    private static CoNLLWord findCore (CoNLLSentence sentence) {
        for (CoNLLWord word : sentence) {
            if (word.HEAD.ID == 0) {
                return word;
            }
        }
        return null;
    }

    private static final class Wrapper {
        Map<Integer, CoNLLWord> map;
        CoNLLWord core;
        String text;

        public Wrapper (Map<Integer, CoNLLWord> map, CoNLLWord core, String text) {
            this.map = map;
            this.core = core;
            this.text = text;
        }
    }
}