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
import ServidorAplicacao.Filtro.Filtro;
import ServidorAplicacao.Filtro.framework.EditDomainObjectTeacherAuthorizationFilter;
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
public class EditCareerTeacherAuthorizationFilter extends EditDomainObjectTeacherAuthorizationFilter
{

    private static EditCareerTeacherAuthorizationFilter instance =
        new EditCareerTeacherAuthorizationFilter();

    /**
	 * The singleton access method of this class.
	 * 
	 * @return Returns the instance of this class responsible for the authorization access to services.
	 */
    public static Filtro getInstance()
    {
        return instance;
    }

    private EditCareerTeacherAuthorizationFilter()
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
            InfoCareer infoCareer = (InfoCareer) infoOject;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCareer persistentCareer = sp.getIPersistentCareer();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            boolean isNew =
                (infoCareer.getIdInternal() == null)
                    || (infoCareer.getIdInternal().equals(new Integer(0)));
            if (isNew)
                return true;

            ICareer career =
                (ICareer) persistentCareer.readByOID(Career.class, infoCareer.getIdInternal());

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
