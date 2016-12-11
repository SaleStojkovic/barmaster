DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getHotelGost`()
BEGIN
	SELECT
		id, 
        naziv, 
        sifra
	FROM 
		hotelgost
	WHERE
		prikazEkran = 1 AND
		(medjuZbir = 0 OR medjuZbir IS NULL)
	LIMIT 8;
END$$
DELIMITER ;
