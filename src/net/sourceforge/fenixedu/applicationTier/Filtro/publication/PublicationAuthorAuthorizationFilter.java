package net.sourceforge.fenixedu.applicationTier.Filtro.publication;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.Publication;

public class PublicationAuthorAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    protected boolean verifyCondition(IUserView id, Integer objectId) {
        final Publication publication = (Publication) rootDomainObject.readPublicationByOID(objectId);
        final Person possibleAuthor = id.getPerson();
        for (final Authorship authorship : publication.getPublicationAuthorships()) {
            if (authorship.getAuthor() == possibleAuthor) {
                return true;
            }
        }
        return false;
    }
    
    protected boolean verifyCondition(IUserView id, InfoPublication infoPublication) {
        return verifyCondition(id,infoPublication.getIdInternal());
    }

}