/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.department;


import java.util.HashMap;
import java.util.List;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.Department;
import Dominio.IDepartment;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.framework.SearchService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadDepartmentTeachersService extends SearchService
{
    private static ReadDepartmentTeachersService service = new ReadDepartmentTeachersService();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadDepartmentTeachersService getService()
    {
        return service;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadDepartmentTeachersService";
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.SearchService#cloneDomainObject(Dominio.IDomainObject)
     */
    protected InfoObject cloneDomainObject(IDomainObject object)
    {
        return Cloner.copyITeacher2InfoTeacher((ITeacher) object);
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.SearchService#doSearch(java.util.HashMap, ServidorPersistente.ISuportePersistente)
     */
    protected List doSearch(HashMap searchParameters, ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        Integer departmentId = Integer.valueOf((String) searchParameters.get("idInternal"));
        IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();
        IDepartment department = (IDepartment) departmentDAO.readByOId(new Department(departmentId), false);
        
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        return teacherDAO.readByDepartment(department);
    }

}