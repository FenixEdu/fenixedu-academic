/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoExternalActivity;
import Dominio.ITeacher;
import Dominio.teacher.ExternalActivity;
import Dominio.teacher.IExternalActivity;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.EditDomainObjectAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentExternalActivity;
import Util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditExternalActivityTeacherAuthorizationFilter extends EditDomainObjectAuthorizationFilter
{
    /* (non-Javadoc)
     * @see ServidorAplicacao.Filtro.framework.EditDomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView, DataBeans.InfoObject)
     */
    protected boolean verifyCondition(IUserView id, InfoObject infoOject)
    {
        try
        {
            InfoExternalActivity infoExternalActivity = (InfoExternalActivity) infoOject;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            boolean isNew =
                (infoExternalActivity.getIdInternal() == null)
                    || (infoExternalActivity.getIdInternal().equals(new Integer(0)));
            if (isNew)
                return true;

            IExternalActivity externalActivity =
                (IExternalActivity) persistentExternalActivity.readByOID(
                    ExternalActivity.class,
                    infoExternalActivity.getIdInternal());

           return externalActivity.getTeacher().equals(teacher);
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            return false;
        }
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Filtro.framework.EditDomainObjectAuthorizationFilter#getRoleType()
     */
    protected RoleType getRoleType()
    {
        return RoleType.TEACHER;
    }
}
