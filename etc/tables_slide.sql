CREATE TABLE branches (
  uri blob NOT NULL, xnumber varchar(20) default NULL,
  childnumber varchar(20) default NULL, PRIMARY KEY (uri(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE children (
  uri blob, childuri blob NOT NULL, PRIMARY KEY (childuri(255)),
  KEY uri (uri(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE label (
  uri blob NOT NULL, xnumber varchar(20) default NULL,
  label blob, PRIMARY KEY  (uri(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE latestrevisions (
  uri blob NOT NULL, branchname varchar(255) default NULL,
  xnumber varchar(20) default NULL, PRIMARY KEY (uri(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE links (
  link blob NOT NULL, linkto blob,
  PRIMARY KEY  (link(255)), KEY linkto (linkto(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE locks (
  id blob, object blob NOT NULL, subject blob,
  type varchar(255) default NULL, expirationdate varchar(15) default NULL,
  inheritable tinyint(1) default NULL, xexclusive tinyint(1) default NULL,
  PRIMARY KEY  (object(255)), KEY id (id(255)), KEY subject (subject(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE objects (
  uri blob NOT NULL, classname varchar(255) default NULL,
  PRIMARY KEY  (uri(255)), KEY classname (classname)
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE permissions (
  object blob, revisionnumber varchar(20) default NULL,
  subject blob, action varchar(255) default NULL,
  inheritable tinyint(1) default NULL, negative tinyint(1) default NULL,
  KEY object (object(255)), KEY subject (subject(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE property (
  uri blob, xnumber varchar(20) default NULL,
  name varchar(255) default NULL, value blob,
  namespace varchar(255) default NULL, type varchar(100) default NULL,
  protected tinyint(1) default NULL, KEY uri (uri(255)), KEY name (name)
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE revision (
  uri blob NOT NULL, xnumber varchar(20) default NULL,
  branchname varchar(255) default NULL, PRIMARY KEY (uri(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE revisioncontent (
  uri blob NOT NULL, xnumber varchar(20) default NULL,
  content blob, PRIMARY KEY  (uri(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE revisions (
  uri blob NOT NULL, isversioned tinyint(1) default NULL,
  initialrevision varchar(10) default NULL, PRIMARY KEY (uri(255))
) TYPE=MyISAM;
# --------------------------------------------------------
CREATE TABLE workingrevision (
  uri blob NOT NULL, baserevision varchar(20) default NULL,
  xnumber varchar(20) default NULL, PRIMARY KEY (uri(255))
) TYPE=MyISAM;
