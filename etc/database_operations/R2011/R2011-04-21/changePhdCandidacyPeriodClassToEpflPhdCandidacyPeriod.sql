SELECT @entry_exists := COUNT(*) FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod';
 
update FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod' 
	WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod' 
			AND @entry_exists = 0;

SELECT @epfl_candidacy_period_class_id := DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod';

update CANDIDACY_PERIOD SET OID = ((@epfl_candidacy_period_class_id << 32) + (OID & 0x00000000ffffffff)), TYPE = 'EPFL', OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod' WHERE OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod';
