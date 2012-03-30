# net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityFctScholarshipExemption
# ----------------------------------------------------------------------------

update EXEMPTION 
set OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityExternalScholarshipExemption"
where OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityFctScholarshipExemption";

update `FF$DOMAIN_CLASS_INFO`
set DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityExternalScholarshipExemption"
where DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityFctScholarshipExemption";

# net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionEvent
# -------------------------------------------------------------------------------------

update EVENT
set OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionEvent"
where OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionEvent";

update `FF$DOMAIN_CLASS_INFO`
set DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionEvent"
where DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionEvent";

# net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionPR
# ----------------------------------------------------------------------------------

update POSTING_RULE
set OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionPR"
where OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionPR";

update `FF$DOMAIN_CLASS_INFO`
set DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionPR"
where DOMAIN_CLASS_NAME = "net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionPR";

alter table `EVENT` 
change `OID_PHD_GRATUITY_FCT_SCHOLARSHIP_EXEMPTION` `OID_PHD_GRATUITY_EXTERNAL_SCHOLARSHIP_EXEMPTION` bigint(20) unsigned;

alter table `EXEMPTION` 
change `OID_FCT_SCHOLARSHIP_PHD_GRATUITY_CONTRIBUITION_EVENT` `OID_EXTERNAL_SCHOLARSHIP_PHD_GRATUITY_CONTRIBUITION_EVENT` bigint(20) unsigned;

update EVENT 
set EVENT_TYPE = "EXTERNAL_SCOLARSHIP" 
where EVENT_TYPE = "FCT_SCOLARSHIP";

update POSTING_RULE 
set EVENT_TYPE = "EXTERNAL_SCOLARSHIP" 
where EVENT_TYPE = "FCT_SCOLARSHIP";

alter table `EXEMPTION` add `OID_PARTY` bigint unsigned;
alter table `EXEMPTION` add index (OID_PARTY);

alter table `PARTY` add `OID_ROOT_DOMAIN_OBJECT_EXTERNAL_SCHOLARSHIP_PROVIDER` bigint unsigned;
alter table `PARTY` add index (OID_ROOT_DOMAIN_OBJECT_EXTERNAL_SCHOLARSHIP_PROVIDER);

update EXEMPTION 
set OID_PARTY = (select P.OID from PARTY_SOCIAL_SECURITY_NUMBER SS, PARTY P where SS.SOCIAL_SECURITY_NUMBER = "503904040" and P.OID = SS.OID_PARTY)
where OJB_CONCRETE_CLASS = "net.sourceforge.fenixedu.domain.phd.debts.PhdGratuityExternalScholarshipExemption";


