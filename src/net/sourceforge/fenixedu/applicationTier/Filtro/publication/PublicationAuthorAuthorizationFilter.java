package net.sourceforge.fenixedu.applicationTier.Filtro.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;

public class PublicationAuthorAuthorizationFilter extends DomainObjectAuthorizationFilter {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.DomainObjectAuthorizationFilter#verifyCondition(ServidorAplicacao.IUserView,
     *      java.lang.Integer)
     */
    protected boolean verifyCondition(IUserView id, Integer objectId) {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentPublication persistentPublication = sp.getIPersistentPublication();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

            IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class, objectId);
            
            IPerson possibleAuthor = persistentPerson.lerPessoaPorUsername(id.getUtilizador());

            //check if the teacher is any of the owners of the publication
            List<IAuthorship> authorships = new ArrayList(publication.getPublicationAuthorships());
            for (IAuthorship authorship : authorships) {
                if (authorship.getAuthor().equals(possibleAuthor)) {
                    return true;
                }
            }
            
            return false;

        } catch (ExcepcaoPersistencia e) {
            return false;
        }

    }
    
    protected boolean verifyCondition(IUserView id, InfoPublication infoPublication) {
        return verifyCondition(id,infoPublication.getIdInternal());
    }
}