DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getArtikliFavorite`(in brojPrvogZapisa int, in brojZapisa int)
BEGIN
	SELECT
		a.id,
		barCode, 
		cena, 
		dozvoljenPopust, 
		jedinicaMere, 
		name as naziv, 
		prioritet, 
		skrNaziv, 
        slika, 
        IF (((SELECT COUNT(id) FROM artikal_dodaci WHERE ArtikalIDGlavni=a.id) + (SELECT COUNT(id) FROM artikal_atribut WHERE artikalID=a.id))>0, 'SLOZ', 'PROS') as tip
	FROM artikal a
	WHERE (blokiran=0) AND (favorite=1)
    ORDER BY prioritet, naziv
    LIMIT brojZapisa OFFSET brojPrvogZapisa;
END$$
DELIMITER ;
