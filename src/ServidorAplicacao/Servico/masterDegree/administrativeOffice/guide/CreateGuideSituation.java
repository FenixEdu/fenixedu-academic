package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.util.Date;
import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Guide;
import Dominio.GuideSituation;
import Dominio.IGuide;
import Dominio.IGuideSituation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.SituationOfGuide;
import Util.State;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGuideSituation implements IService {

    public void run(Integer guideID, String remarks, SituationOfGuide situation, Date date)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IGuide guide = (IGuide) sp.getIPersistentGuide().readByOID(Guide.class, guideID, true);

        IGuideSituation guideSituation = new GuideSituation(situation, remarks, date, guide, new State(
                State.ACTIVE));

        sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);

        for (Iterator iter = guide.getGuideSituations().iterator(); iter.hasNext();) {
            IGuideSituation guideSituationTmp = (IGuideSituation) iter.next();
            guideSituationTmp.setState(new State(State.INACTIVE));
            sp.getIPersistentGuideSituation().simpleLockWrite(guideSituationTmp);

        }

        guide.getGuideSituations().add(guideSituation);

    }

}