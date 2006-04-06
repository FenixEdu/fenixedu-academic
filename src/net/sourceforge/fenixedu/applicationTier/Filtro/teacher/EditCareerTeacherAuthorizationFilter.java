/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.EditDomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
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
        try {
            InfoCareer infoCareer = (InfoCareer) infoOject;

            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());

            boolean isNew = (infoCareer.getIdInternal() == null)
                    || (infoCareer.getIdInternal().equals(new Integer(0)));
            if (isNew)
                return true;

            Career career = rootDomainObject.readCareerByOID(infoCareer
                    .getIdInternal());

            return career.getTeacher().equals(teacher);
        } catch (Exception e) {
            return false;
        }
    }

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }
    
}
