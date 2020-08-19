package tests.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-23 16:52
 **/
public class XmlTests {
    @Test
    public void testXml() throws UnsupportedEncodingException, DocumentException {
        String xml = "<xml>" +
                "<ToUserName><![CDATA[gh_5cb70666666]]></ToUserName>\n" +
                "<FromUserName><![CDATA[xxxxxx4SwieK3NpNJQixxxxxxxxx]]></FromUserName>\n" +
                "<CreateTime>1592902023</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[VIEW]]></Event>\n" +
                "<EventKey><![CDATA[https://www.baidu.com]]></EventKey>\n" +
                "<MenuId>432458626</MenuId>\n" +
                "</xml>" ;
        SAXReader saxReader = new SAXReader();

        Document doc = saxReader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        DefaultXPath ToUserName = new DefaultXPath("/xml/ToUserName");
        DefaultXPath FromUserName = new DefaultXPath("/xml/FromUserName");
        Node nodeToUserName = ToUserName.selectSingleNode(doc);
        Node nodeFromUserName = FromUserName.selectSingleNode(doc);
        System.out.println(nodeToUserName.getStringValue());
        System.out.println(nodeFromUserName.getStringValue());

    }
}
