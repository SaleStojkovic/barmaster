/*
	vrsta korisnika = 1 - konobar, dalje se mogu birati vrste korisnika po zelji
*/
CREATE TABLE `posmeni` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `korisnikID` bigint(20) NOT NULL,
  `fxID` varchar(255) NOT NULL,
  `vrstaKorisnika` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


INSERT INTO `posmeni` (`korisnikID`,`fxID`,`vrstaKorisnika`)
VALUES (2,'fxID_DnevniIzvestaj',1);

INSERT INTO `posmeni` (`korisnikID`,`fxID`,`vrstaKorisnika`)
VALUES (2,'fxID_PeriodicniIzvestaj',1);

INSERT INTO `posmeni` (`korisnikID`,`fxID`,`vrstaKorisnika`)
VALUES (2,'fxID_ZakljucenjeDana',1);

INSERT INTO `posmeni` (`korisnikID`,`fxID`,`vrstaKorisnika`)
VALUES (2,'fxID_PrometKorisnika',1);

INSERT INTO `posmeni` (`korisnikID`,`fxID`,`vrstaKorisnika`)
VALUES (2,'fxID_PresekStanja',1);

INSERT INTO `posmeni` (`korisnikID`,`fxID`,`vrstaKorisnika`)
VALUES (2,'fxID_ProdatiArtikli',1);


INSERT INTO `posmeni` (`korisnikID`,`fxID`,`vrstaKorisnika`)
VALUES (1,'fxID_PresekStanja',1,1);
