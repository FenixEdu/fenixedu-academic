/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import Dominio.ITeacher;
import Dominio.teacher.IOldPublication;
import Dominio.teacher.OldPublication;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Filtro.framework.DomainObjectTeacherAuthorizationFilter;
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
public class OldPublicationTeacherAuthorizationFilter extends DomainObjectTeacherAuthorizationFilter
{

    private static OldPublicationTeacherAuthorizationFilter instance =
        new OldPublicationTeacherAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.framework.DomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView,
	 *      java.lang.Integer)
	 */
    protected boolean domainObjectBelongsToTeacher(IUserView id, Integer objectId)
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentOldPublication persistentOldPublication = sp.getIPersistentOldPublication();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            IOldPublication oldPublication =
                (IOldPublication) persistentOldPublication.readByOID(OldPublication.class, objectId);

            return oldPublication.getTeacher().equals(teacher);
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
