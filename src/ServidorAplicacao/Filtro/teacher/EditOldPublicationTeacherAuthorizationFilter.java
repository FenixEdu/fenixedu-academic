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
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Filtro.framework.EditDomainObjectTeacherAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentOldPublication;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditOldPublicationTeacherAuthorizationFilter
    extends EditDomainObjectTeacherAuthorizationFilter
{

    private static EditOldPublicationTeacherAuthorizationFilter instance =
        new EditOldPublicationTeacherAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    private EditOldPublicationTeacherAuthorizationFilter()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.framework.EditDomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView,
	 *      DataBeans.InfoObject)
	 */
    protected boolean domainObjectBelongsToTeacher(IUserView id, InfoObject infoOject)
    {
        try
        {
            InfoOldPublication infoOldPublication = (InfoOldPublication) infoOject;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            boolean isNew =
                (infoOldPublication.getIdInternal() == null)
                    || (infoOldPublication.getIdInternal().equals(new Integer(0)));
            if (isNew)
                return true;

            IOldPublication oldPublication =
                (IOldPublication) persistentOldPublication.readByOID(
                    OldPublication.class,
                    infoOldPublication.getIdInternal());

            if (!oldPublication.getTeacher().equals(teacher))
                return false;
            return true;
        } catch (ExcepcaoPersistencia e)
        {
            System.out.println("Filter error(ExcepcaoPersistente): " + e.getMessage());
            return false;
        } catch (Exception e)
        {
            System.out.println("Filter error(Unknown): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
