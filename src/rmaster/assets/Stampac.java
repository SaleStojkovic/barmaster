/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import rmaster.models.Konobar;
import rmaster.models.Porudzbina;
import rmaster.views.NumerickaTastaturaController;

/**
 *
 * @author Bosko
 */
public final class Stampac {
    public final void stampajGotovinskiRacun() {
        
    }
    
    public final void stampajFakturu() {
        
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
        // DOTO: Odstampati medjuzbir
        
        porudzbina.setBlokiranaPorudzbina(true);
    }
}
