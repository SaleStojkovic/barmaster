DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPopustiZaNaplatu`()
BEGIN
	SELECT
		id,
		naziv,
                popust
	FROM
		stalnigost
	WHERE
		zaPrikazNaplata = 1
	ORDER BY
		zaPrikazNaplataSort
	LIMIT 8;
END$$
DELIMITER ;
