CREATE TABLE trade (
id INT(20) NOT NULL AUTO_INCREMENT
,dt_user_id INT(11) NOT NULL
,instrument VARCHAR(20)
,start_date DATETIME NOT NULL
,end_date DATETIME DEFAULT 0
,no_of_contracts INT(5) NOT NULL
,start_price NUMERIC(7,2) NOT NULL
,end_price NUMERIC(7,2) NOT NULL
,zone VARCHAR(20) NOT NULL
,type VARCHAR(20) NOT NULL
,entry VARCHAR(20) NOT NULL
,profit_loss NUMERIC(5,2) NOT NULL
,comment VARCHAR(255)
,date_created DATETIME NOT NULL
,INDEX dt_user_id_idx (dt_user_id ASC)
,PRIMARY KEY (id)
,CONSTRAINT FOREIGN KEY (dt_user_id) REFERENCES dt_user(id)
) ENGINE=InnoDB AUTO_INCREMENT=1001;

