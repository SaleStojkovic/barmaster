DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getZatvoreniRacuniKonobaraTogDanaZaStampu`(in konobarID long)
BEGIN
	SELECT 	a.id,
            a.brojStola AS 'Sto',
            SUM(b.cena*((100.0-b.procenatPopusta)/100.0)) AS 'Ukupno',
            c.nacin,
            TIME(a.datum),
            a.brojRacuna,
            a.brojFiskalnogIsecka
	FROM racun a
		LEFT JOIN stavkaracuna b
			ON a.id=b.RACUN_ID
		LEFT JOIN placanje c
			ON a.id=c.RACUN_ID
		WHERE 
			zatvoren=1 AND 
			a.konobar_id=konobarID AND 
            TIMESTAMPDIFF(HOUR, a.datum, CURTIME()) < 1 AND
            c.nacin IN ('GOTOVINA','KARTICA','FAKTURA','CEK')
		GROUP BY a.id;
END$$
DELIMITER ;
