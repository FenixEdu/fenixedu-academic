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

import net.sourceforge.fenixedu.applicationTier.IServico;
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
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadUserManageableDepartments implements IServico {

    private static ReadUserManageableDepartments servico = new ReadUserManageableDepartments();

    /**
     * The singleton access method of this class.
     */
    public static ReadUserManageableDepartments getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadUserManageableDepartments() {
    }

    public final String getNome() {
        return "ReadUserManageableDepartments";
    }

    public List run(String username) throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        IPerson person = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        List departmentTeacherDTOList = new ArrayList();

        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

        List manageableDepartmentsList = person.getManageableDepartmentCredits();
        Iterator departmentIterator = manageableDepartmentsList.iterator();
        while (departmentIterator.hasNext()) {
            IDepartment department = (IDepartment) departmentIterator.next();
            InfoDepartment infoDeparment = Cloner.copyIDepartment2InfoDepartment(department);
            List deparmentTeacherList;
            try {
                deparmentTeacherList = teacherDAO.readByDepartment(department);
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
                throw new FenixServiceException(e);
            }

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