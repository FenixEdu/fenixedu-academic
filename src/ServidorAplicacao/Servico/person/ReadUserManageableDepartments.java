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

package ServidorAplicacao.Servico.person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.DepartmentTeachersDTO;
import DataBeans.InfoDepartment;
import DataBeans.util.Cloner;
import Dominio.IDepartment;
import Dominio.IPerson;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            sp = SuportePersistenteOJB.getInstance();
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