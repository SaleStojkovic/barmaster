/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import rmaster.models.Porudzbina;
import rmaster.models.StavkaTure;
import rmaster.models.Tura;
import rmaster.views.NumerickaTastaturaController;

/**
 *
 * @author Bosko
 */
public final class Stampac {
    // Singleton instanca
    private static Stampac instance = null;
    
    InputStream input = null;
    File file = null; 

    protected Stampac() {
    }
    
    public static Stampac getInstance() {
        if (instance == null) {
            instance = new Stampac();
        }
        return instance;
    }
    
    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }
    public final void stampajDnevniIzvestajNaFiskal() {
        // Kopira fajl u direktorijum za stampu
        
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(Settings.getInstance().getFiscalniIzvestajiDnevniIzvestajFormatDatuma());

            File fSource = new File(Settings.getInstance().getFiscalniIzvestajiPutanja() + Settings.getInstance().getFiscalniIzvestajiDnevniIzvestaj());
            File fDestination = new File(Settings.getInstance().getFiscalniPrinterPath() + "dnevni " + dateFormat.format(new Date()) + ".xml");
            copyFile(fSource, fDestination);
        } catch(IOException e) {
            
        }


    }

    public final void stampajGotovinskiRacun() {
        
    }
    
    public final void stampajFakturu() {
        
    }

    public final void stampajTuru(Tura tura) {
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(Settings.getInstance().getFiscalniIzvestajiDnevniIzvestajFormatDatuma());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements <Poruzbina>
            Document doc = docBuilder.newDocument();
            Element porudzbina = doc.createElement("Poruzbina");
            doc.appendChild(porudzbina);

            // <Datum>2016/11/05</Datum>
            Element element = doc.createElement("Datum");
            element.appendChild(doc.createTextNode(Utils.getStringDatumFromDate(tura.getVremeTure())));
            porudzbina.appendChild(element);

            // <Vreme>12:57:27</Vreme>
            element = doc.createElement("Vreme");
            element.appendChild(doc.createTextNode(Utils.getStringVremeFromDate(tura.getVremeTure())));
            porudzbina.appendChild(element);

            // <ProdajnoMesto>Restoran CHEZ NIK</ProdajnoMesto>
            element = doc.createElement("ProdajnoMesto");
            element.appendChild(doc.createTextNode(Settings.getInstance().getValueString("prodajnomesto")));
            porudzbina.appendChild(element);

            // <kasa>Kasa 1</kasa>
            element = doc.createElement("kasa");
            element.appendChild(doc.createTextNode(Settings.getInstance().getValueString("kasa.naziv")));
            porudzbina.appendChild(element);

            // <sto>R 32</sto>
            element = doc.createElement("sto");
            element.appendChild(doc.createTextNode(rmaster.RMaster.izabraniStoID));
            porudzbina.appendChild(element);

            // <Stavka>
            //    <kolicina>1.0</kolicina>
            //    <naziv>ICE CAFFE</naziv>
            //    <tip></tip>
            // </Stavka>
            for (StavkaTure stavkaTura : tura.listStavkeTure) {
                Element stavka = doc.createElement("Stavka");
                porudzbina.appendChild(stavka);

                element = doc.createElement("kolicina");
                element.appendChild(doc.createTextNode("" + stavkaTura.getKolicina()));
                stavka.appendChild(element);
                
                element = doc.createElement("naziv");
                element.appendChild(doc.createTextNode("" + stavkaTura.getNaziv()));
                stavka.appendChild(element);

                element = doc.createElement("tip");
                element.appendChild(doc.createTextNode(""));
                stavka.appendChild(element);
                
                for (StavkaTure stavkaTureDodatni : stavkaTura.getArtikliDodatni()) {
                    stavka = doc.createElement("Stavka");
                    porudzbina.appendChild(stavka);

                    element = doc.createElement("kolicina");
                    element.appendChild(doc.createTextNode("" + stavkaTureDodatni.getKolicina()));
                    stavka.appendChild(element);

                    element = doc.createElement("naziv");
                    element.appendChild(doc.createTextNode("" + stavkaTureDodatni.getNaziv()));
                    stavka.appendChild(element);

                    element = doc.createElement("tip");
                    element.appendChild(doc.createTextNode("DODA"));
                    stavka.appendChild(element);
                }
            }

            //  <Konobar>Konobar 1</Konobar>
            element = doc.createElement("Konobar");
            element.appendChild(doc.createTextNode(rmaster.RMaster.ulogovaniKonobar.imeKonobara));
            porudzbina.appendChild(element);
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            doc.setXmlStandalone(true);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(
                    Settings.getInstance().getNefiskalniStampacPutanja() + 
                    "\\" + Settings.getInstance().getPrintTuraStampacNaziv() + 
                    "_" + dateFormat.format(new Date()) + 
                    ".xml")
            );

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }        
/*
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Poruzbina>
    <Datum>2016/11/05</Datum>
    <Vreme>12:57:27</Vreme>
    <ProdajnoMesto>Restoran CHEZ NIK</ProdajnoMesto>
    <kasa>Kasa 1</kasa>
    <sto>R 32</sto>
    <Stavka>
        <kolicina>1.0</kolicina>
        <naziv>ICE CAFFE</naziv>
        <tip></tip>
    </Stavka>
    <Stavka>
        <kolicina>1.0</kolicina>
        <naziv>ESPRESSO</naziv>
        <tip></tip>
    </Stavka>
    <Stavka>
        <kolicina>1.0</kolicina>
        <naziv>Lungo</naziv>
        <tip>DODA</tip>
    </Stavka>
    <Konobar>Konobar 1</Konobar>
</Poruzbina>
        
*/      
    //  Settings.getInstance().getNefiskalniStampacPutanja();
    
    }

    public final void stampajMedjuzbir(Porudzbina porudzbina) {
        boolean odobrenaStampa = true;
        if (porudzbina.getBlokiranaPorudzbina()) {
            odobrenaStampa = false;
            try {
                NumerickaTastaturaController tastatura = new NumerickaTastaturaController(
                        "Unesite menadžersku šifru!", 
                        "Unesite menadžersku šifru!", 
                        false, 
                        null
                );
                Optional<String> result = tastatura.showAndWait();

                if (result.isPresent()){
                    odobrenaStampa = new DBBroker().passwordCheckZaMenadzera(result.get());
                }
            } catch (Exception e){
                System.out.println("Greska pri štampanju međuzbira! - " + e.toString());
                return;
            }
        }
        
        if (odobrenaStampa) {
            // DOTO: Odstampati medjuzbir
            porudzbina.setBlokiranaPorudzbina(true);
        }
    }
}
