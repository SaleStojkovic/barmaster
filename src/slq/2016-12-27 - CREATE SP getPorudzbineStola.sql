DELIMITER $$
CREATE PROCEDURE `getPorudzbineStola`(in stoID varchar(10))
BEGIN
	SELECT 
		a.*
	FROM 
			racun a 
        LEFT JOIN 
			stoprikaz b
	ON 
		a.brojStola = b.broj
    WHERE 
        (b.id = stoID) AND 
        (a.zatvoren = 0) AND 
        (a.storniran = 0)
    ORDER BY 
	gost;
END$$
DELIMITER ;
