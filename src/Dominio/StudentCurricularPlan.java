package Dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import Dominio.enrollment.CurricularCourse2Enroll;
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
    // BEGIN: Only for enrollment purposes
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
        allEnrollments.addAll(pastStudentCurricularPlan.getEnrolments());
        allEnrollments.addAll(getEnrolments());
        
        return allEnrollments;
    }

    public List getCurricularCoursesToEnroll(IExecutionPeriod executionPeriod, EnrollmentRuleType enrollmentRuleType)
        throws ExcepcaoPersistencia {

        executionPeriod = getExecutionPeriod(executionPeriod);

        List setOfCurricularCoursesToEnroll = getCommonBranchAndStudentBranchesCourses(executionPeriod);
        
        calculateStudentAcumulatedEnrollments();
        
        setOfCurricularCoursesToEnroll = initAcumulatedEnrollments(setOfCurricularCoursesToEnroll);

//        List enrollmentRules = getListOfEnrollmentRules(executionPeriod, enrollmentRuleType);
//        int size = enrollmentRules.size();
//
//        for (int i = 0; i < size; i++) {
//            IEnrollmentRule enrollmentRule = (IEnrollmentRule) enrollmentRules.get(i);
//            setOfCurricularCoursesToEnroll = enrollmentRule.apply(setOfCurricularCoursesToEnroll);
//            if (setOfCurricularCoursesToEnroll.isEmpty()) {
//                break;
//            }
//        }

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
        return getAllStudentApprovedEnrollments().size() + getStudentNotNeedToEnrollCurricularCourses().size();
    }

    public int getNumberOfEnrolledCurricularCourses() {
        return getAllStudentEnrolledEnrollments().size();
    }

    public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse) {

        List studentApprovedEnrollments = getAllStudentApprovedEnrollments();

        List result = (List) CollectionUtils.collect(studentApprovedEnrollments, new Transformer() {
            public Object transform(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;

                if (enrollment instanceof IEnrolmentInOptionalCurricularCourse) {
                    return (((IEnrolmentInOptionalCurricularCourse) enrollment).getCurricularCourseForOption());
                } else {
                    return enrollment.getCurricularCourse();
                }
            }
        });

        if (result.contains(curricularCourse)) {
            return true;
        }

        List studentNotNeedToEnrollCourses = getStudentNotNeedToEnrollCurricularCourses();

        if (studentNotNeedToEnrollCourses.contains(curricularCourse)) {
            return true;
        }

        return false;
    }

    public boolean isCurricularCourseEnrolled(ICurricularCourse curricularCourse) {

        List studentEnrolledEnrollments = getAllStudentEnrolledEnrollments();

        List result = (List) CollectionUtils.collect(studentEnrolledEnrollments, new Transformer() {
            public Object transform(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getCurricularCourse();
            }
        });

        return result.contains(curricularCourse);
    }

    public void calculateStudentAcumulatedEnrollments() {

        List enrollments = getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment();

        List curricularCourses = (List) CollectionUtils.collect(enrollments, new Transformer() {
            public Object transform(Object obj) {
                ICurricularCourse curricularCourse = ((IEnrollment) obj).getCurricularCourse();
                return curricularCourse.getCurricularCourseUniqueKeyForEnrollment();
            }
        });

        setAcumulatedEnrollmentsMap(CollectionUtils.getCardinalityMap(curricularCourses));
    }

    public Integer getCurricularCourseAcumulatedEnrollments(ICurricularCourse curricularCourse) {
        
        String key = curricularCourse.getCurricularCourseUniqueKeyForEnrollment();

        Integer curricularCourseAcumulatedEnrolments = (Integer) getAcumulatedEnrollmentsMap().get(key);

        if (curricularCourseAcumulatedEnrolments == null) {
            curricularCourseAcumulatedEnrolments = new Integer(0);
        }

        return curricularCourseAcumulatedEnrolments;
    }

    public List getAllStudentEnrolledEnrollments() {

        return (List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getEnrolmentState().equals(EnrolmentState.ENROLED);
            }
        });
    }

    public List getAllStudentEnrolledEnrollmentsInExecutionPeriod(IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {

        final IExecutionPeriod executionPeriod2Compare = getExecutionPeriod(executionPeriod);

        return (List) CollectionUtils.select(getAllStudentEnrolledEnrollments(), new Predicate() {

            public boolean evaluate(Object arg0) {

                return ((IEnrollment) arg0).getExecutionPeriod().equals(executionPeriod2Compare);
            }
        });
    }

    public List getStudentTemporarilyEnrolledEnrollments() {

        return (List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return (enrollment.getEnrolmentState().equals(EnrolmentState.ENROLED) && enrollment.getCondition().equals(
                        EnrollmentCondition.TEMPORARY));
            }
        });
    }

    public CurricularCourse2Enroll transformToCurricularCourse2Enroll(ICurricularCourse curricularCourse,
        IExecutionPeriod currentExecutionPeriod) throws ExcepcaoPersistencia {

        if (isCurricularCourseApproved(curricularCourse)) {
            return new CurricularCourse2Enroll(curricularCourse, CurricularCourseEnrollmentType.NOT_ALLOWED);
        }
        
        if (!curricularCourse.hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester())) {
            return new CurricularCourse2Enroll(curricularCourse, CurricularCourseEnrollmentType.NOT_ALLOWED);
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
            return new CurricularCourse2Enroll(curricularCourse, CurricularCourseEnrollmentType.NOT_ALLOWED);
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
            return new CurricularCourse2Enroll(curricularCourse, CurricularCourseEnrollmentType.TEMPORARY);
        }

        return new CurricularCourse2Enroll(curricularCourse, CurricularCourseEnrollmentType.DEFINITIVE);
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

    protected List getAllStudentApprovedEnrollments() {

        List allEnrollments = getStudentEnrollmentsWithApprovedState();
        int enrollmentsSize = allEnrollments.size();

        List curricularCourseEquivalences = getDegreeCurricularPlan().getCurricularCourseEquivalences();
        int curricularCourseEquivalencesSize = curricularCourseEquivalences.size();

        if (curricularCourseEquivalencesSize == 0) {
            return allEnrollments;
        }

        List result = new ArrayList();
        result.addAll(allEnrollments);

        for (int i = 0; i < enrollmentsSize; i++) {

            IEnrollment enrollment = (IEnrollment) allEnrollments.get(i);

            ICurricularCourse curricularCourse = null;
            if (enrollment instanceof IEnrolmentInOptionalCurricularCourse) {
                curricularCourse = (((IEnrolmentInOptionalCurricularCourse) enrollment).getCurricularCourseForOption());
            } else {
                curricularCourse = enrollment.getCurricularCourse();
            }

            for (int j = 0; j < curricularCourseEquivalencesSize; j++) {

                ICurricularCourseEquivalence curricularCourseEquivalence = (ICurricularCourseEquivalence)
                    curricularCourseEquivalences.get(j);

                if (curricularCourseEquivalence.getOldCurricularCourse().equals(curricularCourse)) {
                    IEnrollment virtualEnrollment = new Enrolment();
                    virtualEnrollment.setCurricularCourse(curricularCourseEquivalence.getEquivalentCurricularCourse());
                    virtualEnrollment.setCreationDate(enrollment.getCreationDate());
                    virtualEnrollment.setEnrolmentEvaluationType(enrollment.getEnrolmentEvaluationType());
                    virtualEnrollment.setEnrolmentState(enrollment.getEnrolmentState());
                    virtualEnrollment.setEvaluations(enrollment.getEvaluations());
                    virtualEnrollment.setExecutionPeriod(enrollment.getExecutionPeriod());
                    virtualEnrollment.setStudentCurricularPlan(this);
                    result.remove(enrollment);
                    result.add(virtualEnrollment);
                }
            }
        }

        return result;
    }

    protected List getStudentEnrollmentsWithApprovedState() {

        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                return enrollment.getEnrolmentState().equals(EnrolmentState.APROVED);
            }
        });
    }

    protected List getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment() {
        
        List studentEnrolledEnrollments = getAllStudentEnrolledEnrollments();

        final List result = (List) CollectionUtils.collect(studentEnrolledEnrollments, new Transformer() {
            public Object transform(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                String key = enrollment.getCurricularCourse().getCurricularCourseUniqueKeyForEnrollment();
                return (key);
            }
        });

        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                IEnrollment enrollment = (IEnrollment) obj;
                String key = enrollment.getCurricularCourse().getCurricularCourseUniqueKeyForEnrollment();
                return result.contains(key);
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

        List commonAreas = getDegreeCurricularPlan().getCommonAreas();
        int commonAreasSize = commonAreas.size();

        for (int i = 0; i < commonAreasSize; i++) {
            IBranch area = (IBranch) commonAreas.get(i);
            curricularCourses.addAll(getDegreeCurricularPlan().getCurricularCoursesFromArea(area, AreaType.BASE_OBJ));
        }

        if (getBranch() != null) {
            curricularCourses.addAll(getDegreeCurricularPlan().getCurricularCoursesFromArea(getBranch(),
                    AreaType.SPECIALIZATION_OBJ));
        }

        if (getSecundaryBranch() != null) {
            curricularCourses.addAll(getDegreeCurricularPlan().getCurricularCoursesFromArea(getSecundaryBranch(),
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

    // -------------------------------------------------------------
    // END: Only for enrollment purposes
    // -------------------------------------------------------------
}