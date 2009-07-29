SET AUTOCOMMIT = 0;
START TRANSACTION;

DROP TABLE IF EXISTS PHD_PROCESS_STATE; 
create table `PHD_PROCESS_STATE` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OJB_CONCRETE_CLASS` varchar(255) NOT NULL,
  `KEY_PERSON` int(11),
  `KEY_PROCESS` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11) default '1',
  `OID` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_PROCESS` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `REMARKS` longtext,
  `TYPE` text,
  `WHEN_CREATED` datetime NOT NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_PERSON),
  index (OID_PERSON),
  index (KEY_PROCESS),
  index (OID_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1;

SELECT @max_value:=null; (SELECT @max_value:=MAX(FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID) + 1 FROM FF$DOMAIN_CLASS_INFO);
INSERT IGNORE INTO FF$DOMAIN_CLASS_INFO VALUES ("net.sourceforge.fenixedu.domain.phd.PhdProcessState", @max_value);

SELECT @max_value:=null; (SELECT @max_value:=MAX(FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID) + 1 FROM FF$DOMAIN_CLASS_INFO);
INSERT IGNORE INTO FF$DOMAIN_CLASS_INFO VALUES ("net.sourceforge.fenixedu.domain.phd.PhdCandidacyProcessState", @max_value);

SELECT @max_value:=null; (SELECT @max_value:=MAX(FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID) + 1 FROM FF$DOMAIN_CLASS_INFO);
INSERT IGNORE INTO FF$DOMAIN_CLASS_INFO VALUES ("net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState", @max_value);

INSERT INTO PHD_PROCESS_STATE (OJB_CONCRETE_CLASS, KEY_PERSON, KEY_PROCESS, OID_PERSON, OID_PROCESS, OID_ROOT_DOMAIN_OBJECT, TYPE, WHEN_CREATED) SELECT "net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState", PROCESS.KEY_PERSON, PROCESS.ID_INTERNAL, PROCESS.OID_PERSON, PROCESS.OID, PROCESS.OID_ROOT_DOMAIN_OBJECT, PROCESS.STATE, NOW() FROM PROCESS WHERE PROCESS.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess";
SELECT @xpto:=null;SELECT @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState";
UPDATE PHD_PROCESS_STATE SET PHD_PROCESS_STATE.OID = (@xpto << 32) + ID_INTERNAL WHERE OID IS NULL AND OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState";

INSERT INTO PHD_PROCESS_STATE (OJB_CONCRETE_CLASS, KEY_PERSON, KEY_PROCESS, OID_PERSON, OID_PROCESS, OID_ROOT_DOMAIN_OBJECT, TYPE, WHEN_CREATED) SELECT "net.sourceforge.fenixedu.domain.phd.PhdCandidacyProcessState", PROCESS.KEY_PERSON, PROCESS.ID_INTERNAL, PROCESS.OID_PERSON, PROCESS.OID, PROCESS.OID_ROOT_DOMAIN_OBJECT, PROCESS.STATE, NOW() FROM PROCESS WHERE PROCESS.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess";
SELECT @xpto:=null;SELECT @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.PhdCandidacyProcessState";
UPDATE PHD_PROCESS_STATE SET PHD_PROCESS_STATE.OID = (@xpto << 32) + ID_INTERNAL WHERE OID IS NULL AND OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.PhdCandidacyProcessState";

UPDATE PROCESS SET PROCESS.STATE = NULL WHERE OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" OR OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess"; 

COMMIT;
