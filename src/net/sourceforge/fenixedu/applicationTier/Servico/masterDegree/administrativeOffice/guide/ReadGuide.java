/*
 * Created on 24/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class ReadGuide implements IService {

    public InfoGuide run(Integer guideId) throws FenixServiceException {

        ISuportePersistente sp = null;
        IGuide guide;
        InfoGuide infoGuide = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            guide = (IGuide) sp.getIPersistentGuide().readByOID(Guide.class, guideId);
            if (guide == null) {
                throw new InvalidArgumentsServiceException();
            }
            infoGuide = Cloner.copyIGuide2InfoGuide(guide);

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return infoGuide;
    }

}