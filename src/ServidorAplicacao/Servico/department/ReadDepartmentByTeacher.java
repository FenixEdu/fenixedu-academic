/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.department;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDepartment;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IDepartment;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadDepartmentByTeacher implements IService {

    public InfoDepartment run(InfoTeacher infoTeacher) throws ExcepcaoPersistencia {
        InfoDepartment infoDepartment = null;

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentDepartment departmentDAO = sp.getIDepartamentoPersistente();

        ITeacher teacher = Cloner.copyInfoTeacher2Teacher(infoTeacher);

        IDepartment department = departmentDAO.readByTeacher(teacher);
        infoDepartment = Cloner.copyIDepartment2InfoDepartment(department);

        return infoDepartment;
    }
}