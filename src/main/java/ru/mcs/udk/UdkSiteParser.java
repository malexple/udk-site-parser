package ru.mcs.udk;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class UdkSiteParser {

    public static final String HTTPS_WWW_TEACODE_COM_ONLINE_UDC = "https://www.teacode.com/online/udc";

    public static void main(String[] args) throws IOException {
        System.setOut(new PrintStream(new FileOutputStream("udk.txt"), true, StandardCharsets.UTF_8));

        getHierarchy(HTTPS_WWW_TEACODE_COM_ONLINE_UDC, 0, 3);
    }

    public static void getHierarchy(String url, int level, int stopLevel) {
        try {
            var document = Jsoup.connect(url)
                    .timeout(5000)
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .execute().parse();

            Elements rows = document.select("table tr[bgcolor=#eaeaea]");

            // тут создаем новую переменную которую будем передавать в метод
            int nextLevel = level + 1;
            for (Element row : rows) {
                Elements udkNumber = row.select("td:eq(0)");
                Elements udkTitle = row.select("td:eq(1)[align=left]");
                System.out.printf("%s%s\t%s%n", "\t".repeat(Math.max(0, level)), udkNumber.text(), udkTitle.text());

                String pageUrl = udkNumber.select("a").attr("href");
                if (!pageUrl.isEmpty() && level < stopLevel - 1) {
                    String subUrl = getUrl(url, pageUrl);

                    getHierarchy(subUrl, nextLevel, stopLevel);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static String getUrl(String url, String subUrl) {
        if (subUrl.startsWith(".")) {
            return String.format("%s%s", HTTPS_WWW_TEACODE_COM_ONLINE_UDC, subUrl.replaceFirst(".", ""));
        } else {
            return String.format("%s%s", url.replaceAll("/[^/]+\\.html$", "/"), subUrl);
        }
    }
}
