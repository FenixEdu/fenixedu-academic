
-- 

UPDATE META_DOMAIN_OBJECT 
SET TYPE = "net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption" 
WHERE TYPE = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemption";

UPDATE FF$DOMAIN_CLASS_INFO 
SET DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption" 
WHERE DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemption";

UPDATE EXEMPTION 
SET OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption" 
WHERE OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemption";

-- 

UPDATE META_DOMAIN_OBJECT 
SET TYPE = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicEventExemptionJustification" 
WHERE TYPE = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemptionJustification";

UPDATE FF$DOMAIN_CLASS_INFO 
SET DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicEventExemptionJustification" 
WHERE DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemptionJustification";

UPDATE EXEMPTION_JUSTIFICATION 
SET OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicEventExemptionJustification" 
WHERE OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestExemptionJustification";
