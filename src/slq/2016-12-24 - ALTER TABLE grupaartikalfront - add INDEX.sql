/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Bosko
 * Created: Dec 24, 2016
 */

ALTER TABLE `grupaartikalafront` 
ADD INDEX `I_GRUPAFRONT_GRUPA` (`GRUPA_ID` ASC);

ALTER TABLE `grupaartikalafront` 
ADD INDEX `I_GRUPAFRONT_PRIKAZ_GRUPA` (`prikazNaEkran` ASC, `GRUPA_ID` ASC);