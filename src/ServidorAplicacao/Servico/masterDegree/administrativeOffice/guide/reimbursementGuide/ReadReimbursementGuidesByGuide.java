/*
 * Created on 24/Mar/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuide;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuideSituation;
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.IGuide;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import Dominio.reimbursementGuide.ReimbursementGuide;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuide;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */

public class ReadReimbursementGuidesByGuide implements IService {

    /**
     * @throws FenixServiceException
     */

    public List run(Integer guideId) throws FenixServiceException {
        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            List infoReimbursementGuides = new ArrayList();

            //guide
            IPersistentGuide persistentGuide = ps.getIPersistentGuide();
            IGuide guide = (IGuide) persistentGuide.readByOID(Guide.class, guideId);

            //reimbursement Guides
            List reimbursementGuides = guide.getReimbursementGuides();
            Iterator it = reimbursementGuides.iterator();

            IPersistentReimbursementGuide persistentReimbursementGuide = ps
                    .getIPersistentReimbursementGuide();
            IReimbursementGuide reimbursementGuide = null;
            InfoReimbursementGuide infoReimbursementGuide = null;

            while (it.hasNext()) {

                reimbursementGuide = (IReimbursementGuide) persistentReimbursementGuide.readByOID(
                        ReimbursementGuide.class, ((IReimbursementGuide) it.next()).getIdInternal());

                if (reimbursementGuide == null) {
                    throw new NonExistingServiceException();
                }
                infoReimbursementGuide = Cloner
                        .copyIReimbursementGuide2InfoReimbursementGuide(reimbursementGuide);

                List guideSituations = reimbursementGuide.getReimbursementGuideSituations();
                CollectionUtils.transform(guideSituations, new Transformer() {

                    public Object transform(Object arg0) {
                        InfoReimbursementGuideSituation infoReimbursementGuideSituation = Cloner
                                .copyIReimbursementGuideSituation2InfoReimbursementGuideSituation((IReimbursementGuideSituation) arg0);
                        return infoReimbursementGuideSituation;
                    }

                });
                infoReimbursementGuide.setInfoReimbursementGuideSituations(guideSituations);

                infoReimbursementGuides.add(infoReimbursementGuide);

            }

            return infoReimbursementGuides;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}