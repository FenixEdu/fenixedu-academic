
--
-- Table structure for table 'Abreviaturas'
--

DROP TABLE IF EXISTS `Abreviaturas`;
CREATE TABLE `Abreviaturas` (
  `abrv` varchar(50) default NULL,
  `expansao` varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table 'Concelho'
--

DROP TABLE IF EXISTS `Concelho`;
CREATE TABLE `Concelho` (
  `Chave` int(11) NOT NULL default '0',
  `chaveDistrito` int(11) NOT NULL default '0',
  `Designacao` varchar(35) NOT NULL default ''
) TYPE=MyISAM;


--
-- Table structure for table 'Distrito'
--

DROP TABLE IF EXISTS `Distrito`;
CREATE TABLE `Distrito` (
  `Chave` int(11) NOT NULL default '0',
  `Designacao` varchar(35) NOT NULL default ''
) TYPE=MyISAM;

--
-- Table structure for table 'Freguesia'
--

DROP TABLE IF EXISTS `Freguesia`;
CREATE TABLE `Freguesia` (
  `Chave` int(11) NOT NULL default '0',
  `chaveDistrito` int(11) NOT NULL default '0',
  `chaveConcelho` int(11) NOT NULL default '0',
  `Designacao` varchar(35) NOT NULL default ''
) TYPE=MyISAM;


--
-- Table structure for table 'PaisNaturalidade'
--

DROP TABLE IF EXISTS `PaisNaturalidade`;
CREATE TABLE `PaisNaturalidade` (
  `Cod` char(2) NOT NULL default '',
  `Origem` varchar(30) NOT NULL default ''
) TYPE=MyISAM;




--
-- Table structure for table 'TopWords'
--

DROP TABLE IF EXISTS `TopWords`;
CREATE TABLE `TopWords` (
  `Palavra` varchar(50) NOT NULL default ''
) TYPE=MyISAM;


--
-- Table structure for table 'concelhoExpansao'
--

DROP TABLE IF EXISTS `concelhoExpansao`;
CREATE TABLE `concelhoExpansao` (
  `descr1` varchar(50) default NULL,
  `descr2` varchar(50) default NULL
) TYPE=MyISAM;


--
-- Table structure for table 'conversaoCidadePais'
--

DROP TABLE IF EXISTS `conversaoCidadePais`;
CREATE TABLE `conversaoCidadePais` (
  `descr1` varchar(50) default NULL,
  `descr2` varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table 'conversaoLocalConcelho'
--

DROP TABLE IF EXISTS `conversaoLocalConcelho`;
CREATE TABLE `conversaoLocalConcelho` (
  `localidade` varchar(50) default NULL,
  `concelho` varchar(50) default NULL
) TYPE=MyISAM;




--
-- Table structure for table 'conversaoLocalDistrito'
--

DROP TABLE IF EXISTS `conversaoLocalDistrito`;
CREATE TABLE `conversaoLocalDistrito` (
  `localidade` varchar(50) default NULL,
  `distrito` varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table 'freguesiaExpansao'
--

DROP TABLE IF EXISTS `freguesiaExpansao`;
CREATE TABLE `freguesiaExpansao` (
  `descr1` varchar(50) default NULL,
  `descr2` varchar(50) default NULL
) TYPE=MyISAM;


--
-- Table structure for table 'paisExpansao'
--

DROP TABLE IF EXISTS `paisExpansao`;
CREATE TABLE `paisExpansao` (
  `descr1` varchar(50) NOT NULL default '',
  `descr2` varchar(50) NOT NULL default ''
) TYPE=MyISAM;


