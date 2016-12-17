DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPorudzbinaTuraStavke`(in idTure BIGINT)
BEGIN
	SELECT
		sr.*,
        a.dozvoljenPopust
	FROM stavkaracuna sr
		INNER JOIN artikal a
			ON sr.ARTIKAL_ID = a.id
	WHERE TURA_ID = idTure;
END$$
DELIMITER ;
