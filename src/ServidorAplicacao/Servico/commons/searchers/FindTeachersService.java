/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.framework.SearchService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class FindTeachersService extends SearchService
{

    static private FindTeachersService serviceInstance = new FindTeachersService();

    private FindTeachersService()
    {

    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.SearchService#cloneDomainObject(Dominio.IDomainObject)
	 */
    protected InfoObject cloneDomainObject(IDomainObject object)
    {
        ITeacher teacher = (ITeacher) object;
        return Cloner.copyITeacher2InfoTeacher(teacher);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.SearchService#doSearch(java.util.HashMap,
	 *          ServidorPersistente.ISuportePersistente)
	 */
    protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        ITeacher teacher = null;
        try
        {
            teacher =
                teacherDAO.readByNumber(new Integer(searchParameters.get("teacherNumber").toString()));
        } catch (NumberFormatException e)
        {
            //ignored
        }
        List returnList = new ArrayList();
        if (teacher != null)
        {
            returnList.add(teacher);
        }
        return returnList;
    }

    public static FindTeachersService getService()
    {
        return serviceInstance;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "FindTeachersService";
    }
}
