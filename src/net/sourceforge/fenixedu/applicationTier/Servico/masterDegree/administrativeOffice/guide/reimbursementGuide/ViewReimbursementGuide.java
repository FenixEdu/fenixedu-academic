/*
 * Created on 20/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideSituation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
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