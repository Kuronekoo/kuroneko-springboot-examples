//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.kuroneko.demos.emoji.emoji;

import com.github.binarywang.java.emoji.model.Emoji4Unicode;
import com.github.binarywang.java.emoji.model.Emoji4Unicode.Category;
import com.github.binarywang.java.emoji.model.Emoji4Unicode.Element;
import com.github.binarywang.java.emoji.model.Emoji4Unicode.SubCategory;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EmojiReader {
    private static final Logger logger = LoggerFactory.getLogger(EmojiReader.class);
    private static final String url = "https://raw.githubusercontent.com/googlei18n/emoji4unicode/master/data/emoji4unicode.xml";
    private static final String TRIM_PATTERN = "[^0-9A-F]*";
    private static Emoji4Unicode emoji = null;
    private static Map<List<Integer>, String> sb2UnicodeMap = null;

    public EmojiReader() {
    }

    public static Emoji4Unicode read() {
        if (emoji != null) {
            return emoji;
        } else {
            long beginTime = System.currentTimeMillis();
            XStream xstream = new XStream(new DomDriver());
            xstream.processAnnotations(Emoji4Unicode.class);
//取消从github读取xml，速度太慢了，而且是先去git再读本地，坑爹啊
//            try {
//                emoji = (Emoji4Unicode)xstream.fromXML(new URL("https://raw.githubusercontent.com/googlei18n/emoji4unicode/master/data/emoji4unicode.xml"));
//                logger.info("==============from url==============");
//            } catch (Exception var4) {
//                var4.printStackTrace();
//            }

            if (emoji == null) {
                logger.info("==============from local file==============");
                return (Emoji4Unicode)xstream.fromXML(EmojiReader.class.getResourceAsStream("/emoji4unicode.xml"));
            } else {
                String interval = (new Interval(beginTime, System.currentTimeMillis())).toPeriod().toString().replace("PT", "").replace("M", "分").replace("S", "秒");
                logger.info("耗时： " + interval);
                return emoji;
            }
        }
    }

    public static Map<List<Integer>, String> readSb2UnicodeMap() {
        if (sb2UnicodeMap != null) {
            return sb2UnicodeMap;
        } else {
            sb2UnicodeMap = Maps.newHashMap();
            Emoji4Unicode emoji4Unicode = read();
            Iterator var1 = emoji4Unicode.getCategories().iterator();

            label82:
            while(true) {
                List subCategories;
                do {
                    if (!var1.hasNext()) {
                        return sb2UnicodeMap;
                    }

                    Category category = (Category)var1.next();
                    subCategories = category.getSubCategories();
                } while(subCategories == null);

                Iterator var4 = subCategories.iterator();

                label80:
                while(true) {
                    List elements;
                    do {
                        if (!var4.hasNext()) {
                            continue label82;
                        }

                        SubCategory subCategory = (SubCategory)var4.next();
                        elements = subCategory.getElements();
                    } while(elements == null);

                    Iterator var7 = elements.iterator();

                    while(true) {
                        while(true) {
                            Element element;
                            ArrayList fromCodePoints;
                            String fromValue;
                            do {
                                if (!var7.hasNext()) {
                                    continue label80;
                                }

                                element = (Element)var7.next();
                                fromCodePoints = new ArrayList();
                                fromValue = element.getSoftbank();
                            } while(fromValue == null);

                            if (fromValue.length() > 6) {
                                String[] froms = fromValue.split("\\+");
                                String[] var12 = froms;
                                int var13 = froms.length;

                                for(int var14 = 0; var14 < var13; ++var14) {
                                    String part = var12[var14];
                                    fromCodePoints.add(Integer.parseInt(part.replaceAll("[^0-9A-F]*", ""), 16));
                                }
                            } else {
                                fromCodePoints.add(Integer.parseInt(fromValue.replaceAll("[^0-9A-F]*", ""), 16));
                            }

                            String toValue = element.getUnicode();
                            if (toValue == null) {
                                sb2UnicodeMap.put(fromCodePoints, null);
                            } else {
                                StringBuilder toBuilder = new StringBuilder();
                                if (toValue.length() > 6) {
                                    String[] tos = toValue.split("\\+");
                                    String[] var21 = tos;
                                    int var22 = tos.length;

                                    for(int var16 = 0; var16 < var22; ++var16) {
                                        String part = var21[var16];
                                        toBuilder.append(Character.toChars(Integer.parseInt(part.replaceAll("[^0-9A-F]*", ""), 16)));
                                    }
                                } else {
                                    toBuilder.append(Character.toChars(Integer.parseInt(toValue.replaceAll("[^0-9A-F]*", ""), 16)));
                                }

                                sb2UnicodeMap.put(fromCodePoints, toBuilder.toString());
                            }
                        }
                    }
                }
            }
        }
    }
}
