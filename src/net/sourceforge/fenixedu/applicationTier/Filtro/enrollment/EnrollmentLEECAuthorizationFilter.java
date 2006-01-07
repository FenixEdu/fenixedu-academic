/*
 * Created on 6/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoRole;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class EnrollmentLEECAuthorizationFilter extends EnrollmentAuthorizationFilter {
    private static String DEGREE_LEEC_CODE = new String("LEEC");

    protected Collection getNeededRoles() {
        List roles = new ArrayList();

        InfoRole infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.COORDINATOR);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.TEACHER);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.STUDENT);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        roles.add(infoRole);

        infoRole = new InfoRole();
        infoRole.setRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        roles.add(infoRole);
        return roles;
    }

    protected String hasPrevilege(IUserView id, Object[] arguments) {
        try {
            List roles = getRoleList(id.getRoles());

            ISuportePersistente sp = null;
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            //verify if the student making the enrollment is a LEEC degree
            // student
            if (roles.contains(RoleType.STUDENT)) {
                Student student = readStudent(id, sp);
                if (student == null) {
                    return "noAuthorization";
                }

                if (!verifyStudentLEEC(arguments, sp)) {
                    return new String("error.student.degreeCurricularPlan.LEEC");
                }
                if (!curriculumOwner(student, id)) {
                    return "noAuthorization";

                }
                Tutor tutor = verifyStudentWithTutor(student, sp);
                if (tutor != null) {
                    return new String("error.enrollment.student.withTutor+"
                            + tutor.getTeacher().getTeacherNumber().toString() + "+"
                            + tutor.getTeacher().getPerson().getNome());
                }
            } else {
                //verify if the student to enroll is a LEEC degree student
                if (!verifyStudentLEEC(arguments, sp)) {
                    return new String("error.student.degreeCurricularPlan.LEEC");
                }

                //verify if the coodinator is of the LEEC degree
                if (roles.contains(RoleType.COORDINATOR) && arguments[0] != null) {
                    Teacher teacher = readTeacher(id, sp);
                    if (teacher == null) {
                        return "noAuthorization";
                    }

                    if (!verifyCoordinatorLEEC(teacher, arguments, sp)) {
                        return "noAuthorization";
                    }
                } else if (roles.contains(RoleType.TEACHER)) {
                    Teacher teacher = readTeacher(id, sp);
                    if (teacher == null) {
                        return "noAuthorization";
                    }

                    Student student = readStudent(arguments, sp);
                    if (student == null) {
                        return "noAuthorization";
                    }

                    if (!verifyStudentTutor(teacher, student, sp)) {
                        return new String("error.enrollment.notStudentTutor+"
                                + student.getNumber().toString());
                    }

                } else if (roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                        || roles.contains(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
                    StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments,
                            sp);

                    if (studentCurricularPlan.getStudent() == null) {
                        return "noAuthorization";
                    }
                    if (insideEnrollmentPeriod(studentCurricularPlan, sp)) {
                        Tutor tutor = verifyStudentWithTutor(studentCurricularPlan.getStudent(), sp);
                        if (tutor != null) {
                            return new String("error.enrollment.student.withTutor+"
                                    + tutor.getTeacher().getTeacherNumber().toString() + "+"
                                    + tutor.getTeacher().getPerson().getNome());
                        }
                    }
                    return null;
                } else {
                    return "noAuthorization";
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return "noAuthorization";
        }
        return null;
    }

    private boolean verifyStudentLEEC(Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        StudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(arguments, sp);
        if (studentCurricularPlan == null) {
            return false;
        }

        String degreeCode = null;
        if (studentCurricularPlan.getDegreeCurricularPlan() != null
                && studentCurricularPlan.getDegreeCurricularPlan().getDegree() != null) {
            degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla();
        }

        return DEGREE_LEEC_CODE.equals(degreeCode);
    }

    private boolean verifyCoordinatorLEEC(Teacher teacher, Object[] arguments, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
        Coordinator coordinator = persistentCoordinator.readCoordinatorByTeacherIdAndExecutionDegreeId(
                teacher.getIdInternal(), (Integer) arguments[0]);
        if (coordinator == null) {
            return false;
        }

        String degreeCode = null;
        if (coordinator.getExecutionDegree() != null
                && coordinator.getExecutionDegree().getDegreeCurricularPlan() != null
                && coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree() != null) {
            degreeCode = coordinator.getExecutionDegree().getDegreeCurricularPlan().getDegree().getSigla();
        }

        return DEGREE_LEEC_CODE.equals(degreeCode);
    }

}