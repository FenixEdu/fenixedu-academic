package org.fenixedu.academic.domain.treasury;

import java.util.List;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.joda.time.LocalDate;

public interface ITreasuryBridgeAPI {

    /* ------------------------
     * ACADEMIC SERVICE REQUEST
     * ------------------------
     */

    public static String ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT = "ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT";

    public void registerNewAcademicServiceRequestSituationHandler();

    public IAcademicServiceRequestAndAcademicTaxTreasuryEvent academicTreasuryEventForAcademicServiceRequest(
            final AcademicServiceRequest academicServiceRequest);

    /* ----------
     * ENROLMENTS
     * ----------
     */

    public static String STANDALONE_ENROLMENT = "STANDALONE_ENROLMENT";
    public static String EXTRACURRICULAR_ENROLMENT = "EXTRACURRICULAR_ENROLMENT";
    public static String IMPROVEMENT_ENROLMENT = "IMPROVEMENT_ENROLMENT";
    public static String NORMAL_ENROLMENT = "NORMAL_ENROLMENT";

    public void registerStandaloneEnrolmentHandler();

    public void registerExtracurricularEnrolmentHandler();

    public void registerImprovementEnrolmentHandler();

    public void standaloneUnenrolment(final Enrolment standaloneEnrolment);

    public void extracurricularUnenrolment(final Enrolment extracurricularEnrolment);

    public void improvementUnrenrolment(final EnrolmentEvaluation improvementEnrolmentEvaluation);

    /* --------
     * TUITIONS
     * --------
     */

    public boolean isToPayTuition(final Registration registration, final ExecutionYear executionYear);
    
    public ITuitionTreasuryEvent getTuitionForRegistrationTreasuryEvent(final Registration registration,
            final ExecutionYear executionYear);

    public ITuitionTreasuryEvent getTuitionForStandaloneTreasuryEvent(final Registration registration,
            final ExecutionYear executionYear);

    public ITuitionTreasuryEvent getTuitionForExtracurricularTreasuryEvent(final Registration registration,
            final ExecutionYear executionYear);

    public ITuitionTreasuryEvent getTuitionForImprovementTreasuryEvent(final Registration registration,
            final ExecutionYear executionYear);

    /* --------------
     * ACADEMIC TAXES
     * --------------
     */

    public IImprovementTreasuryEvent getImprovementTaxTreasuryEvent(final Registration registration, final ExecutionYear executionYear);
    
    public List<IAcademicTreasuryEvent> getAcademicTaxesList(final Registration registration, final ExecutionYear executionYear);


    /* --------------
     * ACADEMICAL ACT
     * --------------
     */

    public boolean isAcademicalActsBlocked(final Person person, final LocalDate when);
    
    public boolean isAcademicalActBlockingSuspended(final Person person, final LocalDate when);
    
    
    /* -----
     * OTHER
     * -----
     */
    
    public List<IAcademicTreasuryEvent> getAllAcademicTreasuryEventsList(final Person person, final ExecutionYear executionYear);

    public List<IAcademicTreasuryEvent> getAllAcademicTreasuryEventsList(final Person person);
    
    /* ------------------------------------
     * ACADEMIC TREASURY MODULE INTEGRATION
     * ------------------------------------
     */
    
    public boolean isPersonAccountTreasuryManagementAvailable(final Person person);
    public String getPersonAccountTreasuryManagementURL(final Person person);
    
}
