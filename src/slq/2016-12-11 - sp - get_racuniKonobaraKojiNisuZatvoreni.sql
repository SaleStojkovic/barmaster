DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_racuniKonobaraKojiNisuZatvoreni`(in konobarID long)
BEGIN
	SELECT 	a.id,
			a.brojRacuna,
			a.brojStola AS 'Sto',
            SUM(b.cena*b.kolicina) AS 'Ukupno'
		FROM barmaster.racun a
		LEFT JOIN stavkaracuna b
		ON a.id=b.RACUN_ID
	WHERE zatvoren=0 AND a.konobar_id=konobarID
    GROUP BY a.id;
END$$
DELIMITER ;
