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
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.DepartmentTeachersDTO;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadUserManageableDepartments implements IService {

    public List run(String username) throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPerson person = suportePersistente.getIPessoaPersistente().lerPessoaPorUsername(username);

        final List departmentTeacherDTOList = new ArrayList();
        final IPersistentTeacher teacherDAO = suportePersistente.getIPersistentTeacher();

        final List manageableDepartmentsList = person.getManageableDepartmentCredits();
        Iterator departmentIterator = manageableDepartmentsList.iterator();
        while (departmentIterator.hasNext()) {
            IDepartment department = (IDepartment) departmentIterator.next();
            InfoDepartment infoDeparment = Cloner.copyIDepartment2InfoDepartment(department);
            List deparmentTeacherList = teacherDAO.readByDepartment(department.getCode());

            List infoTeacherList = (List) CollectionUtils.collect(deparmentTeacherList,
                    new Transformer() {
                        public Object transform(Object input) {
                            ITeacher teacher = (ITeacher) input;
                            return Cloner.copyITeacher2InfoTeacher(teacher);
                        }
                    });
            DepartmentTeachersDTO departmentTeachersDTO = new DepartmentTeachersDTO();
            departmentTeachersDTO.setInfoDepartment(infoDeparment);
            departmentTeachersDTO.setInfoTeacherList(infoTeacherList);

            departmentTeacherDTOList.add(departmentTeachersDTO);
        }
        return departmentTeacherDTOList;
    }
}