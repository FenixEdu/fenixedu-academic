/*
 * Created on 28/Ago/2003, 7:57:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Ago/2003, 7:57:10
 *  
 */
public class ReadStudentById implements IService {

    public ReadStudentById() {
    }

    public Object run(Integer id) throws FenixServiceException {
        InfoStudent infoStudent = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudent student = (IStudent) sp.getIPersistentStudent().readByOID(Student.class, id);
            if (student != null) {

                infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoStudent;
    }
}