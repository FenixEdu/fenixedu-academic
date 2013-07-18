/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter instance =
            new ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter();

    public ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter() {

    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(Integer bibliographicReferenceID) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                || !bibliographicReferenceBelongsToTeacherExecutionCourse(id, bibliographicReferenceID)) {
            throw new NotAuthorizedException();
        }
    }

    private boolean bibliographicReferenceBelongsToTeacherExecutionCourse(IUserView id, Integer bibliographicReferenceID) {
        if (bibliographicReferenceID == null) {
            return false;
        }

        boolean result = false;
        final BibliographicReference bibliographicReference =
                RootDomainObject.getInstance().readBibliographicReferenceByOID(bibliographicReferenceID);
        final Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());

        if (bibliographicReference != null && teacher != null) {
            final ExecutionCourse executionCourse = bibliographicReference.getExecutionCourse();
            final Iterator associatedProfessorships = teacher.getProfessorshipsIterator();
            // Check if Teacher has a professorship to ExecutionCourse
            // BibliographicReference
            while (associatedProfessorships.hasNext()) {
                Professorship professorship = (Professorship) associatedProfessorships.next();
                if (professorship.getExecutionCourse().equals(executionCourse)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}