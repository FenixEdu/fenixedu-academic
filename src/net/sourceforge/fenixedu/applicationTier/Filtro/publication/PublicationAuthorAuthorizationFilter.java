package net.sourceforge.fenixedu.applicationTier.Filtro.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

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
            IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();

            Publication publication = (Publication) persistentObject.readByOID(Publication.class, objectId);
            
            Person possibleAuthor = persistentPerson.lerPessoaPorUsername(id.getUtilizador());

            //check if the teacher is any of the owners of the publication
            List<Authorship> authorships = new ArrayList(publication.getPublicationAuthorships());
            for (Authorship authorship : authorships) {
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