DELIMITER $$
CREATE PROCEDURE `getPorudzbinaTuraStavke`(in idTure BIGINT)
BEGIN
	SELECT
		sr.*,
        a.dozvoljenPopust,
        st.stampacID
	FROM stavkaracuna sr
		INNER JOIN artikal a
			ON sr.ARTIKAL_ID = a.id
		LEFT JOIN artikal_stampac st
			ON sr.ARTIKAL_ID = st.artikalID
	WHERE TURA_ID = idTure;
END$$
DELIMITER ;
