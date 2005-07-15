package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class AuthorshipVO extends VersionedObjectsBase implements IPersistentAuthorship {

    public List<IAuthorship> readByPublicationId(int publicationId) {
        List<IAuthorship> authorships = (List<IAuthorship>) readAll(Authorship.class);
        List<IAuthorship> result = new ArrayList();
        for (IAuthorship authorship : authorships) {
            if (authorship.getPublication().getIdInternal() == publicationId)
            {
                result.add(authorship);
            }
        }
        return result;
    }

   
}