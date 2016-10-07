/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

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
    
}
