package cz.policie.patrani;

import cz.policie.patrani.model.HledanaOsoba;
import cz.policie.patrani.model.Osoba;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Parsuje webové stránky policie české republiky <a href="http://aplikace.policie.cz/patrani-osoby/Vyhledavani.aspx">pátrání po osobách</a>
 */
public class OsobyScraper {

    // Adresa webového formuláře
    private static final String URL = "http://aplikace.policie.cz/patrani-osoby/Vyhledavani.aspx";

    /**
     * Z předané URL parsuje detail osoby do struktury {@link HledanaOsoba}.
     *
     * @param url odkaz na webovou stránku s detail o hledané osobě
     * @return {@link HledanaOsoba}
     * @throws IOException chyba parsování
     */
    private static HledanaOsoba detail(String url) throws IOException {

        Document detail = Jsoup.connect(url).get();
        Elements table = detail.select("table[id=ctl00_ctl00_Application_BasePlaceHolder_TablePatros]");
        Element tr = table.select("tr").first();
        Iterator<Element> spans = tr.select("td span").iterator();
        Map<String, String> map = new LinkedHashMap<>();
        while (spans.hasNext()) {
            Element next = spans.next();
            map.put(next.attr("id"), next.text());
        }
        return HledanaOsoba.parse(map);

    }

    /**
     * Seznam hledaných osob odpovídajících předanému formuláři.
     *
     * @param osoba filtry hledané osoby
     * @return seznam hledaných osob odpovídajících předanému filtru
     * @throws IOException chyba parsování
     */
    static List<HledanaOsoba> parse(Osoba osoba) throws IOException {

        Document galery = PatraniSoup
                .connect(URL)
                .method(Connection.Method.POST)
                .data("ctl00$ctl00$Application$BasePlaceHolder$ddlPohlavi", osoba.getPohlavi().getValue())
                .data("ctl00$ctl00$Application$BasePlaceHolder$ddlHledanyPohresovany", osoba.getHledanyPohresovany().getValue())
                .data("ctl00$ctl00$Application$BasePlaceHolder$txtJmenoPrijmeni", osoba.getJmenoPrijmeni())
                .data("ctl00$ctl00$Application$BasePlaceHolder$ddlBydliste", osoba.getBydliste())
                .data("ctl00$ctl00$Application$BasePlaceHolder$ddlObcanstvi", osoba.getObcanstvi())
                .data("ctl00$ctl00$Application$BasePlaceHolder$txtVekOd", osoba.getVekOd())
                .data("ctl00$ctl00$Application$BasePlaceHolder$txtVekDo", osoba.getVekDo())
                .data("ctl00$ctl00$Application$BasePlaceHolder$txtVyskaOd", osoba.getVyskaOd())
                .data("ctl00$ctl00$Application$BasePlaceHolder$txtVyskaDo", osoba.getVyskaDo())
                .data("ctl00$ctl00$Application$BasePlaceHolder$ddlBarvaOci", osoba.getBarvaOci().getValue())
                .data("ctl00$ctl00$Application$BasePlaceHolder$ddlBarvaVlasu", osoba.getBarvaVlasu().getValue())
                .data("ctl00$ctl00$Application$BasePlaceHolder$btnVyhledat", "Vyhledat")
                .followRedirects(true) // Hooodně důležitej parametr
                .post();

        Elements label1 = galery.select("div[id=gallery]:contains(Nenalezen)");
        if (!label1.isEmpty()) {
            throw new PatraniNotFoundException(label1);
        }

        Iterator<Element> boxs = galery.select("div[class=personBox]").iterator();
        List<HledanaOsoba> result = new ArrayList<>();
        while (boxs.hasNext()) {
            Element next = boxs.next();
            Element link = next.select("a").first();
            HledanaOsoba hledanaOsoba = detail(link.attr("abs:href"));

            result.add(hledanaOsoba);
        }
        return result;

    }

}
