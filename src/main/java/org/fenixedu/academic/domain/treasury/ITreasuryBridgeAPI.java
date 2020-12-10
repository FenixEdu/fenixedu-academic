package org.fenixedu.academic.domain.treasury;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.joda.time.LocalDate;

public interface ITreasuryBridgeAPI {

    // @formatter:off
    /* -------------------------------------------------
     * TREASURY INSTITUTION PRODUCTS, PAYMENT CODE POOLS
     * -------------------------------------------------
     */
    // @formatter:on

    public Set<ITreasuryEntity> getTreasuryEntities();

    public ITreasuryEntity getTreasuryEntityByCode(String code);

    public Set<ITreasuryProduct> getProducts(ITreasuryEntity treasuryEntity);

    public ITreasuryProduct getProductByCode(String code);

    public List<IPaymentCodePool> getPaymentCodePools(ITreasuryEntity treasuryEntity);

    public IPaymentCodePool getPaymentCodePoolByCode(String code);

    // @formatter:off
    /* ------------------------
     * ACADEMIC SERVICE REQUEST
     * ------------------------
     */
    // @formatter:on

    public static String ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT = "ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT";
    public static String ACADEMIC_SERVICE_REQUEST_REJECT_OR_CANCEL_EVENT = "ACADEMIC_SERVICE_REQUEST_REJECT_OR_CANCEL_EVENT";

    public IAcademicServiceRequestAndAcademicTaxTreasuryEvent academicTreasuryEventForAcademicServiceRequest(
            AcademicServiceRequest academicServiceRequest);

    // @formatter:off
    /* ----------
     * ENROLMENTS
     * ----------
     */
    // @formatter:on

    public static String STANDALONE_ENROLMENT = "STANDALONE_ENROLMENT";
    public static String EXTRACURRICULAR_ENROLMENT = "EXTRACURRICULAR_ENROLMENT";
    public static String IMPROVEMENT_ENROLMENT = "IMPROVEMENT_ENROLMENT";
    public static String NORMAL_ENROLMENT = "NORMAL_ENROLMENT";

    public void standaloneUnenrolment(Enrolment standaloneEnrolment);

    public void extracurricularUnenrolment(Enrolment extracurricularEnrolment);

    public void improvementUnrenrolment(EnrolmentEvaluation improvementEnrolmentEvaluation);

    // @formatter:off
    /* --------
     * TUITIONS
     * --------
     */
    // @formatter:on

    public boolean isToPayTuition(Registration registration, ExecutionYear executionYear);

    public ITuitionTreasuryEvent getTuitionForRegistrationTreasuryEvent(Registration registration, ExecutionYear executionYear);

    public ITuitionTreasuryEvent getTuitionForStandaloneTreasuryEvent(Registration registration, ExecutionYear executionYear);

    public ITuitionTreasuryEvent getTuitionForExtracurricularTreasuryEvent(Registration registration,
            ExecutionYear executionYear);

    public ITuitionTreasuryEvent getTuitionForImprovementTreasuryEvent(Registration registration, ExecutionYear executionYear);

    // @formatter:off
    /* --------------
     * ACADEMIC TAXES
     * --------------
     */
    // @formatter:on

    public IImprovementTreasuryEvent getImprovementTaxTreasuryEvent(Registration registration, ExecutionYear executionYear);

    public List<IAcademicTreasuryEvent> getAcademicTaxesList(Registration registration, ExecutionYear executionYear);

    // @formatter:off
    /* ------------------------
     * ACADEMIC TREASURY TARGET
     * ------------------------
     */
    // @formatter:on

    public IAcademicTreasuryEvent getAcademicTreasuryEventForTarget(IAcademicTreasuryTarget target);

    public void anullDebtsForTarget(IAcademicTreasuryTarget target, String reason);

    public IAcademicTreasuryEvent createDebt(ITreasuryEntity treasuryEntity, ITreasuryProduct treasuryProduct,
            IAcademicTreasuryTarget target, LocalDate when, boolean createPaymentCode, IPaymentCodePool paymentCodePool,
            int numberOfUnits, int numberOfPages);

    public IAcademicTreasuryEvent createDebt(ITreasuryEntity treasuryEntity, ITreasuryProduct treasuryProduct,
            IAcademicTreasuryTarget target, BigDecimal amount, LocalDate when, LocalDate dueDate, boolean createPaymentCode,
            IPaymentCodePool paymentCodePool);

    // @formatter:off
    /* --------------
     * ACADEMICAL ACT
     * --------------
     */
    // @formatter:on

    public boolean isAcademicalActsBlocked(Person person, LocalDate when);

    public boolean isAcademicalActBlockingSuspended(Person person, LocalDate when);

    // @formatter:off
    /* -----
     * OTHER
     * -----
     */
    // @formatter:on

    public List<IAcademicTreasuryEvent> getAllAcademicTreasuryEventsList(Person person);

    // @formatter:off
    /* ------------------------------------
     * ACADEMIC TREASURY MODULE INTEGRATION
     * ------------------------------------
     */
    // @formatter:on

    public boolean isPersonAccountTreasuryManagementAvailable(Person person);

    public String getPersonAccountTreasuryManagementURL(Person person);

    public String getRegistrationAccountTreasuryManagementURL(Registration registration);

    public void createAcademicDebts(Registration registration);

    public boolean isValidFiscalNumber(String fiscalAddressCountryCode, String fiscalNumber);

    public boolean updateCustomer(Person person, String fiscalAddressCountryCode, String fiscalNumber);

    public boolean createCustomerIfMissing(Person person);

    public void saveFiscalAddressFieldsFromPersonInActiveCustomer(Person person);

    public PhysicalAddress createSaftDefaultPhysicalAddress(Person person);

    public ITreasuryCustomer getActiveCustomer(Person person);

    public List<ITreasuryCustomer> getCustomersForFiscalNumber(Person person, String fiscalCountry, String fiscalNumber);

    public ITreasuryDebtAccount getActiveDebtAccountForRegistration(Registration registration);
}
