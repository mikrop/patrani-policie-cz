package cz.policie.patrani.model;

import cz.policie.patrani.ScraperUtils;
import org.junit.Assert;

import java.util.Date;
import java.util.Map;

/**
 * Údaje o pohřešované a hledané osoby, po které Policie České republiky vyhlásila pátrání.
 */
public class HledanaOsoba {

    private String prijmeniJmeno;
    private Pohlavi pohlavi = Pohlavi.MUZ;
    private Date datumNarozeni;
    private boolean nebezpecny;
    private boolean ozbrojeny;
    private boolean nakazlivy;
    private String narodnost;
    private String vyska;
    private String vek;
    private String popis;
    private String dalsiUdaje;

    public static HledanaOsoba parse(Map<String, String> map) {
        Assert.assertNotNull(map);

        HledanaOsoba osoba = new HledanaOsoba();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue().isEmpty() ? null : entry.getValue();
            if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_fullName")) {
                osoba.prijmeniJmeno = value;
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_pohlavi")) {
                osoba.pohlavi = ScraperUtils.toPohlavi(value);
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_datumNarozeni")) {

                if (value != null) {
                    String replace = value.replace("Datum narození ", "");
                    osoba.datumNarozeni = ScraperUtils.parse(replace);
                }

            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_Dangerous")) {
                osoba.nebezpecny = ScraperUtils.toBoolean(value);
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_Armed")) {
                osoba.ozbrojeny = ScraperUtils.toBoolean(value);
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_Nemoc")) {
                osoba.nakazlivy = ScraperUtils.toBoolean(value);
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_Nationality")) {
                osoba.narodnost = value;
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_Vyska")) {
                osoba.vyska = value;
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_Age")) {
                osoba.vek = value;
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lblDescription")) {
                osoba.popis = value;
            } else if (entry.getKey().equals("ctl00_ctl00_Application_BasePlaceHolder_lbl_speechLang")) {
                osoba.dalsiUdaje = value;
            }
        }
        return osoba;
    }

    public String getPrijmeniJmeno() {
        return prijmeniJmeno;
    }

    public Pohlavi getPohlavi() {
        return pohlavi;
    }

    public Date getDatumNarozeni() {
        return datumNarozeni;
    }

    public boolean isNebezpecny() {
        return nebezpecny;
    }

    public boolean isOzbrojeny() {
        return ozbrojeny;
    }

    public boolean isNakazlivy() {
        return nakazlivy;
    }

    public String getNarodnost() {
        return narodnost;
    }

    public String getVyska() {
        return vyska;
    }

    public String getVek() {
        return vek;
    }

    public String getPopis() {
        return popis;
    }

    public String getDalsiUdaje() {
        return dalsiUdaje;
    }

}
