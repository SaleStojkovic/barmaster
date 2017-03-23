/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Bosko
 * Created: Mar 23, 2017
 */

USE `barmaster`;
DROP procedure IF EXISTS `zatvoriRacunIOslobodiSto`;

DELIMITER $$
USE `barmaster`$$
CREATE PROCEDURE `zatvoriRacunIOslobodiSto`(racunID INT(20), OUT vreme DATETIME , OUT brojNovogRacuna INT(11))
BEGIN
	DECLARE brojOtvorenihRacuna INT(11);
    DECLARE stoBroj INT(11);
    
    SET brojNovogRacuna = getSledeciRedniBroj('racun.broj.sledeci');
	SET vreme = NOW();
    
    SELECT
			brojStola
		FROM racun
        WHERE 
			id = racunID
		INTO  stoBroj;

	UPDATE
			racun
        SET
			zatvoren = b'1',
            vremeIzdavanjaRacuna = vreme,
            brojRacuna = brojNovogRacuna
		WHERE
			id = racunID;
            
	SELECT
			COUNT(*)
		FROM racun
        WHERE brojStola = stoBroj
			AND zatvoren = b'0'
		INTO brojOtvorenihRacuna;
            
	IF (brojOtvorenihRacuna = 0) THEN
		DELETE
			FROM sto
			WHERE broj = stoBroj;
	END IF;
END$$

DELIMITER ;

