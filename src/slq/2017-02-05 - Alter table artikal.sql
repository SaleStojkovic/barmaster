/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Bosko
 * Created: Feb 5, 2017
 */

UPDATE artikal
	SET favorite = 0
    WHERE ISNULL(favorite);
    
ALTER TABLE `artikal` 
CHANGE COLUMN `favorite` `favorite` TINYINT(4) NOT NULL DEFAULT 0 ;