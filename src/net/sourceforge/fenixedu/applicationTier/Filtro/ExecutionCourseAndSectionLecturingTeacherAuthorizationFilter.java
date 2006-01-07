/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseAndSectionLecturingTeacherAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public ExecutionCourseAndSectionLecturingTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        if ((id == null) || (id.getRoles() == null)
                || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                || !lecturesExecutionCourse(id, arguments)
                || !sectionBelongsExecutionCourse(id, arguments)) {
            throw new NotAuthorizedFilterException();
        }

    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean sectionBelongsExecutionCourse(IUserView id, Object[] argumentos) {
        InfoExecutionCourse infoExecutionCourse = null;
        ExecutionCourse executionCourse = null;
        ISuportePersistente sp;
        Section section = null;
        InfoSection infoSection = null;

        if (argumentos == null) {
            return false;
        }
        try {

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                        ExecutionCourse.class, infoExecutionCourse.getIdInternal());
            } else {
                executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                        ExecutionCourse.class, (Integer) argumentos[0]);
            }
            IPersistentSection persistentSection = sp.getIPersistentSection();
            if (argumentos[1] == null) {
                return true;
            }
            if (argumentos[1] instanceof InfoSection) {
                infoSection = (InfoSection) argumentos[1];

                section = (Section) persistentSection.readByOID(Section.class, infoSection
                        .getIdInternal());
            } else {
                section = (Section) persistentSection.readByOID(Section.class, (Integer) argumentos[1]);

            }
        } catch (Exception e) {
            return false;
        }

        if (section == null) {
            return false;
        }

        return section.getSite().getExecutionCourse().equals(executionCourse);
    }

    private boolean lecturesExecutionCourse(IUserView id, Object[] argumentos) {

        Integer executionCourseID = null;

        if (argumentos == null) {
            return false;
        }
        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            if (argumentos[0] instanceof InfoExecutionCourse) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourseID = infoExecutionCourse.getIdInternal();
            } else {
                executionCourseID = (Integer) argumentos[0];
            }

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            Teacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            Professorship professorship = null;
            if (teacher != null) {
                IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
                professorship = persistentProfessorship.readByTeacherAndExecutionCourse(teacher
                        .getIdInternal(), executionCourseID);
            }
            return professorship != null;

        } catch (Exception e) {
            return false;
        }
    }

}
