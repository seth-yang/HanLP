import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IdiomStory {
    public static void main (String[] args) {
        String text = "　　杯弓蛇影\n" +
            "　　这个成语来源于东汉.应劭《风俗通义》，时北壁上有悬赤弩，照于杯，形如蛇。宣畏恶之，然不敢不饮。\n" +
            "　　有一年夏天，县令应郴请主簿(办理文书事务的官员)杜宣来饮酒。酒席设在厅堂里，北墙上悬挂着一张红色的弓。由于光线折射，酒杯中映入了弓的影子。杜宣看了，以为是一条蛇在酒杯中蠕动，顿时冷汗涔涔。但县令是他的上司，又是特地请他来饮酒的，不敢不饮，所以硬着头皮喝了几口。仆人再斟时，他借故推却，起身告辞走了。回到家里，杜宣越来越疑心刚才饮下的是有蛇的酒，又感到随酒入口的蛇在肚中蠕动，觉得胸腹部疼痛异常，难以忍受，吃饭、喝水都非常困难。\n" +
            "　　家里人赶紧请大夫来诊治。但他服了许多药，病情还是不见好转。\n" +
            "　　过了几天，应郴有事到杜宣家中，问他怎么会闹病的，杜宣便讲了那天饮酒时酒杯中有蛇的事。应郴安慰他几句，就回家了。他坐在厅堂里反复回忆和思考，弄不明白杜宣酒杯里怎么会有蛇的。\n" +
            "　　突然，北墙上的那张红色的弓引起了他的注意。他立即坐在那天杜宣坐的位置上，取来一杯酒，也放在原来的位置上。结果发现，酒杯中有弓的影子，不细细观看，确实像是一条蛇在蠕动。应郴马上命人用马车把杜宣接来，让他坐在原位上，叫他仔细观看酒杯里的影子，并说：“你说的杯中的蛇，不过是墙上那张弓的倒影罢了，没有其他什么怪东西。现在你可以放心了!”\n" +
            "　　杜宣弄清原委后，疑虑立即消失，病也很快痊愈了。";
//        System.out.println (text);
        char[] raw = text.toCharArray ();
        StringBuilder temp = new StringBuilder ();
        for (char ch : raw) {
            if (ch != ' ' && ch != '　') {
                temp.append (ch);
            }
        }
        text = temp.toString ();
        raw = text.toCharArray ();
        List<Pinyin> pinyinList = HanLP.convertToPinyinList(text);
        System.out.printf ("raw.length = %d, pinyin.length = %d%n", raw.length, pinyinList.size ());
        System.out.println ();

        List<Wrapper> wrappers = new ArrayList<> ();

        int start = 0;
        for (int i = 0, n = raw.length; i < n; i ++) {
            char ch = raw[i];
            if (ch == '\n') {
                Wrapper w = new Wrapper ();
                w.chinese = new char[i - start];
                System.arraycopy (raw, start, w.chinese, 0, i - start);
                List<Pinyin> sub = pinyinList.subList (start, i);
                w.pinyin = sub.stream ().map (p -> {
                    String mark = p.getPinyinWithToneMark ();
                    if ("none".equals (mark)) {
                        return " ";
                    } else {
                        return mark;
                    }
                }).collect(Collectors.toList());
                wrappers.add (w);
                start = i + 1;
            }
        }

        final int COLUMNS = 80;
        wrappers.forEach (w -> {
            StringBuilder pinyin = new StringBuilder ();
            StringBuilder chinese = new StringBuilder ();
            int column = 0, columnB = 0;
            for (int i = 0, n = w.chinese.length; i < n; i ++) {
                String word = w.pinyin.get (i);
                column = pinyin.length ();
                if (column + word.length () > COLUMNS) {
                    if (" ".equals (word)) {
                        // 标点符号，不应该换行
                        chinese.append (w.chinese[i]);
                    }
                    System.out.println (pinyin);
                    System.out.println (chinese);
                    System.out.println ();
                    pinyin.setLength (0);
                    chinese.setLength (0);
                    columnB = 0;
                    if (" ".equals (word)) {
                        continue;
                    }
                }
                if (pinyin.length () > 0) {
                    pinyin.append (' ');
                }
                column = pinyin.length ();
                if (columnB > 0) {
                    int m = column - columnB;
                    for (int j = 0; j < m; j ++) {
                        chinese.append (' ');
                    }
                    columnB += m;
                }


                pinyin.append (word);
                char ch = w.chinese[i];
                chinese.append (ch);
                columnB += ch < 128 ? 1 : 2; // 一个汉字2个字符
            }
            if (pinyin.length () > 0) {
                System.out.println (pinyin);
                System.out.println (chinese);
            }
            System.out.println ();
        });
    }

    private static final class Wrapper {
        List<String> pinyin = new ArrayList<> ();
        char[] chinese;
    }
}
