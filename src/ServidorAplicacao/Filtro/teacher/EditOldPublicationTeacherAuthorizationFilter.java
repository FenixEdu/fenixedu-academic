/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoOldPublication;
import Dominio.ITeacher;
import Dominio.teacher.IOldPublication;
import Dominio.teacher.OldPublication;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.EditDomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentOldPublication;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditOldPublicationTeacherAuthorizationFilter extends EditDomainObjectAuthorizationFilter {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.EditDomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView,
     *      DataBeans.InfoObject)
     */
    protected boolean verifyCondition(IUserView id, InfoObject infoOject) {
        try {
            InfoOldPublication infoOldPublication = (InfoOldPublication) infoOject;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            boolean isNew = (infoOldPublication.getIdInternal() == null)
                    || (infoOldPublication.getIdInternal().equals(new Integer(0)));
            if (isNew)
                return true;

            IOldPublication oldPublication = (IOldPublication) persistentOldPublication.readByOID(
                    OldPublication.class, infoOldPublication.getIdInternal());

            return oldPublication.getTeacher().equals(teacher);
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
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