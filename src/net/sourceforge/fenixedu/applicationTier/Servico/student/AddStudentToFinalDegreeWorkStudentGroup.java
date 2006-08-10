/*
 * Created on 2004/04/19
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Luis Cruz
 */
public class AddStudentToFinalDegreeWorkStudentGroup extends Service {

    public boolean run(Integer groupOID, String username) throws ExcepcaoPersistencia,
            FenixServiceException {
        Group group = rootDomainObject.readGroupByOID(groupOID);
        Registration student = Registration.readByUsername(username);
        if (group == null
                || student == null
                || group.getGroupStudents() == null
                || CollectionUtils.find(group.getGroupStudents(),
                        new PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT(student)) != null) {
            return false;
        }
        Scheduleing scheduleing = group.getExecutionDegree().getScheduling();

        if (scheduleing == null || scheduleing.getMaximumNumberOfStudents() == null) {
            throw new MaximumNumberOfStudentsUndefinedException();
        } else if (scheduleing.getMinimumNumberOfCompletedCourses() == null) {
            throw new MinimumNumberOfCompletedCoursesUndefinedException();
        } else if (scheduleing.getMaximumNumberOfStudents().intValue() <= group.getGroupStudents()
                .size()) {
            throw new MaximumNumberOfStudentsReachedException(scheduleing.getMaximumNumberOfStudents()
                    .toString());
        } else {
        	final Integer maximumCurricularYearToCountCompletedCourses = scheduleing.getMaximumCurricularYearToCountCompletedCourses();
        	final Integer minimumCompletedCurricularYear = scheduleing.getMinimumCompletedCurricularYear();
        	final Integer minimumNumberOfCompletedCourses = scheduleing.getMinimumNumberOfCompletedCourses();

        	final StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();
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
    			throw new MinimumNumberOfCompletedCoursesNotReachedException(null, args);
    		}
        }

        GroupStudent groupStudent = new GroupStudent();
        groupStudent.setStudent(student);
        groupStudent.setFinalDegreeDegreeWorkGroup(group);
        return true;
    }

    public class MaximumNumberOfStudentsUndefinedException extends FenixServiceException {
        public MaximumNumberOfStudentsUndefinedException() {
            super();
        }

        public MaximumNumberOfStudentsUndefinedException(int errorType) {
            super(errorType);
        }

        public MaximumNumberOfStudentsUndefinedException(String s) {
            super(s);
        }

        public MaximumNumberOfStudentsUndefinedException(Throwable cause) {
            super(cause);
        }

        public MaximumNumberOfStudentsUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class MaximumNumberOfStudentsReachedException extends FenixServiceException {
        public MaximumNumberOfStudentsReachedException() {
            super();
        }

        public MaximumNumberOfStudentsReachedException(int errorType) {
            super(errorType);
        }

        public MaximumNumberOfStudentsReachedException(String s) {
            super(s);
        }

        public MaximumNumberOfStudentsReachedException(Throwable cause) {
            super(cause);
        }

        public MaximumNumberOfStudentsReachedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class MinimumNumberOfCompletedCoursesUndefinedException extends FenixServiceException {
        public MinimumNumberOfCompletedCoursesUndefinedException() {
            super();
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(int errorType) {
            super(errorType);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(String s) {
            super(s);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(Throwable cause) {
            super(cause);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class NotCompletedCurricularYearException extends FenixServiceException {
        public NotCompletedCurricularYearException(String s, String[] args) {
            super(s, args);
        }
    }

    public class MinimumNumberOfCompletedCoursesNotReachedException extends FenixServiceException {
        public MinimumNumberOfCompletedCoursesNotReachedException(String s, String[] args) {
            super(s, args);
        }
    }

    private class PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT implements Predicate {
        Registration student = null;

        public boolean evaluate(Object arg0) {
            GroupStudent groupStudent = (GroupStudent) arg0;
            return student.getIdInternal().equals(groupStudent.getStudent().getIdInternal());
        }

        public PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT(Registration student) {
            super();
            this.student = student;
        }
    }

}
