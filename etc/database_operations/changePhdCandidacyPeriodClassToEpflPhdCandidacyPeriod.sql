SELECT @entry_exists := COUNT(*) FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod';
 
update FF$DOMAIN_CLASS_INFO SET DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod' 
	WHERE DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod' 
			AND @entry_exists = 0;
