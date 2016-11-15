/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.text.DecimalFormat;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 *
 * @author Bosko
 */
public class Utils {
    
    /**
     * Description: Za kontrolu na kojoj je izvrsen dogadjaj postavlja stil,
     *              a za sve ostale ciji je parent isti sklanja taj stil
     *              (oznacavanje aktivne grupe, podgrupe, artikla)
     * Created: 27.09.2016.
     * @author Bosko
     * @param e
     * @param stilButtonGrupeSelektovana
     */
    public static void postaviStil_ObrisiZaOstaleKontroleRoditelja(ActionEvent e, String stilButtonGrupeSelektovana) {
        Node node = (Node)e.getSource();
        
        node.getParent().getChildrenUnmodifiable().stream().map((ostali) -> (Node) ostali).forEach((x) -> {
            x.getStyleClass().removeAll(stilButtonGrupeSelektovana);
        });
        node.getStyleClass().add(stilButtonGrupeSelektovana);

        
    }
    
    /**
     * Description: Vraca double iz String-a koji je formata 1,234.56
     * Created: 15.11.2016.
     * @author Bosko
     * @param stringVrednost
     * @return resultDouble
     */
    public static double getDoubleFromString(String stringVrednost) {
        double resultDouble = 0;
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("#,##0.00");
        
        try {
            resultDouble = df.parse(stringVrednost).doubleValue();  
        } catch(Exception e) { 
            resultDouble = 0; 
        }
        return resultDouble;
   }

    /**
     * Description: Vraca String iz double-a koji je formata 1,234.56
     * Created: 15.11.2016.
     * @author Bosko
     * @param doubleVrednost
     * @return resultString
     */
    public static String getStringFromDouble(double doubleVrednost) {
        return String.format("%1$,.2f", doubleVrednost);
    }

}
