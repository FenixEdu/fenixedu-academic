/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoCareer;
import Dominio.ITeacher;
import Dominio.teacher.Career;
import Dominio.teacher.ICareer;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.EditDomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentCareer;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCareerTeacherAuthorizationFilter extends EditDomainObjectAuthorizationFilter {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.EditDomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView,
     *      DataBeans.InfoObject)
     */
    protected boolean verifyCondition(IUserView id, InfoObject infoOject) {
        try {
            InfoCareer infoCareer = (InfoCareer) infoOject;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCareer persistentCareer = sp.getIPersistentCareer();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            boolean isNew = (infoCareer.getIdInternal() == null)
                    || (infoCareer.getIdInternal().equals(new Integer(0)));
            if (isNew)
                return true;

            ICareer career = (ICareer) persistentCareer.readByOID(Career.class, infoCareer
                    .getIdInternal());

            return career.getTeacher().equals(teacher);
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