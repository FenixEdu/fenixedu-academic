/*
 * LerAula.java
 * 
 * Created on December 16th, 2002, 1:58
 */

package ServidorAplicacao.Servico.student;

/**
 * Serviço LerAluno.
 * 
 * @author tfc130
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadStudentByUsername implements IService {

    /**
     * The actor of this class.
     */
    public ReadStudentByUsername() {
    }

    public Object run(String username) throws FenixServiceException {

        InfoStudent infoStudent = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudent student = sp.getIPersistentStudent().readByUsername(
                    username);

            if (student != null) {
                infoStudent =copyIStudent2InfoStudent(student);
              
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            throw new FenixServiceException(ex);
        }

        return infoStudent;
    }
    private InfoStudent copyIStudent2InfoStudent(IStudent student) {
        InfoStudent infoStudent = null;
        if (student != null) {
            infoStudent = new InfoStudent();
            infoStudent.setIdInternal(student.getIdInternal());
            infoStudent.setNumber(student.getNumber());

        }
        return infoStudent;
    }
}