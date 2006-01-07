package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class AuthorshipVO extends VersionedObjectsBase implements IPersistentAuthorship {

    public List<Authorship> readByPublicationId(int publicationId) {
        List<Authorship> authorships = (List<Authorship>) readAll(Authorship.class);
        List<Authorship> result = new ArrayList();
        for (Authorship authorship : authorships) {
            if (authorship.getPublication().getIdInternal() == publicationId)
            {
                result.add(authorship);
            }
        }
        return result;
    }

   
}