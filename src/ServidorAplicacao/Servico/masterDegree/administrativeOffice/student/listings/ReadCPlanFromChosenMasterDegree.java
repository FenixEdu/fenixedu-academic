package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCPlanFromChosenMasterDegree implements IService {

    public List run(Integer idInternal) throws FenixServiceException {
        ISuportePersistente sp = null;
        List degreeCurricularPlansList = new ArrayList();
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Get the Master Degree
            ICurso degree = null;
            degree = sp.getICursoPersistente().readByIdInternal(idInternal);

            // Get the List of Degree Curricular Plans
            degreeCurricularPlansList = degree.getDegreeCurricularPlans();
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        Iterator iterator = degreeCurricularPlansList.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            result.add(Cloner
                    .copyIDegreeCurricularPlan2InfoDegreeCurricularPlan((IDegreeCurricularPlan) iterator
                            .next()));
        }

        return result;
    }

}