package net.sourceforge.fenixedu.domain.documents;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

/**
 * Type of {@GeneratedDocument}s
 * 
 * @author Pedro Santos (pmrsa)
 */
public enum GeneratedDocumentType {
    CREDIT_NOTE,

    RECEIPT,

    SCHOOL_REGISTRATION_CERTIFICATE,

    ENROLMENT_CERTIFICATE,

    APPROVEMENT_CERTIFICATE,

    DEGREE_FINALIZATION_CERTIFICATE,

    EXAM_DATE_CERTIFICATE,

    SCHOOL_REGISTRATION_DECLARATION,

    ENROLMENT_DECLARATION,

    IRS_DECLARATION,

    IRS_CUSTOM_DECLARATION,

    DIPLOMA_REQUEST,

    COURSE_LOAD,

    EXTERNAL_COURSE_LOAD,

    PROGRAM_CERTIFICATE,

    EXTERNAL_PROGRAM_CERTIFICATE;

    public static GeneratedDocumentType determineType(DocumentRequestType documentRequestType) {
	return valueOf(documentRequestType.name());
    }
}
