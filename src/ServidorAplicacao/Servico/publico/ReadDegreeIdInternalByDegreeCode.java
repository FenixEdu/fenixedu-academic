/*
 * Created on 11/Nov/2004
 *
 */
package ServidorAplicacao.Servico.publico;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IDegree;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Pedro Santos e Rita Carvalho
 */
public class ReadDegreeIdInternalByDegreeCode implements IService {
	
	public Integer run(String degreeCode) throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        ICursoPersistente degreeDAO = sp.getICursoPersistente();

        IDegree degree = degreeDAO.readBySigla(degreeCode);

        return degree != null ? degree.getIdInternal() : null;
	}

}
