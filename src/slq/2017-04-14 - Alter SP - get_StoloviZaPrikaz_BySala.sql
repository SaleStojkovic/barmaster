DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_StoloviZaPrikaz_BySala`(in sala_ID bigint(20), in VremeDoRezervacije int)
BEGIN
	SELECT s.*, stonaziv.naziv, sto.KONOBAR_ID, sto.blokiran, r.brojStolaRezervacije, r.RezervacijaDatum, r.RezervacijaVreme FROM stoprikaz AS s
		LEFT JOIN stonaziv 
			ON s.broj = stonaziv.broj
		LEFT JOIN sto 
			ON s.broj = sto.broj 
		LEFT JOIN	(SELECT 
							brStola AS brojStolaRezervacije, 
							DATE(vreme) AS RezervacijaDatum, 
							TIME(vreme) AS RezervacijaVreme 
						FROM 
							rezervacija 
						WHERE 
							vreme BETWEEN NOW() AND DATE_ADD(NOW(),INTERVAL 120 MINUTE)
					) as r
			ON s.broj = r.brojStolaRezervacije
		WHERE s.GRAFIK_ID = sala_ID;
END$$
DELIMITER ;
