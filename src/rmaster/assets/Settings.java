/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Bosko
 */
public class Settings {
    // Singleton instance
    private static Settings instance = null;
    
    String putanjaSettings;
    
    Properties prop = new Properties();   
    
    protected Settings() { 
        this.putanjaSettings = "C:\\BarMaster\\Settings.conf";
        
        InputStream input = null;

	try {
                input = new FileInputStream(new File(putanjaSettings));
		// load a properties file
		prop.load(input);
                
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    }
    
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getValueString(String key) {
        try {
            return prop.getProperty(key);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean getValueBoolean(String key) {
        try {
            return Boolean.parseBoolean(prop.getProperty(key));
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getFiscalniPrinterPath() {
        // u Settings.conf stoji =C\:\\BarMaster\\fiscal\\
        return prop.getProperty("rmi.putanja");
    }

    public String getFiscalniPrinter() {
        // u Settings.conf stoji =1
        return prop.getProperty("fiskalni.printer");
    }

    public String getFiscalniIzvestajiPutanja() {
        // u Settings.conf stoji =C\:\\BarMaster\\izvestaji\\
        return prop.getProperty("fiscal.izvestaji.putanja");
    }

    public String getFiscalniIzvestajiDnevniIzvestaj() {
        // u Settings.conf stoji =dnevni_izvestaj.xml
        return prop.getProperty("fiscal.izvestaji.dnevniizvestaj");
    }
    
    public String getFiscalniIzvestajiDnevniIzvestajFormatDatuma() {
        // u Settings.conf stoji =yyyy-MM-dd_HH-mm-ss-S
        return prop.getProperty("fiscal.izvestaji.dnevniizvestaj.formatdatuma");
    }

    public String getPrintTuraFormatVremana() {
        // u Settings.conf stoji =HH:mm:ss
        return prop.getProperty("print.tura.formatvremena");
    }

    public String getPrintTuraFormatDatuma() {
        // u Settings.conf stoji =yyyy/MM/dd
        return prop.getProperty("print.tura.formatdatuma");
    }
    public String getPrintTuraStampacNaziv() {
        // u Settings.conf stoji =yyyy/MM/dd
        return prop.getProperty("print.tura.stampac.naziv");
    }

    public String getFormatDatuma() {
        // u Settings.conf stoji =yyyy-MM-dd HH-mm-ss
        return prop.getProperty("formatdatuma");
    }
    public String getNefiskalniStampacPutanja() {
        // u Settings.conf - putanja gde nefiskalni stampac trazi fajl za stampu
        return prop.getProperty("nefiscal.stampa.izvestaji.putanja");
    }
}
