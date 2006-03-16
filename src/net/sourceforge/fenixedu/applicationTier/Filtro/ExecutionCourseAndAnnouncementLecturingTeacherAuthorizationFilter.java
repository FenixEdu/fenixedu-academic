/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseAndAnnouncementLecturingTeacherAuthorizationFilter extends
        AuthorizationByRoleFilter {

    public ExecutionCourseAndAnnouncementLecturingTeacherAuthorizationFilter() {
    }

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try {
            if ((id == null) || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
                    || !announcementBelongsToTeacherExecutionCourses(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean announcementBelongsToTeacherExecutionCourses(IUserView id, Object[] args) {
        if (args == null) {
            return false;
        }
        boolean result = false;
        try {            
            final Integer announcementID = getAnnouncementID(args);
            final Announcement announcement = (Announcement) persistentObject.readByOID(
                    Announcement.class, announcementID);
            final Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());

            if (announcement != null && teacher != null) {
                final ExecutionCourse executionCourse = announcement.getSite().getExecutionCourse();
                // Check if Teacher has a professorship to ExecutionCourse Announcement
                final Iterator associatedProfessorships = teacher.getProfessorshipsIterator();
                while (associatedProfessorships.hasNext()) {
                    Professorship professorship = (Professorship) associatedProfessorships.next();
                    if (professorship.getExecutionCourse().equals(executionCourse)) {
                        result = true;
                        break;
                    }
                }
            }
        } catch (ExcepcaoPersistencia e) {
            result = false;
        }
        return result;
    }

    private Integer getAnnouncementID(Object[] args) {
        return (Integer) args[0];
    }
}