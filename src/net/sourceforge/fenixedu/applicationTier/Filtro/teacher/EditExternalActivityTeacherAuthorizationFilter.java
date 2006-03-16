/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.EditDomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditExternalActivityTeacherAuthorizationFilter extends EditDomainObjectAuthorizationFilter {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.EditDomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject)
     */
    protected boolean verifyCondition(IUserView id, InfoObject infoOject) {
        try {
            InfoExternalActivity infoExternalActivity = (InfoExternalActivity) infoOject;
            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());

            boolean isNew = (infoExternalActivity.getIdInternal() == null)
                    || (infoExternalActivity.getIdInternal().equals(new Integer(0)));
            if (isNew)
                return true;

            ExternalActivity externalActivity = (ExternalActivity) persistentObject
                    .readByOID(ExternalActivity.class, infoExternalActivity.getIdInternal());

            return externalActivity.getTeacher().equals(teacher);
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.EditDomainObjectAuthorizationFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }
}