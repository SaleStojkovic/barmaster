DELIMITER $$
CREATE PROCEDURE `getArtikliGrupe`(in podgrupaID long, in brojPrvogZapisa int, in brojZapisa int)
BEGIN
	SELECT
		a.id,
		a.barCode, 
		a.cena, 
		a.dozvoljenPopust, 
		a.jedinicaMere, 
		a.name as naziv, 
		a.prioritet, 
		a.skrNaziv, 
        a.slika, 
        IF (((SELECT COUNT(id) FROM artikal_dodaci WHERE ArtikalIDGlavni=a.id) + (SELECT COUNT(id) FROM artikal_atribut WHERE artikalID=a.id))>0, 'SLOZ', 'PROS') as tip,
        s.stampacID
	FROM artikal a 
		INNER JOIN artikal_grupa b
			ON a.id = b.artikalID
		INNER JOIN artikal_stampac s
			ON a.id = s.artikalID
	WHERE (a.blokiran=0) AND (FIND_IN_SET(`tip`,'PROD,SLOZ,POLU') AND (b.grupaID=podgrupaID))
    ORDER BY a.prioritet, a.name
    LIMIT brojZapisa OFFSET brojPrvogZapisa;
END$$
DELIMITER ;
