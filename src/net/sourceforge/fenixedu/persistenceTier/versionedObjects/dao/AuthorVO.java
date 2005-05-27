package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.Predicate;

public class AuthorVO extends VersionedObjectsBase implements IPersistentAuthor {

    public IAuthor readAuthorByKeyPerson(Integer keyPerson) throws ExcepcaoPersistencia {
        final IPerson person = (IPerson) readByOID(Person.class, keyPerson);
        return person.getAuthor();
    }

    public List<IAuthor> readAuthorsBySubName(final String stringSubName) throws ExcepcaoPersistencia {

        final List<IAuthor> authors = (List<IAuthor>) readAll(Author.class);

        final String stringToMatch = stringSubName.replace("%", ".*");

        return (List<IAuthor>) CollectionUtils.select(authors, new Predicate() {

            public boolean evaluate(Object object) {
                IAuthor author = (IAuthor) object;
                //"external" authors
                if (author != null && author.getAuthor() != null
                        && author.getAuthor().toLowerCase().matches(stringToMatch.toLowerCase())) {
                    return true;
                }
                //normal authors
                if (author != null && author.getPerson() != null && author. getPerson().getNome() != null
                        && author.getPerson().getNome().toLowerCase().matches(stringToMatch.toLowerCase())) {
                    return true;
                }
                
                return false;
            }

        });
    }
}