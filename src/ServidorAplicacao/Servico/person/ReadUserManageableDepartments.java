/*
 * ReadMasterDegreeCandidateByUsername.java
 *
 * The Service ReadMasterDegreeCandidateByUsername reads the information of a
 * Candidate and returns it
 * 
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.person;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.DepartmentCreditsView;
import DataBeans.InfoDeparment;
import DataBeans.util.Cloner;
import Dominio.IDepartment;
import Dominio.IPessoa;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadUserManageableDepartments implements IServico {
    
    private static ReadUserManageableDepartments servico = new ReadUserManageableDepartments();
    
    /**
     * The singleton access method of this class.
     **/
    public static ReadUserManageableDepartments getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ReadUserManageableDepartments() { 
    }
    
    public final String getNome() {
        return "ReadUserManageableDepartments";
    }
    
    
    public DepartmentCreditsView run(String username)
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        IPessoa person = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 
		DepartmentCreditsView departmentCreditsView = new DepartmentCreditsView();
		
		List infoDepartmentList = new ArrayList();
		
		List manageableDepartmentsList = person.getManageableDepartmentCredits();
		
		if (manageableDepartmentsList.size() == 1) {
			IDepartment department = (IDepartment) manageableDepartmentsList.get(0);
			InfoDeparment infoDeparment = Cloner.copyIDepartment2InfoDepartment(department);
			departmentCreditsView.setListDepartment(infoDeparment);
			
			IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();
			
			List teacherList = departmentDAO.readTeacherList(department);
			List infoTeacherList = (List) CollectionUtils.collect(teacherList, new Transformer (){
				
					public Object transform(Object input) {
						ITeacher teacher = (ITeacher) input;
						return Cloner.copyITeacher2InfoTeacher(teacher);
					}});
			departmentCreditsView.setInfoTeacherList(infoTeacherList);
			
			infoDepartmentList.add(infoDeparment);
			departmentCreditsView.setInfoTeacherList(infoDepartmentList);
			
		} else  {
			infoDepartmentList = (List) CollectionUtils.collect(manageableDepartmentsList, new Transformer(){

				public Object transform(Object input) {
					IDepartment department = (IDepartment) input;
					return Cloner.copyIDepartment2InfoDepartment(department);
				}
			});			
			departmentCreditsView.setDepartmentList(infoDepartmentList);
			departmentCreditsView.setInfoTeacherList(null);
			departmentCreditsView.setListDepartment(null);
		}

		return departmentCreditsView;
    }
}