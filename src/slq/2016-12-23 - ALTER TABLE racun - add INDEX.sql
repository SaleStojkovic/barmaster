/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Bosko
 * Created: Dec 23, 2016
 */

ALTER TABLE `racun` 
ADD INDEX `I_RACUN_BROJSTOLAKONOBARID` (`brojStola` ASC, `zatvoren` ASC, `storniran` ASC);
