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
import rmaster.models.Porudzbina;
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
