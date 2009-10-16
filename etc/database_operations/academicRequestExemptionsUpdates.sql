
SET AUTOCOMMIT = 0;
START TRANSACTION;


SELECT @max_value:=null; (SELECT @max_value:=MAX(FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID) + 1 FROM FF$DOMAIN_CLASS_INFO);
INSERT IGNORE INTO FF$DOMAIN_CLASS_INFO VALUES ("net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemption", @max_value);


UPDATE EXEMPTION SET OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemption" WHERE OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.DegreeFinalizationCertificateRequestExemption";
SELECT @xpto:=null;SELECT @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemption";
UPDATE EXEMPTION SET OID = (@xpto << 32) + ID_INTERNAL WHERE OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemption";



SELECT @max_value:=null; (SELECT @max_value:=MAX(FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID) + 1 FROM FF$DOMAIN_CLASS_INFO);
INSERT IGNORE INTO FF$DOMAIN_CLASS_INFO VALUES ("net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemptionJustification", @max_value);


UPDATE EXEMPTION_JUSTIFICATION SET OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemptionJustification" WHERE OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.DegreeFinalizationCertificateRequestExemptionJustification";
SELECT @xpto:=null;SELECT @xpto:=FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_ID from FF$DOMAIN_CLASS_INFO where FF$DOMAIN_CLASS_INFO.DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemptionJustification";
UPDATE EXEMPTION_JUSTIFICATION SET OID = (@xpto << 32) + ID_INTERNAL WHERE OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemptionJustification";


UPDATE EXEMPTION E, EXEMPTION_JUSTIFICATION EJ SET E.OID_EXEMPTION_JUSTIFICATION = EJ.OID, EJ.OID_EXEMPTION = E.OID WHERE E.OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemption" AND E.ID_INTERNAL = EJ.KEY_EXEMPTION;


COMMIT;
