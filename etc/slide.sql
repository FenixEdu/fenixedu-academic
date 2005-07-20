-- MySQL dump 9.11
--
-- Host: localhost    Database: slide
-- ------------------------------------------------------
-- Server version	4.0.20-log

--
-- Table structure for table `branches`
--

DROP TABLE IF EXISTS branches;
CREATE TABLE branches (
  uri blob,
  xnumber varchar(20) default NULL,
  childnumber varchar(20) default NULL,
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `children`
--

DROP TABLE IF EXISTS children;
CREATE TABLE children (
  uri blob,
  childuri blob,
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS label;
CREATE TABLE label (
  uri blob,
  xnumber varchar(20) default NULL,
  label blob,
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `latestrevisions`
--

DROP TABLE IF EXISTS latestrevisions;
CREATE TABLE latestrevisions (
  uri blob,
  branchname blob,
  xnumber varchar(20) default NULL,
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `links`
--

DROP TABLE IF EXISTS links;
CREATE TABLE links (
  link blob,
  linkto blob
) TYPE=MyISAM;

--
-- Table structure for table `locks`
--

DROP TABLE IF EXISTS locks;
CREATE TABLE locks (
  id blob,
  object blob,
  subject blob,
  type blob,
  expirationdate varchar(15) default NULL,
  inheritable int(11) default NULL,
  xexclusive int(11) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `objects`
--

DROP TABLE IF EXISTS objects;
CREATE TABLE objects (
  uri blob NOT NULL,
  classname blob,
  PRIMARY KEY  (uri(255)),
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS permissions;
CREATE TABLE permissions (
  object blob,
  revisionnumber varchar(20) default NULL,
  subject blob,
  action blob,
  inheritable int(11) default NULL,
  negative int(11) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `property`
--

DROP TABLE IF EXISTS property;
CREATE TABLE property (
  uri blob,
  xnumber varchar(20) default NULL,
  name blob,
  value blob,
  namespace blob,
  type varchar(100) default NULL,
  protected int(11) default NULL,
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `revision`
--

DROP TABLE IF EXISTS revision;
CREATE TABLE revision (
  uri blob,
  xnumber varchar(20) default NULL,
  branchname blob,
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `revisioncontent`
--

DROP TABLE IF EXISTS revisioncontent;
CREATE TABLE revisioncontent (
  uri blob,
  xnumber varchar(20) default NULL,
  content longblob,
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `revisions`
--

DROP TABLE IF EXISTS revisions;
CREATE TABLE revisions (
  uri blob NOT NULL,
  isversioned int(11) default NULL,
  initialrevision varchar(10) default NULL,
  PRIMARY KEY  (uri(255)),
  KEY uri_index (uri(255))
) TYPE=MyISAM;

--
-- Table structure for table `workingrevision`
--

DROP TABLE IF EXISTS workingrevision;
CREATE TABLE workingrevision (
  uri blob,
  baserevision varchar(20) default NULL,
  xnumber varchar(20) default NULL,
  KEY uri_index (uri(255))
) TYPE=MyISAM;

