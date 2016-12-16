DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `zatvoriRacunIOslobodiSto`(racunID INT(20), vreme DATETIME, stoBroj INT(11))
BEGIN
	DECLARE brojOtvorenihRacuna INT;
    DECLARE brojNovogRacuna INT(20);
    
    SET brojNovogRacuna = getSledeciRedniBroj('racun.broj.sledeci');

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
			AND zatvoren = 0
		INTO brojOtvorenihRacuna;
            
	IF (brojOtvorenihRacuna = 0) THEN
		DELETE
			FROM sto
			WHERE broj = stoBroj;
	END IF;
END$$
DELIMITER ;
