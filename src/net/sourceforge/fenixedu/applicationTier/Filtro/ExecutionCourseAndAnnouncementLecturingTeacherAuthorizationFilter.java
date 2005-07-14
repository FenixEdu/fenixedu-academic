/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.IAnnouncement;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAnnouncement;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
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
            final ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            final IPersistentAnnouncement persistentAnnouncement = persistentSupport
                    .getIPersistentAnnouncement();

            final Integer announcementID = getAnnouncementID(args);
            final IAnnouncement announcement = (IAnnouncement) persistentAnnouncement.readByOID(
                    Announcement.class, announcementID);
            if (announcement != null) {
                final Integer executionCourseID = getExecutionCourseID(args);
                IExecutionCourse executionCourse = announcement.getSite().getExecutionCourse();
                // Check if Announcement Belongs to the same ExecutionCourse
                if (executionCourse.getIdInternal().equals(executionCourseID)) {
                    // Then Check if Teacher has a professorship to that
                    // ExecutionCourse
                    final Iterator associatedProfessorships = executionCourse
                            .getProfessorshipsIterator();
                    while (associatedProfessorships.hasNext()) {
                        IProfessorship professorship = (IProfessorship) associatedProfessorships.next();
                        if (professorship.getExecutionCourse().getIdInternal().equals(executionCourseID)) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        } catch (ExcepcaoPersistencia e) {
            result = false;
        }
        return result;
    }

    private Integer getExecutionCourseID(Object[] args) {
        Integer executionCourseID;
        if (args[0] instanceof InfoExecutionCourse) {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) args[0];
            executionCourseID = infoExecutionCourse.getIdInternal();
        } else {
            executionCourseID = (Integer) args[0];
        }
        return executionCourseID;
    }

    private Integer getAnnouncementID(Object[] args) {
        Integer announcementID;
        if (args[0] instanceof InfoAnnouncement) {
            InfoAnnouncement infoAnnouncement = (InfoAnnouncement) args[1];
            announcementID = infoAnnouncement.getIdInternal();
        } else {
            announcementID = (Integer) args[1];
        }
        return announcementID;
    }
}