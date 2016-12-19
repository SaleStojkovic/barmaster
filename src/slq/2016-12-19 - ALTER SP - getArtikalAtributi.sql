DELIMITER $$
CREATE PROCEDURE `getArtikalAtributi`(in ArtikalID long, in brojPrvogZapisa int, in brojZapisa int)
BEGIN
	SELECT 
		a.id, 
        a.naziv 
	FROM atribut a join artikal_atribut b 
    ON a.id = b.atributID 
    WHERE b.artikalID = ArtikalID
    LIMIT brojZapisa OFFSET brojPrvogZapisa;
END$$
DELIMITER ;
