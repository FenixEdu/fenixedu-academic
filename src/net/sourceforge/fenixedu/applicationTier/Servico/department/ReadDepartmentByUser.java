/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadDepartmentByUser implements IService {
    public ReadDepartmentByUser() {
    }

    public InfoDepartment run(String username) throws FenixServiceException {
        InfoDepartment infoDepartment = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPessoaPersistente personDAO = sp.getIPessoaPersistente();
            IPerson person = personDAO.lerPessoaPorUsername(username);
            List departmentList = person.getManageableDepartmentCredits();
            if (departmentList != null && !departmentList.isEmpty()) {
                infoDepartment = Cloner.copyIDepartment2InfoDepartment((IDepartment) departmentList
                        .get(0));
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException("Problems on database!", e);
        }
        return infoDepartment;
    }
}