/*
 * Created on 24/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGuide;
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.IGuide;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class ReadGuide implements IService {

    public InfoGuide run(Integer guideId) throws FenixServiceException {

        ISuportePersistente sp = null;
        IGuide guide;
        InfoGuide infoGuide = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
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