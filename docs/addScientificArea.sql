DROP TABLE IF EXISTS SCIENTIFIC_AREA;
CREATE TABLE SCIENTIFIC_AREA (
  id_internal int(11) NOT NULL auto_increment,
  name varchar(100) NOT NULL,
  PRIMARY KEY  (id_internal),
  UNIQUE(name)
) TYPE=InnoDB