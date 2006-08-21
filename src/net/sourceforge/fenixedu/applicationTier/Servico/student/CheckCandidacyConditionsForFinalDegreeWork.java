/*
 * Created on 2004/04/21
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class CheckCandidacyConditionsForFinalDegreeWork extends Service {

    public boolean run(IUserView userView, Integer executionDegreeOID) throws ExcepcaoPersistencia,
            FenixServiceException {
    	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);
        Scheduleing scheduleing = executionDegree.getScheduling();

        if (scheduleing == null || scheduleing.getStartOfCandidacyPeriod() == null
                || scheduleing.getEndOfCandidacyPeriod() == null) {
            throw new CandidacyPeriodNotDefinedException();
        }

        Calendar now = Calendar.getInstance();
        if (scheduleing.getStartOfCandidacyPeriod().after(now.getTime())
                || scheduleing.getEndOfCandidacyPeriod().before(now.getTime())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String start = simpleDateFormat.format(new Date(scheduleing.getStartOfCandidacyPeriod()
                    .getTime()));
            String end = simpleDateFormat.format(new Date(scheduleing.getEndOfCandidacyPeriod()
                    .getTime()));
            throw new OutOfCandidacyPeriodException(start + " - " + end);
        }

        Registration registration = findStudent(userView.getPerson());

        if (scheduleing.getMinimumNumberOfCompletedCourses() == null) {
            throw new NumberOfNecessaryCompletedCoursesNotSpecifiedException();
        }
    	final Integer maximumCurricularYearToCountCompletedCourses = scheduleing.getMaximumCurricularYearToCountCompletedCourses();
    	final Integer minimumCompletedCurricularYear = scheduleing.getMinimumCompletedCurricularYear();
    	final Integer minimumNumberOfCompletedCourses = scheduleing.getMinimumNumberOfCompletedCourses();

    	final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
    	final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
    	final Collection<CurricularCourseScope> degreesActiveCurricularCourseScopes = degreeCurricularPlan.getActiveCurricularCourseScopes();
    	final StringBuilder notCompletedCurricularCourses = new StringBuilder();
    	final Set<CurricularCourse> notCompletedCurricularCoursesForMinimumCurricularYear = new HashSet<CurricularCourse>();
    	final Set<CurricularCourse> completedCurricularCourses = new HashSet<CurricularCourse>();
    	//int numberCompletedCurricularCourses = 0;
		for (final CurricularCourseScope curricularCourseScope : degreesActiveCurricularCourseScopes) {
			final CurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();
			final boolean isCurricularCourseApproved = studentCurricularPlan.isCurricularCourseApproved(curricularCourse);

			final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
			final CurricularYear curricularYear = curricularSemester.getCurricularYear();

			if (minimumCompletedCurricularYear != null && curricularYear.getIdInternal().intValue() <= minimumCompletedCurricularYear.intValue()) {
				if (!isCurricularCourseApproved) {
					notCompletedCurricularCoursesForMinimumCurricularYear.add(curricularCourse);
				}
			}

			if (maximumCurricularYearToCountCompletedCourses == null || curricularYear.getYear().intValue() <= maximumCurricularYearToCountCompletedCourses.intValue()) {
				if (isCurricularCourseApproved) {
					completedCurricularCourses.add(curricularCourseScope.getCurricularCourse());
					//numberCompletedCurricularCourses++;
				} else {
					if (notCompletedCurricularCourses.length() > 0) {
						notCompletedCurricularCourses.append(", ");
					}
					notCompletedCurricularCourses.append(curricularCourseScope.getCurricularCourse().getName());
				}
			}
		}

		if (!notCompletedCurricularCoursesForMinimumCurricularYear.isEmpty()) {
			final StringBuilder stringBuilder = new StringBuilder();
			for (final CurricularCourse curricularCourse : notCompletedCurricularCoursesForMinimumCurricularYear) {
				if (stringBuilder.length() > 0) {
					stringBuilder.append(", ");
				}
				stringBuilder.append(curricularCourse.getName());
			}
			final String[] args = { minimumCompletedCurricularYear.toString(), stringBuilder.toString()};
			throw new NotCompletedCurricularYearException(null, args);
		}

		int numberCompletedCurricularCourses = completedCurricularCourses.size();
		if (numberCompletedCurricularCourses < minimumNumberOfCompletedCourses) {
			final int numberMissingCurricularCourses = minimumNumberOfCompletedCourses - numberCompletedCurricularCourses;
			final String[] args = { Integer.toString(numberMissingCurricularCourses), notCompletedCurricularCourses.toString()};
			throw new InsufficientCompletedCoursesException(null, args);
		}

        return true;
    }

    private Registration findStudent(final Person person) {
    	if (person != null) {
    		final Registration registration = person.getStudentByType(DegreeType.DEGREE);
    		return registration == null ? person.getStudentByType(DegreeType.MASTER_DEGREE) : registration;
    	}
    	return null;
	}

	public class CandidacyPeriodNotDefinedException extends FenixServiceException {

        public CandidacyPeriodNotDefinedException() {
            super();
        }

        public CandidacyPeriodNotDefinedException(int errorType) {
            super(errorType);
        }

        public CandidacyPeriodNotDefinedException(String s) {
            super(s);
        }

        public CandidacyPeriodNotDefinedException(Throwable cause) {
            super(cause);
        }

        public CandidacyPeriodNotDefinedException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public class OutOfCandidacyPeriodException extends FenixServiceException {

        public OutOfCandidacyPeriodException() {
            super();
        }

        public OutOfCandidacyPeriodException(int errorType) {
            super(errorType);
        }

        public OutOfCandidacyPeriodException(String s) {
            super(s);
        }

        public OutOfCandidacyPeriodException(Throwable cause) {
            super(cause);
        }

        public OutOfCandidacyPeriodException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public class NotCompletedCurricularYearException extends FenixServiceException {
        public NotCompletedCurricularYearException(String s, String[] args) {
            super(s, args);
        }
    }

    public class InsufficientCompletedCoursesException extends FenixServiceException {
        public InsufficientCompletedCoursesException(String s, String[] args) {
            super(s, args);
        }
    }

    public class NumberOfNecessaryCompletedCoursesNotSpecifiedException extends FenixServiceException {

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException() {
            super();
        }

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException(int errorType) {
            super(errorType);
        }

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException(String s) {
            super(s);
        }

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException(Throwable cause) {
            super(cause);
        }

        public NumberOfNecessaryCompletedCoursesNotSpecifiedException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}