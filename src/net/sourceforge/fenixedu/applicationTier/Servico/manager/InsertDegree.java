/*
 * Created on 30/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class InsertDegree implements IService {

    public InsertDegree() {
    }

    public void run(InfoDegree infoDegree) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ICursoPersistente persistentDegree = persistentSuport.getICursoPersistente();

            String code = infoDegree.getSigla();
            String name = infoDegree.getNome();
            TipoCurso type = infoDegree.getTipoCurso();

            IDegree degree = new Degree();
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