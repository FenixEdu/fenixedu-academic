package org.fenixedu.academic.domain.treasury;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;


public interface ITreasuryBridgeAPI {
    
    public static String ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT = "ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT";
    public static String STANDALONE_ENROLMENT = "STANDALONE_ENROLMENT";
    public static String STANDALONE_UNENROLMENT = "STANDALONE_UNENROLMENT";
    
    public void registerNewAcademicServiceRequestSituationHandler();
    
    public void registerStandaloneEnrolmentHandler();
    
    public void registerStandaloneUnenrolmentHandler();
    
    public IAcademicTreasuryEvent academicTreasuryEventForAcademicServiceRequest(final AcademicServiceRequest academicServiceRequest);

    public void standaloneUnenrolment(final Enrolment standaloneEnrolment);
    
}
