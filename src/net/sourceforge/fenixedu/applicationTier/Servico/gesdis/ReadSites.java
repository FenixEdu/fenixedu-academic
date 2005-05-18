package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

/**
 * Serviço ObterSitios.
 * 
 * @author Joao Pereira
 * @author Ivo Brandão
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadSites implements IService {

    public List run() throws FenixServiceException {
        ISuportePersistente sp;
        Collection allSites = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            allSites = sp.getIPersistentSite().readAll(Site.class);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allSites == null || allSites.isEmpty())
            throw new InvalidArgumentsServiceException();

        // build the result of this service
        Iterator iterator = allSites.iterator();
        List result = new ArrayList(allSites.size());

        while (iterator.hasNext())
            result.add(Cloner.copyISite2InfoSite((ISite) iterator.next()));

        return result;
    }
}