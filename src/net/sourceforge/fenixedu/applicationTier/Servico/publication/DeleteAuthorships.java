package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteAuthorships implements IService {

    /**
     * This method deletes all the Authorships for a given publication in the Database.
     * @param publicationId the id of the publication.
     * @throws ExcepcaoPersistencia
     */
	public void run(Integer publicationId) throws ExcepcaoPersistencia {
	    ISuportePersistente sp = null;
	    IPersistentAuthorship persistentAuthorship = null;
	    
	    sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentAuthorship = sp.getIPersistentAuthorship();
        
        List<IAuthorship> authorships = persistentAuthorship.readByPublicationId(publicationId);
        
        for (IAuthorship authorship : authorships) {
            authorship.delete();
            persistentAuthorship.simpleLockWrite(authorship);
        }
	}
}