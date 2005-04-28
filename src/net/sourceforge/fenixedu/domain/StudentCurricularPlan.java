package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.cache.EnrollmentInfoCacheOSCacheImpl;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.IEnrollmentRule;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.util.AreaType;
import net.sourceforge.fenixedu.util.BranchType;
import net.sourceforge.fenixedu.util.Specialization;
import net.sourceforge.fenixedu.util.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.enrollment.EnrollmentCondition;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlan extends StudentCurricularPlan_Base {

	protected StudentCurricularPlanState currentState;

	protected Specialization specialization;

	// For enrollment purposes only
	protected Map acumulatedEnrollments;

	protected List enrollmentReports;

	public StudentCurricularPlan() {
		setOjbConcreteClass(getClass().getName());
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof IStudentCurricularPlan) {
			IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) obj;
			result = getIdInternal().equals(
					studentCurricularPlan.getIdInternal());
		}

		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "internalCode = " + getIdInternal() + "; ";
		result += "student = " + this.getStudent() + "; ";
		result += "degreeCurricularPlan = " + this.getDegreeCurricularPlan() + "; ";
		result += "startDate = " + this.getStartDate() + "; ";
		result += "specialization = " + this.specialization + "; ";
		result += "currentState = " + this.currentState + "]\n";
		result += "when alter = " + this.getWhen() + "]\n";
		if (this.getEmployee() != null) {
			result += "employee = " + this.getEmployee().getPerson().getNome()
					+ "]\n";
		}
		return result;
	}

	public StudentCurricularPlanState getCurrentState() {
		return currentState;
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

	public void setCurrentState(StudentCurricularPlanState currentState) {
		this.currentState = currentState;
	}

	public void setSecundaryBranch(IBranch secundaryBranch) {
	}

	public void setSecundaryBranchKey(Integer secundaryBranchKey) {
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes (PUBLIC)
	// -------------------------------------------------------------

	public List getAllEnrollments() {

		IStudentCurricularPlan pastStudentCurricularPlan = (IStudentCurricularPlan) CollectionUtils
				.find(getStudent().getStudentCurricularPlans(),
						new Predicate() {
							public boolean evaluate(Object obj) {
								IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) obj;
								return studentCurricularPlan
										.getCurrentState()
										.equals(
												StudentCurricularPlanState.PAST_OBJ);
							}
						});

		List allEnrollments = new ArrayList();

		if (pastStudentCurricularPlan != null) {
			allEnrollments.addAll(pastStudentCurricularPlan.getEnrolments());
		}

		allEnrollments.addAll(getEnrolments());

		return (List) CollectionUtils.select(allEnrollments, new Predicate() {

			public boolean evaluate(Object arg0) {
				IEnrolment enrollment = (IEnrolment) arg0;
				return !enrollment.getCondition().equals(
						EnrollmentCondition.INVISIBLE);
			}
		});
	}

	public List getCurricularCoursesToEnroll(IExecutionPeriod executionPeriod)
			throws FenixDomainException {

		calculateStudentAcumulatedEnrollments();

		List setOfCurricularCoursesToEnroll = getCommonBranchAndStudentBranchesCourses(executionPeriod);

		setOfCurricularCoursesToEnroll = initAcumulatedEnrollments(setOfCurricularCoursesToEnroll);

		List enrollmentRules = getListOfEnrollmentRules(executionPeriod);
		int size = enrollmentRules.size();

		for (int i = 0; i < size; i++) {
			IEnrollmentRule enrollmentRule = (IEnrollmentRule) enrollmentRules
					.get(i);
			setOfCurricularCoursesToEnroll = enrollmentRule
					.apply(setOfCurricularCoursesToEnroll);
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
			ICurricularCourse curricularCourse = (ICurricularCourse) getDegreeCurricularPlan()
					.getCurricularCourses().get(i);
			if (isCurricularCourseApproved(curricularCourse)) {
				counter++;
			}
		}

		return counter;
	}

	public int getNumberOfEnrolledCurricularCourses() {
		return getStudentEnrollmentsWithEnrolledState().size();
	}
    
    protected boolean isApproved(ICurricularCourse curricularCourse, List approvedCourses) {
        if (isThisCurricularCoursesInTheList(curricularCourse, approvedCourses)) {
            return true;
        }

        int size = approvedCourses.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourseDoneByStudent = (ICurricularCourse) approvedCourses
                    .get(i);
            List curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(curricularCourseDoneByStudent);
            if (curricularCourseEquivalences.contains(curricularCourse)) {
                return true;
            }
        }

        List studentNotNeedToEnrollCourses = getStudentNotNeedToEnrollCurricularCourses();

        if (isThisCurricularCoursesInTheList(curricularCourse,
                studentNotNeedToEnrollCourses)) {
            return true;
        }

        size = studentNotNeedToEnrollCourses.size();
        for (int i = 0; i < size; i++) {
            ICurricularCourse curricularCourseDoneByStudent = (ICurricularCourse) studentNotNeedToEnrollCourses
                    .get(i);
            List curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(curricularCourseDoneByStudent);
            if (curricularCourseEquivalences.contains(curricularCourse)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean isCurricularCourseApprovedInCurrentOrPreviousPeriod(final ICurricularCourse course, final IExecutionPeriod executionPeriod) {
        final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();
        final List approvedCurricularCourses = new ArrayList();
        
        for (Iterator iter = studentApprovedEnrollments.iterator(); iter.hasNext();) {
            IEnrolment enrolment = (IEnrolment) iter.next();
            if(enrolment.getExecutionPeriod().compareTo(executionPeriod) <= 0) {
                approvedCurricularCourses.add(enrolment.getCurricularCourse());
            }
        }
        
        return isApproved(course, approvedCurricularCourses);

    }
    
    public boolean isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(final ICurricularCourse course, final IExecutionPeriod executionPeriod) {
        final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        IEnrolment enrolment = (IEnrolment) CollectionUtils.find(studentApprovedEnrollments, new Predicate() {
            public boolean evaluate(Object arg0) {
                IEnrolment enrolment = (IEnrolment) arg0;
                if((enrolment.getCurricularCourse().getIdInternal().equals(course.getIdInternal())) 
                        && (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) 
                        && (enrolment.getExecutionPeriod().compareTo(executionPeriod) <= 0)){
                    return true;
                }
                return false;
        }});
        
        if(enrolment != null) 
            return true;
        return false;
    }

	public boolean isCurricularCourseApproved(ICurricularCourse curricularCourse) {

		List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

		List result = (List) CollectionUtils.collect(
				studentApprovedEnrollments, new Transformer() {
					public Object transform(Object obj) {
						IEnrolment enrollment = (IEnrolment) obj;

						return enrollment.getCurricularCourse();

					}
				});
        
        return isApproved(curricularCourse, result);

	}
    

	public boolean isCurricularCourseEnrolled(ICurricularCourse curricularCourse) {

		List studentEnrolledEnrollments = getStudentEnrollmentsWithEnrolledState();

		List result = (List) CollectionUtils.collect(
				studentEnrolledEnrollments, new Transformer() {
					public Object transform(Object obj) {
						IEnrolment enrollment = (IEnrolment) obj;
						return enrollment.getCurricularCourse();
					}
				});

		return result.contains(curricularCourse);
	}

	public boolean isCurricularCourseEnrolledInExecutionPeriod(
			ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {

		List studentEnrolledEnrollments = getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionPeriod);

		List result = (List) CollectionUtils.collect(
				studentEnrolledEnrollments, new Transformer() {
					public Object transform(Object obj) {
						IEnrolment enrollment = (IEnrolment) obj;
						return enrollment.getCurricularCourse();
					}
				});

		return result.contains(curricularCourse);
	}

	public Integer getCurricularCourseAcumulatedEnrollments(
			ICurricularCourse curricularCourse) {

		String key = curricularCourse
				.getCurricularCourseUniqueKeyForEnrollment();

		Integer curricularCourseAcumulatedEnrolments = (Integer) getAcumulatedEnrollmentsMap()
				.get(key);

		if (curricularCourseAcumulatedEnrolments == null) {
			curricularCourseAcumulatedEnrolments = new Integer(0);
		}

		if (curricularCourseAcumulatedEnrolments.intValue() >= curricularCourse
				.getMinimumValueForAcumulatedEnrollments().intValue()) {
			curricularCourseAcumulatedEnrolments = curricularCourse
					.getMaximumValueForAcumulatedEnrollments();
		}

		if (curricularCourseAcumulatedEnrolments.intValue() == 0) {
			curricularCourseAcumulatedEnrolments = curricularCourse
					.getMinimumValueForAcumulatedEnrollments();
		}

		return curricularCourseAcumulatedEnrolments;
	}

	public List getAllStudentEnrolledEnrollmentsInExecutionPeriod(
			final IExecutionPeriod executionPeriod) {

        calculateStudentAcumulatedEnrollments();
		return initAcumulatedEnrollments((List) CollectionUtils.select(
				getStudentEnrollmentsWithEnrolledState(), new Predicate() {

					public boolean evaluate(Object arg0) {

						return ((IEnrolment) arg0).getExecutionPeriod()
								.equals(executionPeriod);
					}
				}));
	}

	public List getStudentTemporarilyEnrolledEnrollments() {

		return initAcumulatedEnrollments((List) CollectionUtils.select(
				getEnrolments(), new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrolment enrollment = (IEnrolment) obj;
						return (enrollment.getEnrollmentState().equals(
								EnrollmentState.ENROLLED) && enrollment
								.getCondition().equals(
										EnrollmentCondition.TEMPORARY));
					}
				}));
	}

	public CurricularCourseEnrollmentType getCurricularCourseEnrollmentType(
			ICurricularCourse curricularCourse,
			IExecutionPeriod currentExecutionPeriod) {

		if (getBranch() == null) {
			if (!curricularCourse
					.hasActiveScopeInGivenSemester(currentExecutionPeriod
							.getSemester())) {
				return CurricularCourseEnrollmentType.NOT_ALLOWED;
			}
		} else {
			if (!curricularCourse
					.hasActiveScopeInGivenSemesterForCommonAndGivenBranch(
							currentExecutionPeriod.getSemester(), getBranch())) {
				return CurricularCourseEnrollmentType.NOT_ALLOWED;
			}
		}

		if (isCurricularCourseApproved(curricularCourse)) {
			return CurricularCourseEnrollmentType.NOT_ALLOWED;
		}

		List enrollmentsWithEnrolledStateInCurrentExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod);

		for (int i = 0; i < enrollmentsWithEnrolledStateInCurrentExecutionPeriod
				.size(); i++) {
			IEnrolment enrollment = (IEnrolment) enrollmentsWithEnrolledStateInCurrentExecutionPeriod
					.get(i);
			if (curricularCourse.equals(enrollment.getCurricularCourse())) {
				return CurricularCourseEnrollmentType.NOT_ALLOWED;
			}
		}

		List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod
				.getPreviousExecutionPeriod());

		for (int i = 0; i < enrollmentsWithEnrolledStateInPreviousExecutionPeriod
				.size(); i++) {
			IEnrolment enrollment = (IEnrolment) enrollmentsWithEnrolledStateInPreviousExecutionPeriod
					.get(i);
			if (curricularCourse.equals(enrollment.getCurricularCourse())) {
				return CurricularCourseEnrollmentType.TEMPORARY;
			}
		}

		if (isMathematicalCourse(curricularCourse)) {
			if (hasCurricularCourseEquivalenceIn(curricularCourse,
					enrollmentsWithEnrolledStateInPreviousExecutionPeriod))
				return CurricularCourseEnrollmentType.TEMPORARY;
		}

		return CurricularCourseEnrollmentType.DEFINITIVE;
	}

	protected boolean hasActiveScopeInGivenSemester(
			ICurricularCourse curricularCourse,
			IExecutionPeriod currentExecutionPeriod) {

		boolean result = true;
		List curricularCoursesFromCommonBranches = new ArrayList();
		List commonAreas = getDegreeCurricularPlan().getCommonAreas();
		int commonAreasSize = commonAreas.size();

		for (int i = 0; i < commonAreasSize; i++) {
			IBranch area = (IBranch) commonAreas.get(i);
			curricularCoursesFromCommonBranches.addAll(getDegreeCurricularPlan()
					.getCurricularCoursesFromArea(area, AreaType.BASE_OBJ));
		}
		if (!curricularCoursesFromCommonBranches.contains(curricularCourse)) {

			if (this.getBranch() != null && this.getSecundaryBranch() != null) {
				if (!curricularCourse
						.hasActiveScopeInGivenSemesterForGivenBranch(
								currentExecutionPeriod.getSemester(), this
										.getBranch())
						&& !curricularCourse
								.hasActiveScopeInGivenSemesterForGivenBranch(
										currentExecutionPeriod.getSemester(),
										this.getSecundaryBranch())) {
					result = false;
				}
			} else if (getBranch() != null && getSecundaryBranch() == null) {
				if (!curricularCourse
						.hasActiveScopeInGivenSemesterForGivenBranch(
								currentExecutionPeriod.getSemester(), this
										.getBranch())) {
					result = false;
				}
			} else if (getBranch() == null && getSecundaryBranch() != null) {
				if (!curricularCourse
						.hasActiveScopeInGivenSemesterForGivenBranch(
								currentExecutionPeriod.getSemester(), this
										.getSecundaryBranch())) {
					result = false;
				}
			} else if (getBranch() == null) {
				if (!curricularCourse
						.hasActiveScopeInGivenSemester(currentExecutionPeriod
								.getSemester())) {
					result = false;
				}
			}
		} else {
			result = curricularCourse
					.hasActiveScopeInGivenSemester(currentExecutionPeriod
							.getSemester());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Dominio.IStudentCurricularPlan#areNewAreasCompatible(Dominio.IBranch,
	 *      Dominio.IBranch)
	 */
	public boolean areNewAreasCompatible(IBranch specializationArea,
			IBranch secundaryArea) throws BothAreasAreTheSameServiceException,
			InvalidArgumentsServiceException {
		// This look stupid but it avoids a irritating warnin that the exception is not thrown
		// Performance-wise the compiler removes the whole block because it is un-reachable.
		if (false) {
			throw new BothAreasAreTheSameServiceException();
		}
		if (false) {
			throw new InvalidArgumentsServiceException();
		}		
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see Dominio.IStudentCurricularPlan#getCreditsInSecundaryArea()
	 */
	public Integer getCreditsInSecundaryArea() {
		return new Integer(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Dominio.IStudentCurricularPlan#setCreditsInSecundaryArea(java.lang.Integer)
	 */
	public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Dominio.IStudentCurricularPlan#getCreditsInSpecializationArea()
	 */
	public Integer getCreditsInSpecializationArea() {
		return new Integer(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Dominio.IStudentCurricularPlan#setCreditsInSpecializationArea(java.lang.Integer)
	 */
	public void setCreditsInSpecializationArea(
			Integer creditsInSpecializationArea) {
		// do nothing
	}

	/**
	 * @return Returns the enrollmentReports.
	 */
	public List getEnrollmentReports() {
		return enrollmentReports;
	}

	/**
	 * @param enrollmentReports
	 *            The enrollmentReports to set.
	 */
	public void setEnrollmentReports(List enrollmentReports) {
		this.enrollmentReports = enrollmentReports;
	}

	public List getAprovedEnrolmentsInExecutionPeriod(
			final IExecutionPeriod executionPeriod) {
		return (List) CollectionUtils.select(getEnrolments(), new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrollment = (IEnrolment) obj;
				if (enrollment.getEnrollmentState().equals(
						EnrollmentState.APROVED)
						&& enrollment.getExecutionPeriod().equals(
								executionPeriod))
					return true;
				return false;
			}
		});
	}

	// -------------------------------------------------------------

	// END: Only for enrollment purposes (PUBLIC)
	// -------------------------------------------------------------

	// -------------------------------------------------------------
	// BEGIN: Only for enrollment purposes (PROTECTED)
	// -------------------------------------------------------------

	protected void calculateStudentAcumulatedEnrollments() {
        if (getAcumulatedEnrollmentsMap() == null) {
            List enrollments = getAllEnrollmentsExceptTheOnesWithEnrolledState();

            List curricularCourses = (List) CollectionUtils.collect(enrollments, new Transformer() {
                public Object transform(Object obj) {
                    ICurricularCourse curricularCourse = ((IEnrolment) obj).getCurricularCourse();
                    return curricularCourse.getCurricularCourseUniqueKeyForEnrollment();
                }
            });
            setAcumulatedEnrollmentsMap(CollectionUtils.getCardinalityMap(curricularCourses));
        }
    }

	protected CurricularCourse2Enroll transformToCurricularCourse2Enroll(
			ICurricularCourse curricularCourse,
			IExecutionPeriod currentExecutionPeriod) {
		return new CurricularCourse2Enroll(curricularCourse,
				getCurricularCourseEnrollmentType(curricularCourse,
						currentExecutionPeriod), new Boolean(false));
	}

	protected List initAcumulatedEnrollments(List elements) {

		if (getAcumulatedEnrollmentsMap() != null) {
			List result = new ArrayList();
			int size = elements.size();

			for (int i = 0; i < size; i++) {
				try {
					IEnrolment enrollment = (IEnrolment) elements.get(i);
					enrollment
							.setAccumulatedWeight(getCurricularCourseAcumulatedEnrollments(enrollment
									.getCurricularCourse()));
					result.add(enrollment);
				} catch (ClassCastException e) {
					CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) elements
							.get(i);
					curricularCourse2Enroll
							.setAccumulatedWeight(getCurricularCourseAcumulatedEnrollments(curricularCourse2Enroll
									.getCurricularCourse()));
					result.add(curricularCourse2Enroll);
				}
			}

			return result;
		}
		return elements;

	}

	protected List getCurricularCoursesInCurricularCourseEquivalences(
			final ICurricularCourse curricularCourse) {

		final EnrollmentInfoCacheOSCacheImpl cache = EnrollmentInfoCacheOSCacheImpl
				.getInstance();

		final StringBuffer stringBuffer = new StringBuffer(96);
		stringBuffer.append(getDegreeCurricularPlan().getIdInternal());
		stringBuffer.append(":");
		stringBuffer.append(curricularCourse.getName());
		stringBuffer.append(":");
		stringBuffer.append(curricularCourse.getCode());

		final String cacheKey = stringBuffer.toString();
		List resultCurricularCourses = (List) cache.lookup(cacheKey);
		if (resultCurricularCourses == null) {
			final List curricularCourseEquivalences = getDegreeCurricularPlan()
					.getCurricularCourseEquivalences();
			resultCurricularCourses = resultCurricularCourses = new ArrayList();
			for (int i = 0; i < curricularCourseEquivalences.size(); i++) {
				final ICurricularCourseEquivalence curricularCourseEquivalence = (ICurricularCourseEquivalence) curricularCourseEquivalences
						.get(i);
				if (areTheseCurricularCoursesTheSame(
						curricularCourseEquivalence.getOldCurricularCourse(),
						curricularCourse)) {
					resultCurricularCourses.add(curricularCourseEquivalence
							.getEquivalentCurricularCourse());
				}
			}
			cache.cache(cacheKey, resultCurricularCourses);
		}
		return resultCurricularCourses;
	}

	protected boolean areTheseCurricularCoursesTheSame(
			ICurricularCourse curricularCourse1,
			ICurricularCourse curricularCourse2) {

		return curricularCourse1.getCurricularCourseUniqueKeyForEnrollment()
				.equals(
						curricularCourse2
								.getCurricularCourseUniqueKeyForEnrollment());
	}

	protected boolean isThisCurricularCoursesInTheList(
			final ICurricularCourse curricularCourse, List curricularCourses) {

		ICurricularCourse curricularCourseFound = (ICurricularCourse) CollectionUtils
				.find(curricularCourses, new Predicate() {
					public boolean evaluate(Object obj) {
						ICurricularCourse curricularCourseToCompare = (ICurricularCourse) obj;
						return curricularCourseToCompare
								.getCurricularCourseUniqueKeyForEnrollment()
								.equals(
										curricularCourse
												.getCurricularCourseUniqueKeyForEnrollment());
					}
				});

		return (curricularCourseFound != null);
	}

	protected List getStudentEnrollmentsWithApprovedState() {

		return (List) CollectionUtils.select(getAllEnrollments(),
				new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrolment enrollment = (IEnrolment) obj;
						return enrollment.getEnrollmentState().equals(
								EnrollmentState.APROVED);
					}
				});
	}

	protected List getStudentEnrollmentsWithEnrolledState() {

		return (List) CollectionUtils.select(getEnrolments(), new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrollment = (IEnrolment) obj;
				return enrollment.getEnrollmentState().equals(
						EnrollmentState.ENROLLED)
						&& !enrollment.getCondition().equals(
								EnrollmentCondition.INVISIBLE);
			}
		});
	}

	protected List getAllEnrollmentsExceptTheOnesWithEnrolledState() {

		return (List) CollectionUtils.select(getAllEnrollments(),
				new Predicate() {
					public boolean evaluate(Object obj) {
						IEnrolment enrollment = (IEnrolment) obj;
						return !enrollment.getEnrollmentState().equals(
								EnrollmentState.ENROLLED)
								&& !enrollment.getEnrollmentState().equals(
										EnrollmentState.ANNULED);
					}
				});
	}

	protected Map getAcumulatedEnrollmentsMap() {
		return acumulatedEnrollments;
	}

	protected void setAcumulatedEnrollmentsMap(Map acumulatedEnrollments) {
		this.acumulatedEnrollments = acumulatedEnrollments;
	}

	protected List getListOfEnrollmentRules(IExecutionPeriod executionPeriod) {
		return getDegreeCurricularPlan().getListOfEnrollmentRules(this,
				executionPeriod);
	}

	protected List getStudentNotNeedToEnrollCurricularCourses() {

		return (List) CollectionUtils.collect(
				getNotNeedToEnrollCurricularCourses(), new Transformer() {
					public Object transform(Object obj) {
						INotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (INotNeedToEnrollInCurricularCourse) obj;
						return notNeedToEnrollInCurricularCourse
								.getCurricularCourse();
					}
				});
	}

	protected List getCommonBranchAndStudentBranchesCourses(
			IExecutionPeriod executionPeriod) {

		HashSet curricularCourses = new HashSet();
		IDegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
		List commonAreas = degreeCurricularPlan.getCommonAreas();
		int commonAreasSize = commonAreas.size();

		for (int i = 0; i < commonAreasSize; i++) {
			IBranch area = (IBranch) commonAreas.get(i);
			curricularCourses.addAll(degreeCurricularPlan
					.getCurricularCoursesFromArea(area, AreaType.BASE_OBJ));
		}

		if (getBranch() != null) {
			curricularCourses.addAll(degreeCurricularPlan
					.getCurricularCoursesFromArea(getBranch(),
							AreaType.SPECIALIZATION_OBJ));
		}

		if (getSecundaryBranch() != null) {
			curricularCourses.addAll(degreeCurricularPlan
					.getCurricularCoursesFromArea(getSecundaryBranch(),
							AreaType.SECONDARY_OBJ));
		}

		curricularCourses.addAll(degreeCurricularPlan.getTFCs());

		List allCurricularCourses = new ArrayList(curricularCourses.size());
		allCurricularCourses.addAll(curricularCourses);

		List result = new ArrayList();
		int curricularCoursesSize = curricularCourses.size();

		for (int i = 0; i < curricularCoursesSize; i++) {
			ICurricularCourse curricularCourse = (ICurricularCourse) allCurricularCourses
					.get(i);
			result.add(transformToCurricularCourse2Enroll(curricularCourse,
					executionPeriod));
		}

		markOptionalCurricularCourses(allCurricularCourses, result);

		List elementsToRemove = (List) CollectionUtils.select(result,
				new Predicate() {
					public boolean evaluate(Object obj) {
						CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
						return curricularCourse2Enroll
								.getEnrollmentType()
								.equals(
										CurricularCourseEnrollmentType.NOT_ALLOWED)
								|| !curricularCourse2Enroll
										.getCurricularCourse()
										.getEnrollmentAllowed().booleanValue();
					}
				});

		result.removeAll(elementsToRemove);

		return result;
	}

	protected void markOptionalCurricularCourses(List curricularCourses,
			List result) {

		List allOptionalCurricularCourseGroups = getDegreeCurricularPlan()
				.getAllOptionalCurricularCourseGroups();

		List curricularCoursesToRemove = new ArrayList();
		List curricularCoursesToKeep = new ArrayList();

		int size = allOptionalCurricularCourseGroups.size();
		for (int i = 0; i < size; i++) {

			ICurricularCourseGroup optionalCurricularCourseGroup = (ICurricularCourseGroup) allOptionalCurricularCourseGroups
					.get(i);

			if (getBranch() != null && getSecundaryBranch() != null) {
				if (optionalCurricularCourseGroup.getBranch().equals(
						getBranch())
						|| optionalCurricularCourseGroup.getBranch().equals(
								getSecundaryBranch())) {
					selectOptionaCoursesToBeRemoved(curricularCoursesToRemove,
							curricularCoursesToKeep,
							optionalCurricularCourseGroup);
				}
			} else if (getBranch() != null && getSecundaryBranch() == null) {
				if (optionalCurricularCourseGroup.getBranch().equals(
						getBranch())) {
					selectOptionaCoursesToBeRemoved(curricularCoursesToRemove,
							curricularCoursesToKeep,
							optionalCurricularCourseGroup);
				}
			} else if (getBranch() == null && getSecundaryBranch() != null) {
				if (optionalCurricularCourseGroup.getBranch().equals(
						getSecundaryBranch())) {
					selectOptionaCoursesToBeRemoved(curricularCoursesToRemove,
							curricularCoursesToKeep,
							optionalCurricularCourseGroup);
				}
			} else if (getBranch() == null) {
				if (optionalCurricularCourseGroup.getBranch().getBranchType()
						.equals(BranchType.COMMON_BRANCH)) {
					selectOptionaCoursesToBeRemoved(curricularCoursesToRemove,
							curricularCoursesToKeep,
							optionalCurricularCourseGroup);
				}
			}

		}

		size = result.size();
		for (int i = 0; i < size; i++) {
			CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) result
					.get(i);

			if (curricularCoursesToRemove.contains(curricularCourse2Enroll
					.getCurricularCourse())
					&& !curricularCoursesToKeep
							.contains(curricularCourse2Enroll
									.getCurricularCourse())) {
				curricularCourse2Enroll
						.setEnrollmentType(CurricularCourseEnrollmentType.NOT_ALLOWED);
			} else if (curricularCoursesToKeep.contains(curricularCourse2Enroll
					.getCurricularCourse())) {
				curricularCourse2Enroll
						.setOptionalCurricularCourse(new Boolean(true));
			}
		}
	}

	/**
	 * @param curricularCoursesToRemove
	 * @param curricularCoursesToKeep
	 * @param optionalCurricularCourseGroup
	 */
	protected void selectOptionaCoursesToBeRemoved(
			List curricularCoursesToRemove, List curricularCoursesToKeep,
			ICurricularCourseGroup optionalCurricularCourseGroup) {
		int count = 0;

		int size2 = optionalCurricularCourseGroup.getCurricularCourses().size();
		for (int j = 0; j < size2; j++) {
			ICurricularCourse curricularCourse = (ICurricularCourse) optionalCurricularCourseGroup
					.getCurricularCourses().get(j);
			if (isCurricularCourseEnrolled(curricularCourse)
					|| isCurricularCourseApproved(curricularCourse)) {
				count++;
			}
		}

		if (count >= optionalCurricularCourseGroup
				.getMaximumNumberOfOptionalCourses().intValue()) {
			curricularCoursesToRemove.addAll(optionalCurricularCourseGroup
					.getCurricularCourses());
		} else {
			curricularCoursesToKeep.addAll(optionalCurricularCourseGroup
					.getCurricularCourses());
		}
	}

	protected boolean hasCurricularCourseEquivalenceIn(
			ICurricularCourse curricularCourse,
			List curricularCoursesEnrollments) {

		int size = curricularCoursesEnrollments.size();
		for (int i = 0; i < size; i++) {
			ICurricularCourse tempCurricularCourse = ((IEnrolment) curricularCoursesEnrollments
					.get(i)).getCurricularCourse();
			List curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(tempCurricularCourse);
			if (curricularCourseEquivalences.contains(curricularCourse)) {
				return true;
			}
		}

		List studentNotNeedToEnrollCourses = getStudentNotNeedToEnrollCurricularCourses();

		if (isThisCurricularCoursesInTheList(curricularCourse,
				studentNotNeedToEnrollCourses)) {
			return true;
		}

		size = studentNotNeedToEnrollCourses.size();
		for (int i = 0; i < size; i++) {
			ICurricularCourse ccNotNeedToDo = (ICurricularCourse) studentNotNeedToEnrollCourses
					.get(i);
			List curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(ccNotNeedToDo);
			if (curricularCourseEquivalences.contains(curricularCourse)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param curricularCourse
	 * @return
	 */
	protected boolean isMathematicalCourse(ICurricularCourse curricularCourse) {
		String code = curricularCourse.getCode();

		return code.equals("QN") || code.equals("PY") || code.equals("P5")
				|| code.equals("UN") || code.equals("U8") || code.equals("AZ2")
				|| code.equals("AZ3") || code.equals("AZ4")
				|| code.equals("AZ5") || code.equals("AZ6");
	}
	// -------------------------------------------------------------
	// END: Only for enrollment purposes (PROTECTED)
	// -------------------------------------------------------------
}