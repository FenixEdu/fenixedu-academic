/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.EditDomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.Career;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class EditCareerTeacherAuthorizationFilter extends EditDomainObjectAuthorizationFilter {

    protected boolean verifyCondition(IUserView id, InfoObject infoOject) {
        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();

        final InfoCareer infoCareer = (InfoCareer) infoOject;

        boolean isNew = infoCareer.getIdInternal() == null || infoCareer.getIdInternal().intValue() == 0;
        if (isNew)
            return true;

        final Career career = rootDomainObject.readCareerByOID(infoCareer.getIdInternal());

        return career.getTeacher() == teacher;
    }

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

}
