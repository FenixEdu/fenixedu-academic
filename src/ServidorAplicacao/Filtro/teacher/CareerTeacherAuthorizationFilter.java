/*
 * Created on 14/Nov/2003
 *  
 */
package ServidorAplicacao.Filtro.teacher;

import Dominio.ITeacher;
import Dominio.teacher.Career;
import Dominio.teacher.ICareer;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.framework.DomainObjectTeacherAuthorizationFilter;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentCareer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerTeacherAuthorizationFilter extends DomainObjectTeacherAuthorizationFilter
{

    public CareerTeacherAuthorizationFilter() {}

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
            IPersistentCareer persistentCareer = sp.getIPersistentCareer();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());
            ICareer career = (ICareer) persistentCareer.readByOID(Career.class, objectId);

            return career.getTeacher().equals(teacher);
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
