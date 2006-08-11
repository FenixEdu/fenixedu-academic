/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.EditDomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class EditExternalActivityTeacherAuthorizationFilter extends EditDomainObjectAuthorizationFilter {

    protected boolean verifyCondition(IUserView id, InfoObject infoOject) {
        InfoExternalActivity infoExternalActivity = (InfoExternalActivity) infoOject;
        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();

        boolean isNew = infoExternalActivity.getIdInternal() == null
                || infoExternalActivity.getIdInternal().intValue() == 0;
        if (isNew) {
            return true;
        }

        final ExternalActivity externalActivity = rootDomainObject
                .readExternalActivityByOID(infoExternalActivity.getIdInternal());
        return externalActivity.getTeacher() == teacher;
    }

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

}
