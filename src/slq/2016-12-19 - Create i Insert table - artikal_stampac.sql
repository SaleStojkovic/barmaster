CREATE TABLE `artikal_stampac` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `artikalID` bigint(20) NOT NULL,
  `stampacID` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1031 DEFAULT CHARSET=utf8;


INSERT INTO 
	artikal_stampac (artikalID, stampacID)
    SELECT id, 3 FROM artikal WHERE jelo = 1;
INSERT INTO 
	artikal_stampac (artikalID, stampacID)
	SELECT id, 2 FROM artikal WHERE pice = 1;