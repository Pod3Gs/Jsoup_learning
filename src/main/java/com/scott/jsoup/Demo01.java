package com.scott.jsoup;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo01 {
  public static void main(String[] args) throws IOException {
    // 利用httpclient爬数据
    String url = "http://www.hao123.com";
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(url);
    CloseableHttpResponse response = httpClient.execute(httpGet);
    HttpEntity entity = response.getEntity();
    String content = EntityUtils.toString(entity, "utf-8");
    response.close();

    // 利用Joup处理收到的数据
    Document doc = Jsoup.parse(content);
    Elements elements = doc.getElementsByTag("a");
    List<String> strings = new ArrayList<String>();
    for (Element element : elements) {
      String hrefGotten = element.toString();
      strings.add(getAHref(hrefGotten));
    }
    System.out.println(strings);

    // 将所有的超链接输出
    String filePath = Demo01.class.getClassLoader().getResource("file.text").getPath();
    //String filePath = "src/main/resources/file.txt";
    FileWriter clearFile = new FileWriter(filePath);
    clearFile.write("");
    clearFile.close();
    FileWriter fw = new FileWriter(filePath, true);
    fw.write("-------------------来自" + url + "的超链接---------------" + "\n");
    for (String string : strings) {
      String a = "\"#\"";
      String b = "";
      String c = "\"javascript:;\""; // "javascript:;"

      if (!string.equals(a) && !string.equals(b) && !string.equals(c)) {
        fw.write(string + "\n");
      }
    }
    fw.close();
  }

  private static String getAHref(String strs) {
    String result = "";
    String regex = "<a.*?/a>";

    Pattern pt = Pattern.compile(regex);
    Matcher mt = pt.matcher(strs);

    while (mt.find()) {
      String s3 = "href=\"(.*?)\"";
      Pattern pt3 = Pattern.compile(s3);
      Matcher mt3 = pt3.matcher(mt.group());
      while (mt3.find()) {
        result = mt3.group().replaceAll("href=|>", "");
      }
    }
    return result;
  }
}
