package ru.mcs.udk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class UdkSiteParser {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("out.txt"), true, StandardCharsets.UTF_8));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var document = Jsoup.connect("https://www.teacode.com/online/udc/")
                    .timeout(10000)
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .execute().parse();

            Elements one = document.select("table tr[bgcolor=#eaeaea] td:eq(0) a");
            Elements two = document.select("table tr[bgcolor=#eaeaea] td:eq(1)[align=left]");

            if (one.size() == two.size()) {
                for (int i = 0; i < one.size(); i++) {
                    Element udkNumber = one.get(i);
                    Element udkText = two.get(i);
                    System.out.printf("%s\t%s%n", udkNumber.text(), udkText.text());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
