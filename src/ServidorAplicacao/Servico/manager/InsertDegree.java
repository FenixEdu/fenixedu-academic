/*
 * Created on 30/Mai/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegree;
import Dominio.Curso;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoCurso;

/**
 * @author lmac1
 */

public class InsertDegree implements IService {

    public InsertDegree() {
    }

    public void run(InfoDegree infoDegree) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            ICursoPersistente persistentDegree = persistentSuport.getICursoPersistente();

            String code = infoDegree.getSigla();
            String name = infoDegree.getNome();
            TipoCurso type = infoDegree.getTipoCurso();

            ICurso degree = new Curso();
            persistentDegree.simpleLockWrite(degree);
            degree.setSigla(code);
            degree.setNome(name);
            degree.setTipoCurso(type);
            degree.setConcreteClassForDegreeCurricularPlans(DegreeCurricularPlan.class.getName());

        } catch (ExistingPersistentException existingException) {
            throw new ExistingServiceException(existingException);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}