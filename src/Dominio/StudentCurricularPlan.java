package Dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import Dominio.degree.enrollment.rules.IEnrollmentRule;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AreaType;
import Util.EnrolmentState;
import Util.Specialization;
import Util.StudentCurricularPlanState;
import Util.enrollment.EnrollmentCondition;
import Util.enrollment.EnrollmentRuleType;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlan extends DomainObject implements
		IStudentCurricularPlan {
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
					&& getDegreeCurricularPlan().equals(
							studentCurricularPlan.getDegreeCurricularPlan())
					&& getCurrentState().equals(
							studentCurricularPlan.getCurrentState());
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
			result += "employee = " + this.employee.getPerson().getNome()
					+ "]\n";
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

	public void setDegreeCurricularPlan(
			IDegreeCurricularPlan degreeCurricularPlan) {
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

	public void setNotNeedToEnrollCurricularCourses(
			List notNeedToEnrollCurricularCourses) {
		this.notNeedToEnrollCurricularCourses = notNeedToEnrollCurricularCourses;
	}

	public List getCurricularCoursesToEnroll(IExecutionPeriod executionPeriod, EnrollmentRuleType enrollmentRuleType)
            throws ExcepcaoPersistencia {
        executionPeriod = getExecutionPeriod(executionPeriod);
        List setOfCurricularCoursesToEnroll = this.getCommonBranchAndStudentBranchesCourses(executionPeriod.getSemester());
        List enrollmentRules = this.getListOfEnrollmentRules(executionPeriod, enrollmentRuleType);
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
		return this.getStudent().getStudentKind().getMinCoursesToEnrol();
	}

	public Integer getMaximumNumberOfCoursesToEnroll() {
		return this.getStudent().getStudentKind().getMaxCoursesToEnrol();
	}

	public Integer getMaximumNumberOfAcumulatedEnrollments() {
		return this.getStudent().getStudentKind().getMaxNACToEnrol();
	}

	public int getNumberOfApprovedCurricularCourses() {
		int a = this.getStudentApprovedEnrollments().size();

		int b = this.getStudentNotNeedToEnrollCourses().size();

		// TODO [DAVID]: Still missing to check if the student has any approved
		// enrollment
		// in a curricular course with a degree curricular plan equivalence.

		return a + b;
	}

	public int getNumberOfEnrolledCurricularCourses() {
		return this.getStudentEnrolledEnrollments().size();
	}

	public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse) {
		List studentApprovedEnrollments = this.getStudentApprovedEnrollments();

		List result = (List) CollectionUtils.collect(
				studentApprovedEnrollments, new Transformer() {
					public Object transform(Object obj) {
						IEnrollment enrollment = (IEnrollment) obj;

						if (enrollment instanceof IEnrolmentInOptionalCurricularCourse) {
							return (((IEnrolmentInOptionalCurricularCourse) enrollment)
									.getCurricularCourseForOption());
						} else {
							return enrollment.getCurricularCourse();
						}
					}
				});

		if (result.contains(curricularCourse)) {
			return true;
		}

		List studentNotNeedToEnrollCourses = this
				.getStudentNotNeedToEnrollCourses();

		if (studentNotNeedToEnrollCourses.contains(curricularCourse)) {
			return true;
		}

		// TODO [DAVID]: Still missing to check if the student has any approved
		// enrollment
		// in a curricular course with a degree curricular plan equivalence.

		return false;
	}

	public boolean isCurricularCourseEnrolled(ICurricularCourse curricularCourse) {
		List studentEnrolledEnrollments = this.getStudentEnrolledEnrollments();

		List result = (List) CollectionUtils.collect(
				studentEnrolledEnrollments, new Transformer() {
					public Object transform(Object obj) {
						IEnrollment enrollment = (IEnrollment) obj;
						return enrollment.getCurricularCourse();
					}
				});

		if (result.contains(curricularCourse)) {
			return true;
		}

		return false;
	}

	public void calculateStudentAcumulatedEnrollments() {
		List enrollments = this
				.getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment();

		List curricularCourses = (List) CollectionUtils.collect(enrollments,
				new Transformer() {
					public Object transform(Object obj) {
						ICurricularCourse curricularCourse = ((IEnrollment) obj)
								.getCurricularCourse();
						return curricularCourse
								.getCurricularCourseUniqueKeyForEnrollment();
					}
				});

		setAcumulatedEnrollmentsMap(CollectionUtils
				.getCardinalityMap(curricularCourses));
	}

	public Integer getCurricularCourseAcumulatedEnrolments(
			ICurricularCourse curricularCourse) {
		String key = curricularCourse
				.getCurricularCourseUniqueKeyForEnrollment();

		Integer curricularCourseAcumulatedEnrolments = (Integer) this
				.getAcumulatedEnrollmentsMap().get(key);

		if (curricularCourseAcumulatedEnrolments == null) {
			curricularCourseAcumulatedEnrolments = new Integer(0);
		}

		return curricularCourseAcumulatedEnrolments;
	}

	public List getStudentApprovedEnrollments() {
		return (List) CollectionUtils.select(this.getEnrolments(),
				new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrollment enrollment = (IEnrollment) obj;
						return enrollment.getEnrolmentState().equals(
								EnrolmentState.APROVED);
					}
				});
	}

	public List getStudentEnrolledEnrollments() {
		return (List) CollectionUtils.select(this.getEnrolments(),
				new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrollment enrollment = (IEnrollment) obj;
						return enrollment.getEnrolmentState().equals(
								EnrolmentState.ENROLED);
					}
				});
	}

    public List getStudentEnrolledEnrollmentsInExecutionPeriod(
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        final IExecutionPeriod executionPeriod2Compare = getExecutionPeriod(executionPeriod);

        return (List) CollectionUtils.select(this
                .getStudentEnrolledEnrollments(), new Predicate() {

            public boolean evaluate(Object arg0) {

                return ((IEnrollment) arg0).getExecutionPeriod().equals(
                        executionPeriod2Compare);
            }
        });
    }

	public List getStudentTemporarilyEnrolledEnrollments() {
		return (List) CollectionUtils.select(this.getEnrolments(),
				new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrollment enrollment = (IEnrollment) obj;
						return (enrollment.getEnrolmentState().equals(
								EnrolmentState.APROVED) && enrollment
								.getCondition().equals(
										EnrollmentCondition.TEMPORARY));
					}
				});
	}

	protected List getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment() {
		List studentEnrolledEnrollments = this.getStudentEnrolledEnrollments();

		final List result = (List) CollectionUtils.collect(
				studentEnrolledEnrollments, new Transformer() {
					public Object transform(Object obj) {
						IEnrollment enrollment = (IEnrollment) obj;
						String key = enrollment.getCurricularCourse()
								.getCurricularCourseUniqueKeyForEnrollment();
						return (key);
					}
				});

		return (List) CollectionUtils.select(this.getEnrolments(),
				new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrollment enrollment = (IEnrollment) obj;
						String key = enrollment.getCurricularCourse()
								.getCurricularCourseUniqueKeyForEnrollment();
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

	protected List getListOfEnrollmentRules(IExecutionPeriod executionPeriod,
			EnrollmentRuleType enrollmentRuleType) {
		return this.getDegreeCurricularPlan().getListOfEnrollmentRules(this,
				executionPeriod, enrollmentRuleType);
	}

	protected List getStudentNotNeedToEnrollCourses() {
		return (List) CollectionUtils.collect(this
				.getNotNeedToEnrollCurricularCourses(), new Transformer() {
			public Object transform(Object obj) {
				INotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (INotNeedToEnrollInCurricularCourse) obj;
				return notNeedToEnrollInCurricularCourse.getCurricularCourse();
			}
		});
	}

	protected List getCommonBranchAndStudentBranchesCourses(
			final Integer semester) {
		List curricularCourses = new ArrayList();

		List commonAreas = this.getDegreeCurricularPlan().getCommonAreas();

		for (int i = 0; i < commonAreas.size(); i++) {
			IBranch area = (IBranch) commonAreas.get(i);
			curricularCourses.addAll(this.getDegreeCurricularPlan()
					.getCurricularCoursesFromArea(area, AreaType.BASE_OBJ));
		}

		if (this.getBranch() != null) {
			curricularCourses.addAll(this.getDegreeCurricularPlan()
					.getCurricularCoursesFromArea(this.getBranch(),
							AreaType.SPECIALIZATION_OBJ));
		}

		if (this.getSecundaryBranch() != null) {
			curricularCourses.addAll(this.getDegreeCurricularPlan()
					.getCurricularCoursesFromArea(this.getSecundaryBranch(),
							AreaType.SECONDARY_OBJ));
		}

		List curricularCoursesToRemove = (List) CollectionUtils.select(
				curricularCourses, new Predicate() {
					public boolean evaluate(Object obj) {
						ICurricularCourse curricularCourse = (ICurricularCourse) obj;
						return (isCurricularCourseApproved(curricularCourse)
								|| isCurricularCourseEnrolled(curricularCourse) || !curricularCourse
								.hasActiveScopeInGivenSemester(semester));
					}
				});

		curricularCourses.removeAll(curricularCoursesToRemove);

		return curricularCourses;
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