/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Bosko
 * Created: Feb 4, 2017
 */

CREATE TABLE `artikal_vrsta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `naziv_UNIQUE` (`naziv`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

INSERT INTO `artikal_vrsta` (`naziv`) VALUES ('glavni');
INSERT INTO `artikal_vrsta` (`naziv`) VALUES ('opisni');
INSERT INTO `artikal_vrsta` (`naziv`) VALUES ('dodatni');

ALTER TABLE `artikal` ADD COLUMN `artikalVrstaID` INT NOT NULL AFTER `favorite`;

UPDATE `artikal`    SET `artikalVrstaID`=1;

INSERT INTO `artikal` (`blokiran`,`dozvoljenPopust`,`jedinicaMere`,`jelo`,`mera`,`name`,`pice`,`poreskaGrupa`,`prioritet`,`sifra`,`skrNaziv`,`tip`,`trenutnaNV`,`ODELJENJENORMATIV_ID`,`PODGRUPA_ID`,`artikalVrstaID`) VALUES (b'0',b'1','KOM',b'0',1,'Hladna',b'1',3,1,'','Hladna','OPIS',0,null,-1,2);
INSERT INTO `artikal` (`blokiran`,`dozvoljenPopust`,`jedinicaMere`,`jelo`,`mera`,`name`,`pice`,`poreskaGrupa`,`prioritet`,`sifra`,`skrNaziv`,`tip`,`trenutnaNV`,`ODELJENJENORMATIV_ID`,`PODGRUPA_ID`,`artikalVrstaID`) VALUES (b'0',b'1','KOM',b'0',1,'Topla',b'1',3,1,'','Topla','OPIS',0,null,-1,2);
INSERT INTO `artikal` (`blokiran`,`dozvoljenPopust`,`jedinicaMere`,`jelo`,`mera`,`name`,`pice`,`poreskaGrupa`,`prioritet`,`sifra`,`skrNaziv`,`tip`,`trenutnaNV`,`ODELJENJENORMATIV_ID`,`PODGRUPA_ID`,`artikalVrstaID`) VALUES (b'0',b'1','KOM',b'0',1,'Mućena',b'1',3,1,'','Mućena','OPIS',0,null,-1,2);
INSERT INTO `artikal` (`blokiran`,`dozvoljenPopust`,`jedinicaMere`,`jelo`,`mera`,`name`,`pice`,`poreskaGrupa`,`prioritet`,`sifra`,`skrNaziv`,`tip`,`trenutnaNV`,`ODELJENJENORMATIV_ID`,`PODGRUPA_ID`,`artikalVrstaID`) VALUES (b'0',b'1','KOM',b'0',1,'Mešana',b'1',3,1,'','Mešana','OPIS',0,null,-1,2);

INSERT INTO `artikal` (`blokiran`,`cena`,`cena2`,`dozvoljenPopust`,`jedinicaMere`,`jelo`,`mera`,`name`,`pice`,`poreskaGrupa`,`prioritet`,`sifra`,`skrNaziv`,`tip`,`trenutnaNV`,`ODELJENJENORMATIV_ID`,`PODGRUPA_ID`,`artikalVrstaID`) VALUES (b'0',20.00,20.00,b'1','KOM',b'0',1,'Kolačić',b'1',3,1,'','Kolačić','DODA',0,null,-1,3);
INSERT INTO `artikal` (`blokiran`,`cena`,`cena2`,`dozvoljenPopust`,`jedinicaMere`,`jelo`,`mera`,`name`,`pice`,`poreskaGrupa`,`prioritet`,`sifra`,`skrNaziv`,`tip`,`trenutnaNV`,`ODELJENJENORMATIV_ID`,`PODGRUPA_ID`,`artikalVrstaID`) VALUES (b'0',30.00,30.00,b'1','KOM',b'0',1,'Čokoladica',b'1',3,1,'','Čokoladica','DODA',0,null,-1,3);
INSERT INTO `artikal` (`blokiran`,`cena`,`cena2`,`dozvoljenPopust`,`jedinicaMere`,`jelo`,`mera`,`name`,`pice`,`poreskaGrupa`,`prioritet`,`sifra`,`skrNaziv`,`tip`,`trenutnaNV`,`ODELJENJENORMATIV_ID`,`PODGRUPA_ID`,`artikalVrstaID`) VALUES (b'0',25.00,25.00,b'1','KOM',b'0',1,'Kesica meda',b'1',3,1,'','Kesica meda','DODA',0,null,-1,3);

CREATE TABLE `artikal_slozeni` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `artikalID` INT NOT NULL,
  `opisniDodatniArtikalID` INT NOT NULL,
  PRIMARY KEY (`id`));

/* Ovo bi valjda trebalo da uveze  kafa i neskafa sa hladno, toplo, mucena i mesana */
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (1,667);
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (1,668);
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (2,667);
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (2,669);
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (2,670);

INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (1,671);
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (1,672);
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (1,673);
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (2,671);
INSERT INTO `artikal_slozeni` (`artikalID`,`opisniDodatniArtikalID`) VALUES (2,672);

