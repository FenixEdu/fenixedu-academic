package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.publication.IPublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.PublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class PublicationAuthorVO extends VersionedObjectsBase implements IPersistentPublicationAuthor{

    public List readByPublicationId(final Integer publicationId) throws ExcepcaoPersistencia{
        final List<IPublicationAuthor> publicationAuthors = (List<IPublicationAuthor>)this.readAll(PublicationAuthor.class);
        return (List<IPublicationAuthor>) CollectionUtils.select(publicationAuthors, new Predicate() {
            public boolean evaluate(Object object) {
                if (((IPublicationAuthor) object).getPublication().getIdInternal().equals(publicationId)) {
                    return true;
                }
                return false;
            }
        });
    }
    
}
