/*
 * Created on 20/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuide;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuideEntry;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuideSituation;
import DataBeans.util.Cloner;
import Dominio.reimbursementGuide.IReimbursementGuide;
import Dominio.reimbursementGuide.IReimbursementGuideEntry;
import Dominio.reimbursementGuide.IReimbursementGuideSituation;
import Dominio.reimbursementGuide.ReimbursementGuide;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.guide.IPersistentReimbursementGuide;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 * 
 * <br>
 * <strong>Description: </strong> <br>
 * Standard reading service using the ID to identify the object
 * 
 *  
 */
public class ViewReimbursementGuide implements IService {

    /**
     * @throws FenixServiceException
     */

    public InfoReimbursementGuide run(Integer reimbursementGuideId) throws FenixServiceException {
        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentReimbursementGuide persistentReimbursementGuide = ps
                    .getIPersistentReimbursementGuide();
            IReimbursementGuide reimbursementGuide = (IReimbursementGuide) persistentReimbursementGuide
                    .readByOID(ReimbursementGuide.class, reimbursementGuideId);

            if (reimbursementGuide == null) {
                throw new NonExistingServiceException();
            }
            InfoReimbursementGuide infoReimbursementGuide = Cloner
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

            List reibursementGuideEntries = reimbursementGuide.getReimbursementGuideEntries();
            CollectionUtils.transform(reibursementGuideEntries, new Transformer() {

                public Object transform(Object arg0) {
                    InfoReimbursementGuideEntry infoReimbursementGuideEntry = Cloner
                            .copyIReimbursementGuideEntry2InfoReimbursementGuideEntry((IReimbursementGuideEntry) arg0);
                    return infoReimbursementGuideEntry;
                }

            });
            infoReimbursementGuide.setInfoReimbursementGuideEntries(reibursementGuideEntries);

            return infoReimbursementGuide;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}