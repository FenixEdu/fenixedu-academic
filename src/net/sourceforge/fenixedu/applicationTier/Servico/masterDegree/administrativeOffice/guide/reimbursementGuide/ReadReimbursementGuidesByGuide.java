/*
 * Created on 24/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideSituation;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
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