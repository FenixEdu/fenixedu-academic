package ServidorAplicacao.Servico.publico;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegree;
import Dominio.Curso;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Luis Cruz
 */
public class ReadDegreeByOID implements IService {

    public InfoDegree run(Integer degreeId) throws ExcepcaoPersistencia {
        ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
        ICursoPersistente persistentDegree = suportePersistente.getICursoPersistente();

        ICurso degree = (ICurso) persistentDegree.readByOID(Curso.class, degreeId);

        InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
        return infoDegree;
    }
}