/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import rmaster.assets.QueryBuilder.QueryBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import rmaster.models.NacinPlacanja;
import rmaster.models.Porudzbina;
import rmaster.models.Stampac;
import rmaster.models.StavkaTure;
import rmaster.models.Tura;
import rmaster.views.NumerickaTastaturaController;

/**
 *
 * @author Bosko
 */
public final class Stampa {
    // Singleton instanca
    private static Stampa instance = null;
//    List<Map<String, String>> listaStampaca = new ArrayList<>();
    List<Stampac> listaStampaca = new ArrayList<>();
    InputStream input = null;
    File file = null; 

    protected Stampa() {
    }
    
    public static Stampa getInstance() {
        if (instance == null) {
            instance = new Stampa();
            instance.pokupiStampace();
        }
        return instance;
    }
    
    public void pokupiStampace() {
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName(Stampac.TABLE_NAME);
        query.setSelectColumns(Stampac.PRIMARY_KEY, Stampac.NAZIV, Stampac.BROJ_KOPIJA_ZBIRNE, Stampac.KODNA_STRANA, Stampac.STAMPA_IZVESTAJE, Stampac.STAMPA_ZBIRNU, Stampac.TIP);
        //query.addCriteriaColumns(StalniGost.SIFRA, StalniGost.BLOKIRAN, StalniGost.GRUPA_ID);
        //query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        //query.addOperators(QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND);
        //query.addCriteriaValues("", QueryBuilder.TRUE, grupaId);
        DBBroker db = new DBBroker();
        List<Map<String, String>> listaRezultata = db.runQuery(query);
          
        for (Map mapStampac : listaRezultata) {
            Stampac noviStampac = new Stampac();
            noviStampac.makeFromHashMap((HashMap)mapStampac);
            
//            listaStampaca.add(noviStampac.makeMapForTableOutput());
            listaStampaca.add(noviStampac);
        }
    }
    
    public Stampac getStampacByID(String stampacID) {
        for (Stampac stampac : listaStampaca) {
            if (stampac.id.equals(stampacID))
                return stampac;
        }
        return null;
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
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(Settings.getInstance().getFiscalniIzvestajiDnevniIzvestajFormatDatuma());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements <Poruzbina>
            Document doc = docBuilder.newDocument();
            Element element = doc.createElement("Izvestaj");
            element.appendChild(doc.createTextNode("2"));
            doc.appendChild(element);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                                        new File(
                                                Settings.getInstance().getFiscalniPrinterPath() + 
                                                "dnevni " + 
                                                dateFormat.format(new Date()) + 
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
    }

    public final void stampajPresekNaFiskal() {
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(Settings.getInstance().getFiscalniIzvestajiDnevniIzvestajFormatDatuma());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements <Poruzbina>
            Document doc = docBuilder.newDocument();
            Element element = doc.createElement("Izvestaj");
            element.appendChild(doc.createTextNode("1"));
            doc.appendChild(element);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                                        new File(
                                                Settings.getInstance().getFiscalniPrinterPath() + 
                                                "presek " + 
                                                dateFormat.format(new Date()) + 
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
    }

    public final void stampajDnevniIzvestajIPresekNaFiskal() {
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(Settings.getInstance().getFiscalniIzvestajiDnevniIzvestajFormatDatuma());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements <Poruzbina>
            Document doc = docBuilder.newDocument();
            Element element = doc.createElement("Izvestaj");
            element.appendChild(doc.createTextNode("4"));
            doc.appendChild(element);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                                        new File(
                                                Settings.getInstance().getFiscalniPrinterPath() + 
                                                "dnevni i presek " + 
                                                dateFormat.format(new Date()) + 
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
    }
    
    public final void stampajPeriodicni(Date odDatuma, Date doDatuma ) {
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(Settings.getInstance().getFiscalniIzvestajiDnevniIzvestajFormatDatuma());
            java.text.SimpleDateFormat dateFormatDatum = new java.text.SimpleDateFormat(Settings.getInstance().getPrintTuraFormatDatuma());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements <Poruzbina>
            Document doc = docBuilder.newDocument();
            Element element = doc.createElement("Periodicni");
            doc.appendChild(element);

            Element elementFromDate = doc.createElement("FromDate");
            elementFromDate.appendChild(doc.createTextNode(dateFormatDatum.format(odDatuma)));
            element.appendChild(elementFromDate);

            Element elementToDate = doc.createElement("ToDate");
            elementToDate.appendChild(doc.createTextNode(dateFormatDatum.format(doDatuma)));
            element.appendChild(elementToDate);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                                        new File(
                                                Settings.getInstance().getFiscalniPrinterPath() + 
                                                "periodicni " + 
                                                dateFormat.format(new Date()) + 
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
    }

    public void ubaciElementeIzListeStavki(Document doc, Map<String, Element> mapaElemenata, List<StavkaTure> lista) {
        Element element;
        for (StavkaTure stavkaTura : lista) {

//                    <FiscalItem>
//                        <Naziv>ESPRESSO</Naziv>
//                        <Cena>120.0</Cena>
//                        <Kolicina>1.0</Kolicina>
//                        <PoreskaGrupa>GRUPA_DJ</PoreskaGrupa>
//                    </FiscalItem>
            Element stavkaElem = mapaElemenata.get("" + stavkaTura.ARTIKAL_ID);
            if (stavkaElem != null) {
                // loop the staff child node
		NodeList list = stavkaElem.getChildNodes();
                Node nodeKolicina = null;
                Node nodeCena = null;
                Node node = null;

		for (int i = 0; i < list.getLength(); i++) {
                    node = list.item(i);

                    if ("Cena".equals(node.getNodeName())) {
                        nodeCena = node;
                    }
                    if ("Kolicina".equals(node.getNodeName())) {
                        nodeKolicina = node;
                    }
                }
                try {
                    double cena = Utils.getDoubleFromString(nodeCena.getTextContent()) / Utils.getDoubleFromString(nodeKolicina.getTextContent());
                    double kolicina = Utils.getDoubleFromString(nodeKolicina.getTextContent()) + stavkaTura.getKolicina();
                    nodeKolicina.setTextContent("" + kolicina);
                    nodeCena.setTextContent("" + (cena * kolicina));    
                } catch (Exception e) {
                }
                ubaciElementeIzListeStavki(doc, mapaElemenata, stavkaTura.dodatniArtikli);
                continue;
            }

            stavkaElem = doc.createElement("FiscalItem");

            element = doc.createElement("Naziv");
            element.appendChild(doc.createTextNode("" + stavkaTura.getNaziv()));
            stavkaElem.appendChild(element);

            element = doc.createElement("Cena");
            element.appendChild(doc.createTextNode("" + stavkaTura.getCenaSaObracunatimPopustom()));
            stavkaElem.appendChild(element);

            element = doc.createElement("Kolicina");
            element.appendChild(doc.createTextNode("" + stavkaTura.getKolicina()));
            stavkaElem.appendChild(element);

            element = doc.createElement("PoreskaGrupa");
            element.appendChild(doc.createTextNode("GRUPA_DJ"));
            stavkaElem.appendChild(element);
            
            mapaElemenata.put("" + stavkaTura.ARTIKAL_ID, stavkaElem);
            
            ubaciElementeIzListeStavki(doc, mapaElemenata, stavkaTura.dodatniArtikli);
        }
        
    }
    
    public final void stampajGotovinskiRacun(Porudzbina porudzbina, List<NacinPlacanja> placanja) {
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(Settings.getInstance().getFiscalniIzvestajiDnevniIzvestajFormatDatuma());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            Map<String, Element> mapaElemenata = new HashMap();
            Element porudzbinaElem = null;
            Element element = null;
            Document doc = docBuilder.newDocument();

            // root elements <FiscalRecipet>
            porudzbinaElem = doc.createElement("FiscalRecipet");
            doc.appendChild(porudzbinaElem);

            for (Tura tura : porudzbina.getTure())
                ubaciElementeIzListeStavki(doc, mapaElemenata, tura.listStavkeTure);

            for (Map.Entry<String, Element> entry : mapaElemenata.entrySet()) {
                porudzbinaElem.appendChild(entry.getValue());
            }
            //<Placanje>
            //    <Gotovina>0.0</Gotovina>
            //    <Cek>0.0</Cek>
            //    <Kartica>300.0</Kartica>
            //</Placanje>
            Element stavka = doc.createElement("Placanje");
            porudzbinaElem.appendChild(stavka);
            
            for (NacinPlacanja nacinPlacanja : placanja) {
                if (nacinPlacanja.getNacinPlacanja().equals(NacinPlacanja.VrstePlacanja.GOTOVINA)) {
                    element = doc.createElement("Gotovina");
                    element.appendChild(doc.createTextNode(nacinPlacanja.getVrednostString()));
                    stavka.appendChild(element);
                }

                if (nacinPlacanja.getNacinPlacanja().equals(NacinPlacanja.VrstePlacanja.CEK)) {
                    element = doc.createElement("Cek");
                    element.appendChild(doc.createTextNode(nacinPlacanja.getVrednostString()));
                    stavka.appendChild(element);
                }

                if (nacinPlacanja.getNacinPlacanja().equals(NacinPlacanja.VrstePlacanja.KARTICA)) {
                    element = doc.createElement("Kartica");
                    element.appendChild(doc.createTextNode(nacinPlacanja.getVrednostString()));
                    stavka.appendChild(element);
                }
            }

            //  <Konobar>Konobar 1</Konobar>
            element = doc.createElement("Konobar");
            element.appendChild(doc.createTextNode(rmaster.RMaster.ulogovaniKonobar.imeKonobara));
            porudzbinaElem.appendChild(element);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            doc.setXmlStandalone(true);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);

            String imeFajla = dateFormat.format(new Date()) + ".xml";
            String putanjaFajla = Settings.getInstance().getFiscalniPrinterPath() + 
                    "\\" + imeFajla;
            StreamResult result = new StreamResult(new File(putanjaFajla));
            transformer.transform(source, result); 
            
            Timer timer = new Timer();
            timer.schedule(
                new TimerTask() {
                    long start = System.nanoTime();
                    long PET_SEKUNDI = 25000000000l;
                    String putanja = Settings.getInstance().getValueString("fiskal.putanja.fromFP") + imeFajla;
                    String porudzbinaID = "" + porudzbina.getID();

                    @Override
                    public void run() {
                        boolean racunOdstampan = false;
                        racunOdstampan = pokupiBrojFiskalnogRacunaIzFajla(putanja, porudzbinaID);
                        if (racunOdstampan || System.nanoTime() - start > PET_SEKUNDI)
                            this.cancel();
                    }
                }, 0, 1000);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }        
        
    }
    
    private boolean pokupiBrojFiskalnogRacunaIzFajla(String putanja, String porudzbinaID) {
        boolean rezultat = false;
        
        try {
            File f = new File(putanja);
            Scanner s = new Scanner(f);
            if (s.hasNextLong()) {
                HashMap<String, String> elementi = new HashMap<>();
                elementi.put("brojFiskalnogIsecka", "" + s.nextLong());
                new DBBroker().izmeni("racun", "id", porudzbinaID, elementi, true);
                s.close();
                s = null;
                rezultat = f.delete();
                f = null;
            }
        } catch (FileNotFoundException ex) {
            //System.err.println(ex);
        } catch (Exception ex) {
            System.err.println(ex);
        }         

        // 4. obrisi fajl
        
        return rezultat;
    }
    
    public final void stampajFakturu(Porudzbina porudzbina) {
        //        try {
//            
//            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getResourceAsStream("reports/faktura.jasper"));
//            
//            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, DBBroker.poveziSaBazom());
//            
//            JRViewer viewer = new JRViewer(print);
//
//            viewer.setVisible(true);
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        myController.setScreen(ScreenMap.NAPLATA, newData);
    }

    public final void stampajTuru(Tura tura, String izabraniStoId) {
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(Settings.getInstance().getFiscalniIzvestajiDnevniIzvestajFormatDatuma());

            Map<String, Document> fajlStampac = new HashMap();
            Map<String, Element> porudzbinaStampac = new HashMap();

            Document doc = null;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Element porudzbina = null;
            Element element = null;

            for (StavkaTure stavkaTura : tura.listStavkeTure) {
                doc = fajlStampac.get(stavkaTura.getStampacID());
                porudzbina = porudzbinaStampac.get(stavkaTura.getStampacID());
                if (doc == null) {
                    // root elements <Poruzbina>
                    doc = docBuilder.newDocument();
                    porudzbina = doc.createElement("Poruzbina");
                    doc.appendChild(porudzbina);

                    // <Datum>2016/11/05</Datum>
                    element = doc.createElement("Datum");
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
                    element.appendChild(doc.createTextNode(izabraniStoId));
                    porudzbina.appendChild(element);
                    
                    fajlStampac.put(stavkaTura.getStampacID(), doc);
                    porudzbinaStampac.put(stavkaTura.getStampacID(), porudzbina);
                }

                // <Stavka>
                //    <kolicina>1.0</kolicina>
                //    <naziv>ICE CAFFE</naziv>
                //    <tip></tip>               "", "DODA", "OPIS"
                // </Stavka>
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
                for (StavkaTure stavkaTureDodatni : stavkaTura.getArtikliOpisni()) {
                    stavka = doc.createElement("Stavka");
                    porudzbina.appendChild(stavka);

                    element = doc.createElement("kolicina");
                    element.appendChild(doc.createTextNode("" + stavkaTureDodatni.getKolicina()));
                    stavka.appendChild(element);

                    element = doc.createElement("naziv");
                    element.appendChild(doc.createTextNode("" + stavkaTureDodatni.getNaziv()));
                    stavka.appendChild(element);

                    element = doc.createElement("tip");
                    element.appendChild(doc.createTextNode("OPIS"));
                    stavka.appendChild(element);
                }
            }

            for (Map.Entry<String, Document> entry : fajlStampac.entrySet()) {
                doc = entry.getValue();
                porudzbina = porudzbinaStampac.get(entry.getKey());

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
                        "\\" + Stampa.getInstance().getStampacByID(entry.getKey()).naziv + 
                        "_" + dateFormat.format(new Date()) + 
                        ".xml")
                );
                transformer.transform(source, result);
                System.out.println("File saved!");
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }        
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
