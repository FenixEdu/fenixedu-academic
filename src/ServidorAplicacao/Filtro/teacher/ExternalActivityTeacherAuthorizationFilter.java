/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import Dominio.ITeacher;
import Dominio.teacher.ExternalActivity;
import Dominio.teacher.IExternalActivity;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectTeacherAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ExternalActivityTeacherAuthorizationFilter extends DomainObjectTeacherAuthorizationFilter
{

    public ExternalActivityTeacherAuthorizationFilter()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView,
     *          java.lang.Integer)
     */
    protected boolean domainObjectBelongsToTeacher(IUserView id, Integer objectId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExternalActivity persistentExternalActivity = sp.getIPersistentExternalActivity();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IExternalActivity externalActivity = (IExternalActivity) persistentExternalActivity
                            .readByOID(ExternalActivity.class, objectId);

            return externalActivity.getTeacher().equals(teacher);
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        }
        catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            return false;
        }
    }

}
