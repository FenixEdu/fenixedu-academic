package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * 
 * @author Fernanda Quitério 1/Mar/2004
 * 
 */
public class ReadStudentByNumberAndAllDegreeTypes implements IService {

    public Object run(Integer number) throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        InfoStudent infoStudent = null;

        IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                TipoCurso.LICENCIATURA_OBJ);

        if (student == null) {
            student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                    TipoCurso.MESTRADO_OBJ);
        }
        if (student != null) {
            infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
        }

        return infoStudent;
    }

}