package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

public class ReadStudentByNumberAndDegreeType implements IService {

    public Object run(Integer number, TipoCurso degreeType) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        final IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                degreeType);

        if (student != null) {
            return InfoStudentWithInfoPerson.newInfoFromDomain(student);
        }

        return null;
    }

}