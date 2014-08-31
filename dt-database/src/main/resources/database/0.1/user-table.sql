CREATE TABLE dt_user (
id INT(11) NOT NULL AUTO_INCREMENT
,username VARCHAR(255) NOT NULL
,date_created DATETIME NOT NULL
,PRIMARY KEY (id)
,UNIQUE(username)
) ENGINE=InnoDB AUTO_INCREMENT=1001;


insert into dt_user values (1, "Ruben", now());