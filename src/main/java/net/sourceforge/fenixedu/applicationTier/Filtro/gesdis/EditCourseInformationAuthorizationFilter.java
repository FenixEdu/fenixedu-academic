/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class EditCourseInformationAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final EditCourseInformationAuthorizationFilter instance = new EditCourseInformationAuthorizationFilter();

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(String courseReportID, InfoCourseReport infoCourseReport, String newReport) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();

        try {
            if (((id != null && id.getRoleTypes() != null && !id.hasRoleType(getRoleType()))) || (id == null)
                    || (id.getRoleTypes() == null) || (!isResponsibleFor(id, infoCourseReport))) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    private boolean isResponsibleFor(IUserView id, InfoCourseReport infoCourseReport) {
        final Person person = id.getPerson();

        InfoExecutionCourse infoExecutionCourse = infoCourseReport.getInfoExecutionCourse();
        ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(infoExecutionCourse.getExternalId());

        List<Professorship> responsiblesFor = executionCourse.responsibleFors();

        for (Professorship professorship : responsiblesFor) {
            if (professorship.getPerson() == person) {
                return true;
            }
        }
        return false;
    }
}