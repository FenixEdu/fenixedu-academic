/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.department;

import java.util.List;

import DataBeans.InfoDepartment;
import DataBeans.util.Cloner;
import Dominio.IDepartment;
import Dominio.IPessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadDepartmentByUser implements IServico
{
    private static ReadDepartmentByUser service = new ReadDepartmentByUser();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadDepartmentByUser getService()
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
        return "ReadDepartmentByUser";
    }

    public InfoDepartment run(String username) throws FenixServiceException
    {
        InfoDepartment infoDepartment = null;
        
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPessoa person = personDAO.lerPessoaPorUsername(username);
            List departmentList = person.getManageableDepartmentCredits();
            infoDepartment = Cloner.copyIDepartment2InfoDepartment((IDepartment) departmentList.get(0));
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }
        return infoDepartment;
    }
}