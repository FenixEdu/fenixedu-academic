package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class EnrollmentVO extends VersionedObjectsBase implements
		IPersistentEnrollment {

	public List readAllEnrolmentsByStudentCurricularPlanAndExecutionPeriod(
			Integer studentCurricularPlanId, final Integer executionPeriodId)
			throws ExcepcaoPersistencia {
		StudentCurricularPlan scp = (StudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanId);
		List enrolments = scp.getEnrolments();

		return (List) CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				return ((Enrolment) o).getExecutionPeriod().getIdInternal()
						.equals(executionPeriodId);
			}
		});
	}

	public List readEnrollmentsByStudentAndCurricularCourseNameAndDegree(
			final Integer studentId, final String curricularCourseName,
			final Integer degreeId) throws ExcepcaoPersistencia {
		List enrolments = (List) readAll(Enrolment.class);
		return (List) CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				Enrolment enrolment = (Enrolment) o;

				return (enrolment.getStudentCurricularPlan().getStudent()
						.getIdInternal().equals(studentId))
						&& (enrolment.getCurricularCourse().getName()
								.equals(curricularCourseName))
						&& (enrolment.getStudentCurricularPlan()
								.getDegreeCurricularPlan().getDegree()
								.getIdInternal().equals(degreeId));
			}
		});
	}

	public List readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
			Integer studentCurricularPlanId,
			final EnrollmentState enrollmentState) throws ExcepcaoPersistencia {
		StudentCurricularPlan scp = (StudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanId);
		List enrolments = scp.getEnrolments();

		return (List) CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				return ((Enrolment) o).getEnrollmentState().equals(
						enrollmentState);
			}
		});
	}

	public Enrolment readEnrolmentByStudentNumberAndCurricularCourse(
			final Integer studentNumber, final Integer curricularCourseId,
			final String year) throws ExcepcaoPersistencia {
		List enrolments = (List) readAll(Enrolment.class);
		return (Enrolment) CollectionUtils.find(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				Enrolment enrolment = (Enrolment) o;

				return (enrolment.getStudentCurricularPlan().getStudent()
						.getNumber().equals(studentNumber))
						&& (enrolment.getExecutionPeriod().getExecutionYear()
								.getYear().equals(year))
						&& (enrolment.getCurricularCourse().getIdInternal()
								.equals(curricularCourseId));
			}
		});
	}

	public List readByCurricularCourseAndYear(Integer curricularCourseId,
			final String year) throws ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) readByOID(
				CurricularCourse.class, curricularCourseId);
		List enrolments = curricularCourse.getEnrolments();

		return (List) CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				return ((Enrolment) o).getExecutionPeriod().getExecutionYear()
						.getYear().equals(year);
			}
		});
	}

	public List readByCurricularCourseAndExecutionPeriod(
			Integer curricularCourseId, final Integer executionPeriodId)
			throws ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) readByOID(
				CurricularCourse.class, curricularCourseId);
		List enrolments = curricularCourse.getEnrolments();

		return (List) CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				return ((Enrolment) o).getExecutionPeriod().getIdInternal()
						.equals(executionPeriodId);
			}
		});
	}

	public List readAprovedEnrolmentsFromOtherExecutionPeriodByStudentCurricularPlanAndCurricularCourse(
			Integer studentCurricularPlanId, final Integer curricularCourseId,
			final Integer executionPeriodId) throws ExcepcaoPersistencia {
		StudentCurricularPlan scp = (StudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanId);
		List enrolments = scp.getEnrolments();

		return (List) CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				Enrolment enrolment = (Enrolment) o;
				return (enrolment.getCurricularCourse().getIdInternal()
						.equals(curricularCourseId))
						&& (!enrolment.getExecutionPeriod().equals(
								executionPeriodId))
						&& (enrolment.getEnrollmentState()
								.equals(EnrollmentState.APROVED));
			}
		});
	}

	public Enrolment readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
			Integer studentCurricularPlanId, final Integer curricularCourseId,
			final Integer executionPeriodId) throws ExcepcaoPersistencia {
		StudentCurricularPlan scp = (StudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanId);
		List enrolments = scp.getEnrolments();

		return (Enrolment) CollectionUtils.find(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				Enrolment enrolment = (Enrolment) o;
				return (enrolment.getCurricularCourse().getIdInternal().equals(curricularCourseId))
						&& (enrolment.getExecutionPeriod().getIdInternal().equals(executionPeriodId));
			}
		});
	}

	public List readByStudentCurricularPlanAndCurricularCourse(
			Integer studentCurricularPlanId, final Integer curricularCourseId)
			throws ExcepcaoPersistencia {
		StudentCurricularPlan scp = (StudentCurricularPlan) readByOID(
				StudentCurricularPlan.class, studentCurricularPlanId);
		List enrolments = scp.getEnrolments();

		return (List) CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				Enrolment enrolment = (Enrolment) o;
				return enrolment.getCurricularCourse().getIdInternal().equals(
						curricularCourseId);
			}
		});
	}

	public int countEnrolmentsByCurricularCourseAndExecutionPeriod(
			Integer curricularCourseId, final Integer executionPeriodId)
			throws ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) readByOID(
				CurricularCourse.class, curricularCourseId);
		List enrolments = curricularCourse.getEnrolments();

		return (CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				return ((Enrolment) o).getExecutionPeriod().getIdInternal()
						.equals(executionPeriodId);
			}
		})).size();
	}

	public int countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(
			Integer studentId) throws ExcepcaoPersistencia {
		
		Student student = (Student)readByOID(Student.class,studentId);
		
		int numberCompletedCourses = countCompletedCoursesInActiveStudentCurricularPlans(studentId);

        StudentCurricularPlan studentCurricularPlan = getStudentsActiveUndergraduateCurricularPlan(student);
        if ((studentCurricularPlan != null)
                && !(studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals("LEEC"))) {
            
			List<Enrolment> enrolmentsInOtherStudentCurricularPlans = getApprovedEnrolmentsInOtherStudentCurricularPlans(studentId);
            for (Enrolment enrolment : enrolmentsInOtherStudentCurricularPlans) {
                int numberOfEquivalentEnrolments = countEnrolmentEquivalences(enrolment);

                if (numberOfEquivalentEnrolments == 0) {
                    numberCompletedCourses++;
                }
            }
        }

        return numberCompletedCourses;
	}

	private int countEnrolmentEquivalences(Enrolment enrolment) {
		return enrolment.getEquivalentEnrolmentForEnrolmentEquivalences().size();
	}

	private int countCompletedCoursesInActiveStudentCurricularPlans(final Integer studentId) {
		List enrolments = (List)readAll(Enrolment.class);

		return (CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				
				Enrolment enrolment = (Enrolment) o;
				return enrolment.getStudentCurricularPlan().getStudent().getIdInternal().equals(studentId) &&
						enrolment.getStudentCurricularPlan().getCurrentState().equals(StudentCurricularPlanState.ACTIVE) &&
						enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getDegree().getTipoCurso().equals(DegreeType.DEGREE) &&
						enrolment.getEnrollmentState().equals(EnrollmentState.APROVED);
			}
		})).size();
	}

	private List getApprovedEnrolmentsInOtherStudentCurricularPlans(final Integer studentId) throws ExcepcaoPersistencia {
		List enrolments = (List)readAll(Enrolment.class);

		return (List)CollectionUtils.select(enrolments, new Predicate() {
			public boolean evaluate(Object o) {
				
				Enrolment enrolment = (Enrolment) o;
				return enrolment.getStudentCurricularPlan().getStudent().getIdInternal().equals(studentId) &&
						(!enrolment.getStudentCurricularPlan().getCurrentState().equals(StudentCurricularPlanState.ACTIVE)) &&
						enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getDegree().getTipoCurso().equals(DegreeType.DEGREE) &&
						enrolment.getEnrollmentState().equals(EnrollmentState.APROVED);
			}
		});
	}
	
	private StudentCurricularPlan getStudentsActiveUndergraduateCurricularPlan(
			Student student) throws ExcepcaoPersistencia {

		List scps = student.getStudentCurricularPlans();
		
		return (StudentCurricularPlan)CollectionUtils.find(scps,new Predicate(){
				public boolean evaluate (Object o) {
					StudentCurricularPlan scp = (StudentCurricularPlan)o;
					
					return scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE) &&
							scp.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(DegreeType.DEGREE);
				}
		});
	}

}
