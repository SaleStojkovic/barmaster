/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Bosko
 * Created: Mar 31, 2017
 */

DROP procedure IF EXISTS `getRezervacijeAktuelne`;

DELIMITER $$
USE `barmaster`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRezervacijeAktuelne`()
BEGIN
    SELECT 
	ime, 
        vreme, 
        brStola, 
        brOsoba, 
        tel, 
        napomena, 
        id 
	FROM rezervacija 
    WHERE (vreme > DATE_SUB(NOW(), INTERVAL 1 HOUR))
    ORDER BY vreme;
END$$

DELIMITER ;


