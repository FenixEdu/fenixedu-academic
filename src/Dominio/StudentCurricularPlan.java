package Dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.degree.enrollment.CurricularCourse2Enroll;
import Dominio.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import Dominio.degree.enrollment.rules.IEnrollmentRule;
import ServidorAplicacao.Servico.exceptions.BothAreasAreTheSameServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;
import Util.EnrolmentState;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.enrollment.CurricularCourseEnrollmentType;
import Util.enrollment.EnrollmentCondition;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlan extends DomainObject implements IStudentCurricularPlan {

    protected Integer studentKey;

    protected Integer branchKey;

    protected Integer degreeCurricularPlanKey;
 
    protected Integer employeeKey;

    protected String ojbConcreteClass;

    protected IStudent student;

    protected IBranch branch;

    protected IDegreeCurricularPlan degreeCurricularPlan;

    protected IEmployee employee;

    protected Date startDate;

    protected StudentCurricularPlanState currentState;

    protected List enrolments;

    protected Integer completedCourses;

    protected Double classification;

    protected Integer enrolledCourses;

    protected String observations;

    protected Specialization specialization;

    protected Double givenCredits;

    protected Date when;

    // For enrollment purposes only
    protected Map acumulatedEnrollments;

    protected List notNeedToEnrollCurricularCourses;

    public StudentCurricularPlan() {
        ojbConcreteClass = getClass().getName();
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof IStudentCurricularPlan) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) obj;

            result = getStudent().equals(studentCurricularPlan.getStudent())
                    && getDegreeCurricularPlan().equals(studentCurricularPlan.getDegreeCurricularPlan())
                    && getCurrentState().equals(studentCurricularPlan.getCurrentState());
        }

        return result;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "internalCode = " + getIdInternal() + "; ";
        result += "student = " + this.student + "; ";
        result += "degreeCurricularPlan = " + this.degreeCurricularPlan + "; ";
        result += "startDate = " + this.startDate + "; ";
        result += "specialization = " + this.specialization + "; ";
        result += "currentState = " + this.currentState + "]\n";
        result += "when alter = " + this.when + "]\n";
        if (this.employee != null) {
            result += "employee = " + this.employee.getPerson().getNome() + "]\n";
        }
        return result;
    }

    public IBranch getBranch() {
        return branch;
    }

    public Integer getBranchKey() {
        return branchKey;
    }

    public Double getClassification() {
        return classification;
    }

    public Integer getCompletedCourses() {
        return completedCourses;
    }

    public StudentCurricularPlanState getCurrentState() {
        return currentState;
    }

    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public Integer getDegreeCurricularPlanKey() {
        return degreeCurricularPlanKey;
    }

    public IEmployee getEmployee() {
        return employee;
    }

    public Integer getEmployeeKey() {
        return employeeKey;
    }

    public Integer getEnrolledCourses() {
        return enrolledCourses;
    }

    public List getEnrolments() {
        return enrolments;
    }

    public Double getGivenCredits() {
        return givenCredits;
    }

    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    public String getObservations() {
        return observations;
    }

    public IBranch getSecundaryBranch() {
        return null;
    }

    public Integer getSecundaryBranchKey() {
        return null;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public Date getStartDate() {
        return startDate;
    }

    public IStudent getStudent() {
        return student;
    }

    public Integer getStudentKey() {
        return studentKey;
    }

    public Date getWhen() {
        return when;
    }

    public void setBranch(IBranch branch) {
        this.branch = branch;
    }

    public void setBranchKey(Integer branchKey) {
        this.branchKey = branchKey;
    }

    public void setClassification(Double classification) {
        this.classification = classification;
    }

    public void setCompletedCourses(Integer completedCourses) {
        this.completedCourses = completedCourses;
    }

    public void setCurrentState(StudentCurricularPlanState currentState) {
        this.currentState = currentState;
    }

    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public void setDegreeCurricularPlanKey(Integer degreeCurricularPlanKey) {
        this.degreeCurricularPlanKey = degreeCurricularPlanKey;
    }

    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

    public void setEmployeeKey(Integer employeeKey) {
        this.employeeKey = employeeKey;
    }

    public void setEnrolledCourses(Integer enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void setEnrolments(List enrolments) {
        this.enrolments = enrolments;
    }

    public void setGivenCredits(Double givenCredits) {
        this.givenCredits = givenCredits;
    }

    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setSecundaryBranch(IBranch secundaryBranch) {
    }

    public void setSecundaryBranchKey(Integer secundaryBranchKey) {
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStudent(IStudent student) {
        this.student = student;
    }

    public void setStudentKey(Integer studentKey) {
        this.studentKey = studentKey;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    public List getNotNeedToEnrollCurricularCourses() {
        return notNeedToEnrollCurricularCourses;
    }

    public void setNotNeedToEnrollCurricularCourses(List notNeedToEnrollCurricularCourses) {
        this.notNeedToEnrollCurricularCourses = notNeedToEnrollCurricularCourses;
    }

    public List getAllEnrollments() {

        IStudentCurricularPlan pastStudentCurricularPlan = (IStudentCurricularPlan) CollectionUtils.find(getStudent()
                .getStudentCurricularPlans(), new Predicate() {
            public boolean evaluate(Object obj) {
                IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) obj;
                return studentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.PAST_OBJ);
            }
        });

        List allEnrollments = new ArrayList();

        if (pastStudentCurricularPlan != null) {
            allEnrollments.addAll(pastStudentCurricularPlan.getEnrolments());
        }
        
        allEnrollments.addAll(getEnrolments());

        return allEnrollments;
    }

    public List getCurricularCoursesToEnroll(IExecutionPeriod executionPeriod, EnrollmentRuleType enrollmentRuleType)
            throws ExcepcaoPersistencia {

        executionPeriod = getExecutionPeriod(executionPeriod);

        calculateStudentAcumulatedEnrollments();

        List setOfCurricularCoursesToEnroll = getCommonBranchAndStudentBranchesCourses(executionPeriod);

        setOfCurricularCoursesToEnroll = initAcumulatedEnrollments(setOfCurricularCoursesToEnroll);

        List enrollmentRules = getListOfEnrollmentRules(executionPeriod, enrollmentRuleType);
        int size = enrollmentRules.size();

        for (int i = 0; i < size; i++) {
            IEnrollmentRule enrollmentRule = (IEnrollmentRule) enrollmentRules.get(i);
            setOfCurricularCoursesToEnroll = enrollmentRule.apply(setOfCurricularCoursesToEnroll);
            if (setOfCurricularCoursesToEnroll.isEmpty()) {
                break;
            }
        }

        return setOfCurricularCoursesToEnroll;
    }

    public Integer getMinimumNumberOfCoursesToEnroll() {
        return getStudent().getStudentKind().getMinCoursesToEnrol();
    }

    public Integer getMaximumNumberOfCoursesToEnroll() {
        return getStudent().getStudentKind().getMaxCoursesToEnrol();
    }

    public Integer getMaximumNumberOfAcumulatedEnrollments() {
        return getStudent().getStudentKind().getMaxNACToEnrol();
    }

    public int getNumberOfApprovedCurricularCourses() {

        int counter = 0;

        int size = getDegreeCurricularPlan().getCurricularCourses().size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) getDegreeCurricularPlan().getCurricularCourses().get(i);
            if (isCurricularCourseApproved(curricularCourse)) {
                counter++;
            }
        }

        return counter;
    }

    public int getNumberOfEnrolledCurricularCourses() {
        return getStudentEnrollmentsWithEnrolledState().size();
    }

    public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse) {

        List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        List result = (List) CollectionUtils.collect(studentApprovedEnrollments, new Transformer() {
            public Object transform(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;

                if (enrollment instanceof IEnrolmentInOptionalCurricularCourse) {
                    return (((IEnrolmentInOptionalCurricularCourse) enrollment).getCurricularCourseForOption());
                }
                return enrollment.getCurricularCourse();

            }
        });

        if (isThisCurricularCoursesInTheList(curricularCourse, result)) {
            return true;
        }

        int size = result.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourseDoneByStudent = (ICurricularCourse) result.get(i);
            ICurricularCourse equivalentCurricularCourse = getCurricularCourseInCurricularCourseEquivalences(
                    curricularCourseDoneByStudent);
            if ((equivalentCurricularCourse != null)
                    && (areTheseCurricularCoursesTheSame(curricularCourse, equivalentCurricularCourse))) {
                return true;
            }
        }

        List studentNotNeedToEnrollCourses = getStudentNotNeedToEnrollCurricularCourses();

        if (isThisCurricularCoursesInTheList(curricularCourse, studentNotNeedToEnrollCourses)) {
            return true;
        }

        size = studentNotNeedToEnrollCourses.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourseDoneByStudent = (ICurricularCourse) studentNotNeedToEnrollCourses.get(i);
            ICurricularCourse equivalentCurricularCourse = getCurricularCourseInCurricularCourseEquivalences(
                    curricularCourseDoneByStudent);
            if ((equivalentCurricularCourse != null)
                    && (areTheseCurricularCoursesTheSame(curricularCourse, equivalentCurricularCourse))) {
                return true;
            }
        }

        return false;
    }

    public boolean isCurricularCourseEnrolled(ICurricularCourse curricularCourse) {

        List studentEnrolledEnrollments = getStudentEnrollmentsWithEnrolledState();

        List result = (List) CollectionUtils.collect(studentEnrolledEnrollments, new Transformer() {
            public Object transform(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getCurricularCourse();
            }
        });

        return result.contains(curricularCourse);
    }

    public Integer getCurricularCourseAcumulatedEnrollments(ICurricularCourse curricularCourse) {

        String key = curricularCourse.getCurricularCourseUniqueKeyForEnrollment();

        Integer curricularCourseAcumulatedEnrolments = (Integer) getAcumulatedEnrollmentsMap().get(key);

        if (curricularCourseAcumulatedEnrolments == null) {
            curricularCourseAcumulatedEnrolments = new Integer(0);
        }

        if (curricularCourseAcumulatedEnrolments.intValue() >= curricularCourse.getMinimumValueForAcumulatedEnrollments()
                .intValue()) {
            curricularCourseAcumulatedEnrolments = curricularCourse.getMaximumValueForAcumulatedEnrollments();
        }

        if (curricularCourseAcumulatedEnrolments.intValue() == 0) {
            curricularCourseAcumulatedEnrolments = curricularCourse.getMinimumValueForAcumulatedEnrollments();
        }

        return curricularCourseAcumulatedEnrolments;
    }

    public List getAllStudentEnrolledEnrollments() {
        return initAcumulatedEnrollments(getStudentEnrollmentsWithEnrolledState());
    }

    public List getAllStudentEnrolledEnrollmentsInExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

        final IExecutionPeriod executionPeriod2Compare = getExecutionPeriod(executionPeriod);

        return initAcumulatedEnrollments((List) CollectionUtils.select(getStudentEnrollmentsWithEnrolledState(), new Predicate() {

            public boolean evaluate(Object arg0) {

                return ((IEnrollment) arg0).getExecutionPeriod().equals(executionPeriod2Compare);
            }
        }));
    }

    public List getStudentTemporarilyEnrolledEnrollments() {

        return initAcumulatedEnrollments((List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return (enrollment.getEnrolmentState().equals(EnrolmentState.ENROLED) && enrollment.getCondition().equals(
                        EnrollmentCondition.TEMPORARY));
            }
        }));
    }

    public CurricularCourseEnrollmentType getCurricularCourseEnrollmentType(ICurricularCourse curricularCourse,
            IExecutionPeriod currentExecutionPeriod) throws ExcepcaoPersistencia {

        if (isCurricularCourseApproved(curricularCourse)) {
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        if (!curricularCourse.hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester())) {
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        List enrollmentsWithEnrolledStateInCurrentExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(
                currentExecutionPeriod);

        List result = (List) CollectionUtils.collect(enrollmentsWithEnrolledStateInCurrentExecutionPeriod, new Transformer() {
            public Object transform(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getCurricularCourse();
            }
        });

        if (result.contains(curricularCourse)) {
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(
                currentExecutionPeriod.getPreviousExecutionPeriod());

        result = (List) CollectionUtils.collect(enrollmentsWithEnrolledStateInPreviousExecutionPeriod, new Transformer() {
            public Object transform(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getCurricularCourse();
            }
        });

        if (result.contains(curricularCourse)) {
            return CurricularCourseEnrollmentType.TEMPORARY;
        }

        return CurricularCourseEnrollmentType.DEFINITIVE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IStudentCurricularPlan#areNewAreasCompatible(Dominio.IBranch,
     *      Dominio.IBranch)
     */
    public boolean areNewAreasCompatible(IBranch specializationArea,
            IBranch secundaryArea) throws ExcepcaoPersistencia,
            BothAreasAreTheSameServiceException, InvalidArgumentsServiceException {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IStudentCurricularPlan#getCanChangeSpecializationArea()
     */
    public boolean getCanChangeSpecializationArea() {
        if (getBranch() != null) {
            return false;
        }
        return true;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    protected void calculateStudentAcumulatedEnrollments() {

        List enrollments = getAllEnrollmentsExceptTheOnesWithEnrolledState();

        List curricularCourses = (List) CollectionUtils.collect(enrollments, new Transformer() {
            public Object transform(Object obj) {
                ICurricularCourse curricularCourse = ((IEnrollment) obj).getCurricularCourse();
                return curricularCourse.getCurricularCourseUniqueKeyForEnrollment();
            }
        });

        setAcumulatedEnrollmentsMap(CollectionUtils.getCardinalityMap(curricularCourses));
    }

    protected CurricularCourse2Enroll transformToCurricularCourse2Enroll(ICurricularCourse curricularCourse,
            IExecutionPeriod currentExecutionPeriod) throws ExcepcaoPersistencia {
        return new CurricularCourse2Enroll(curricularCourse, getCurricularCourseEnrollmentType(
                curricularCourse, currentExecutionPeriod));
    }

    protected List initAcumulatedEnrollments(List elements) {

        List result = new ArrayList();
        int size = elements.size();

        for (int i = 0; i < size; i++) {
            try {
                IEnrollment enrollment = (IEnrollment) elements.get(i);
                enrollment.setAccumulatedWeight(getCurricularCourseAcumulatedEnrollments(enrollment.getCurricularCourse()));
                result.add(enrollment);
            } catch (ClassCastException e) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) elements.get(i);
                curricularCourse2Enroll.setAccumulatedWeight(getCurricularCourseAcumulatedEnrollments(curricularCourse2Enroll
                        .getCurricularCourse()));
                result.add(curricularCourse2Enroll);
            }
        }

        return result;
    }

    protected ICurricularCourse getCurricularCourseInCurricularCourseEquivalences(final ICurricularCourse curricularCourse) {

        List curricularCourseEquivalences = getDegreeCurricularPlan().getCurricularCourseEquivalences();

        ICurricularCourseEquivalence curricularCourseEquivalenceFound = (ICurricularCourseEquivalence) CollectionUtils.find(
            curricularCourseEquivalences, new Predicate() {
                public boolean evaluate(Object obj) {
                    ICurricularCourseEquivalence curricularCourseEquivalence = (ICurricularCourseEquivalence) obj;
                    return areTheseCurricularCoursesTheSame(curricularCourseEquivalence.getOldCurricularCourse(),
                                curricularCourse);
                }
            });

        if (curricularCourseEquivalenceFound != null) {
            return curricularCourseEquivalenceFound.getEquivalentCurricularCourse();
        }

        return null;
    }

    protected boolean areTheseCurricularCoursesTheSame(ICurricularCourse curricularCourse1, ICurricularCourse curricularCourse2) {

        return curricularCourse1.getCurricularCourseUniqueKeyForEnrollment().equals(
                curricularCourse2.getCurricularCourseUniqueKeyForEnrollment());
    }

    protected boolean isThisCurricularCoursesInTheList(final ICurricularCourse curricularCourse, List curricularCourses) {

        ICurricularCourse curricularCourseFound = (ICurricularCourse) CollectionUtils.find(curricularCourses, new Predicate() {
            public boolean evaluate(Object obj) {
                ICurricularCourse curricularCourseToCompare = (ICurricularCourse) obj;
                return curricularCourseToCompare.getCurricularCourseUniqueKeyForEnrollment().equals(
                        curricularCourse.getCurricularCourseUniqueKeyForEnrollment());
            }
        });

        return (curricularCourseFound != null);
    }

    protected List getStudentEnrollmentsWithApprovedState() {

        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getEnrolmentState().equals(EnrolmentState.APROVED);
            }
        });
    }

    protected List getStudentEnrollmentsWithEnrolledState() {

        return (List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getEnrolmentState().equals(EnrolmentState.ENROLED);
            }
        });
    }

//    protected List getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment() {
//
//        List studentEnrolledEnrollments = getStudentEnrollmentsWithEnrolledState();
//
//        final List result = (List) CollectionUtils.collect(studentEnrolledEnrollments, new Transformer() {
//            public Object transform(Object obj) {
//                IEnrollment enrollment = (IEnrollment) obj;
//                String key = enrollment.getCurricularCourse().getCurricularCourseUniqueKeyForEnrollment();
//                return (key);
//            }
//        });
//
//        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
//            public boolean evaluate(Object obj) {
//                IEnrollment enrollment = (IEnrollment) obj;
//                String key = enrollment.getCurricularCourse().getCurricularCourseUniqueKeyForEnrollment();
//                return result.contains(key);
//            }
//        });
//    }

    protected List getAllEnrollmentsExceptTheOnesWithEnrolledState() {

        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return !enrollment.getEnrolmentState().equals(EnrolmentState.ENROLED);
            }
        });
    }

    protected Map getAcumulatedEnrollmentsMap() {
        return acumulatedEnrollments;
    }

    protected void setAcumulatedEnrollmentsMap(Map acumulatedEnrollments) {
        this.acumulatedEnrollments = acumulatedEnrollments;
    }

    protected List getListOfEnrollmentRules(IExecutionPeriod executionPeriod, EnrollmentRuleType enrollmentRuleType) {
        return getDegreeCurricularPlan().getListOfEnrollmentRules(this, executionPeriod, enrollmentRuleType);
    }

    protected List getStudentNotNeedToEnrollCurricularCourses() {

        return (List) CollectionUtils.collect(getNotNeedToEnrollCurricularCourses(), new Transformer() {
            public Object transform(Object obj) {
                INotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (INotNeedToEnrollInCurricularCourse) obj;
                return notNeedToEnrollInCurricularCourse.getCurricularCourse();
            }
        });
    }

    protected List getCommonBranchAndStudentBranchesCourses(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

        List curricularCourses = new ArrayList();
        IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        List commonAreas = degreeCurricularPlan.getCommonAreas();
        int commonAreasSize = commonAreas.size();

        for (int i = 0; i < commonAreasSize; i++) {
            IBranch area = (IBranch) commonAreas.get(i);
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(area, AreaType.BASE_OBJ));
        }

        if (getBranch() != null) {
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(getBranch(),
                    AreaType.SPECIALIZATION_OBJ));
        }

        if (getSecundaryBranch() != null) {
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(getSecundaryBranch(),
                    AreaType.SECONDARY_OBJ));
        }

        List result = new ArrayList();
        int curricularCoursesSize = curricularCourses.size();

        for (int i = 0; i < curricularCoursesSize; i++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourses.get(i);
            result.add(transformToCurricularCourse2Enroll(curricularCourse, executionPeriod));
        }

        List elementsToRemove = (List) CollectionUtils.select(result, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                return curricularCourse2Enroll.getEnrollmentType().equals(CurricularCourseEnrollmentType.NOT_ALLOWED);
            }
        });

        result.removeAll(elementsToRemove);

        return result;
    }

    protected IExecutionPeriod getExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

        IExecutionPeriod executionPeriod2Return = executionPeriod;

        if (executionPeriod == null) {
            ISuportePersistente daoFactory = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = daoFactory.getIPersistentExecutionPeriod();
            executionPeriod2Return = executionPeriodDAO.readActualExecutionPeriod();
        }

        return executionPeriod2Return;
    }

    /* (non-Javadoc)
     * @see Dominio.IStudentCurricularPlan#getCreditsInSecundaryArea()
     */
    public Integer getCreditsInSecundaryArea() {        
        return new Integer(0);
    }

    /* (non-Javadoc)
     * @see Dominio.IStudentCurricularPlan#setCreditsInSecundaryArea(java.lang.Integer)
     */
    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
        // do nothing
     }

    /* (non-Javadoc)
     * @see Dominio.IStudentCurricularPlan#getCreditsInSpecializationArea()
     */
    public Integer getCreditsInSpecializationArea() {       
        return new Integer(0);
    }

    /* (non-Javadoc)
     * @see Dominio.IStudentCurricularPlan#setCreditsInSpecializationArea(java.lang.Integer)
     */
    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
        //do nothing
    }

    /* (non-Javadoc)
     * @see Dominio.IStudentCurricularPlan#getCurricularCoursesToEnroll(Dominio.IExecutionYear, Util.enrollment.EnrollmentRuleType)
     */
    public List getCurricularCoursesToEnrollInExecutionYear(IExecutionYear executionYear, EnrollmentRuleType enrollmentRuleType) throws ExcepcaoPersistencia {
            List result = new ArrayList();
            List executionPeriods=null;
        if (executionYear==null){
            executionPeriods = getExecutionPeriod(null).getExecutionYear().getExecutionPeriods();
        } else {
            executionPeriods = executionYear.getExecutionPeriods();
        }
            Iterator iter = executionPeriods.iterator();
            while (iter.hasNext()){
                result.addAll(getCurricularCoursesToEnroll((IExecutionPeriod) iter.next(),enrollmentRuleType));
            }
        return result;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------
}