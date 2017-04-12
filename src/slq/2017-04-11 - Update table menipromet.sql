/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Bosko
 * Created: Apr 11, 2017
 */

ALTER TABLE `menipromet` 
CHANGE COLUMN `datum` `datum` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;
