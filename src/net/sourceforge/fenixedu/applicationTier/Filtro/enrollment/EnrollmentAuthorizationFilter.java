package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class EnrollmentAuthorizationFilter extends AuthorizationByManyRolesFilter {

    private static final int LEIC_OLD_DCP = 10;

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
        final List<RoleType> roles = new ArrayList<RoleType>();
        roles.add(RoleType.COORDINATOR);
        roles.add(RoleType.TEACHER);
        roles.add(RoleType.STUDENT);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        return roles;
    }

    protected String hasPrevilege(IUserView userView, Object[] arguments) {
            if (userView.hasRoleType(RoleType.STUDENT)) {
                return checkStudentInformation(userView);

            } else {
                if (userView.hasRoleType(RoleType.COORDINATOR) && arguments[0] != null) {
                	return checkCoordinatorInformation(userView, arguments);
                    
                } else if (userView.hasRoleType(RoleType.TEACHER)) {
                	return checkTeacherInformation(userView, arguments);

                } else if (userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                        || userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {                	
                    
                	return checkDegreeAdministrativeOfficeInformation(arguments);
                    
                } else {
                    return "noAuthorization";
                }
            }
    }

	protected String checkDegreeAdministrativeOfficeInformation(Object[] args) {

		final StudentCurricularPlan studentCurricularPlan = readStudent(args).getActiveStudentCurricularPlan();
		if (studentCurricularPlan == null) {
			return "noAuthorization";
		}
		if (insideEnrollmentPeriod(studentCurricularPlan)) {
			final Tutor tutor = studentCurricularPlan.getStudent().getAssociatedTutor();
			if (tutor != null) {
				return new String("error.enrollment.student.withTutor+"
						+ tutor.getTeacher().getTeacherNumber().toString() + "+"
						+ tutor.getTeacher().getPerson().getNome());
			}
		}
		return null;
	}

	protected String checkTeacherInformation(IUserView userView, Object[] arguments) {
		
		final Teacher teacher = readTeacher(userView);
		if (teacher == null) {
		    return "noAuthorization";
		}

		final Registration student = readStudent(arguments);
		if (student == null) {
		    return "noAuthorization";
		}

		if (! student.hasAssociatedTutor()) {
		    return new String("error.enrollment.notStudentTutor+"
		            + student.getNumber().toString());
		}
		
		return null;
	}

	private String checkCoordinatorInformation(IUserView userView, Object[] arguments) {
		
		final Teacher teacher = readTeacher(userView);
		if (teacher == null) {
		    return "noAuthorization";
		}
		if (!verifyCoordinator(teacher, arguments)) {
		    return "noAuthorization";
		}
		
		return null;
	}

	private String checkStudentInformation(IUserView userView) {
		
		final Registration student = readStudent(userView);
		if (student == null) {
		    return "noAuthorization";
		}
		if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
		    if(student.getInterruptedStudies().equals(Boolean.FALSE)) {
		    	return "error.message.tuitionNotPayed";
		    }
		}
		if (student.getFlunked() == null || student.getFlunked().equals(Boolean.TRUE)) {
		    return "error.message.flunked";
		}
		if (student.getRequestedChangeDegree() == null || student.getRequestedChangeDegree().equals(Boolean.TRUE)) {
		    return "error.message.requested.change.degree";
		}
		
		if (student.hasAssociatedTutor()) {
		    return new String("error.enrollment.student.withTutor+"
		            + student.getAssociatedTutor().getTeacher().getTeacherNumber().toString() + "+"
		            + student.getAssociatedTutor().getTeacher().getPerson().getNome());
		}

		// check if the student is in the list of secretary enrolments
		// students
		final SecretaryEnrolmentStudent secretaryEnrolmentStudent = student.getSecretaryEnrolmentStudents().isEmpty() ?
		        null : student.getSecretaryEnrolmentStudents().iterator().next();
		if (secretaryEnrolmentStudent != null) {
		    return "error.message.secretaryEnrolmentStudent";
		}

		// check if the student is from old Leic Curricular Plan
		List studentCurricularPlans = (List) CollectionUtils.select(student.getStudentCurricularPlans(), new Predicate(){
		    public boolean evaluate(Object arg0) {
		        StudentCurricularPlan scp = (StudentCurricularPlan) arg0;
		        return scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE);
		    }});
            
		boolean oldLeicStudent = CollectionUtils.exists(studentCurricularPlans, new Predicate() {
		    public boolean evaluate(Object arg0) {

		        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) arg0;
		        return (studentCurricularPlan.getDegreeCurricularPlan().getIdInternal()
		                .intValue() == LEIC_OLD_DCP);

		    }
		});
		if (oldLeicStudent) {
		    return "error.message.oldLeicStudent";
		}
		
		return null;
	}

    protected boolean insideEnrollmentPeriod(StudentCurricularPlan studentCurricularPlan) {
    	return (studentCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriod() != null);
    }

    protected Registration readStudent(IUserView userView) {
    	return userView.getPerson().getStudentByUsername();
    }

    protected Registration readStudent(Object[] arguments) {
    	return  (arguments[1] != null) ? (Registration) arguments[1] : null; 
    }

    protected Teacher readTeacher(IUserView id) {
        return Teacher.readTeacherByUsername(id.getUtilizador());
    }

    protected boolean verifyCoordinator(Teacher teacher, Object[] args) {

        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID((Integer) args[0]);
        if(executionDegree == null) {
        	return false;
        }
        
        final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(teacher);
        if (coordinator == null) {
            return false;
        }
        
    	//check if is LEEC coordinator
    	if(!coordinator.getExecutionDegree().getDegreeCurricularPlan().getName().equals("LEEC 2003")) {
    		return false;
    	}

        final StudentCurricularPlan studentCurricularPlan = readStudent(args).getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            return false;
        }
        return studentCurricularPlan.getDegreeCurricularPlan().equals(
                coordinator.getExecutionDegree().getDegreeCurricularPlan());
    }
}