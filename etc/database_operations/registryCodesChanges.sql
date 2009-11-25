delete from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.serviceRequests.DiplomaCode';
delete from FF$DOMAIN_CLASS_INFO where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.serviceRequests.RegistryDiplomaCode';

rename table `REGISTRY_CODE_BAG` to `RECTORATE_SUBMISSION_BATCH`;
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.serviceRequests.RegistryCodeBag';

alter table `INSTITUTION_REGISTRY_CODE_GENERATOR` drop column `OID_CURRENT_BAG`;

alter table `REGISTRY_CODE` change column `OID_REGISTRY_CODE_BAG` `OID_RECTORATE_SUBMISSION_BATCH` bigint(20), drop column `OID_DOCUMENT_REQUEST`, drop column `OJB_CONCRETE_CLASS`;

alter table `RECTORATE_SUBMISSION_BATCH` drop column `OID_NEXT`, drop column `OID_PREVIOUS`, change column `DATE` `CREATION` timestamp NULL default NULL, add column `RECEPTION` timestamp NULL default NULL, add column `STATE` text, add column `SUBMISSION` timestamp NULL default NULL;
update RECTORATE_SUBMISSION_BATCH set STATE = 'UNSENT';