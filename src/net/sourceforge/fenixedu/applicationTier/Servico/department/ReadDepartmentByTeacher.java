/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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