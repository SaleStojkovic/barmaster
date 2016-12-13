DELIMITER $$
CREATE DEFINER=`root`@`localhost` FUNCTION `getSledeciRedniBrojIPovecaj`(settingName VARCHAR(255)) RETURNS int(11)
BEGIN
	DECLARE rezultat int(20);
	SELECT 
			actual
		FROM
			setting
		WHERE
			name = settingName
	INTO rezultat;
    
    IF (rezultat IS NULL) THEN
		SET rezultat = 1;
	END IF;
    
    UPDATE
			setting
        SET
			actual = rezultat + 1
		WHERE
			name = settingName;
RETURN rezultat;
END$$
DELIMITER ;
