package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAuthorsToInsert implements IService {

    public List<IPerson> run(List<Integer> idsInternalAuthors) throws FenixServiceException {
        List<IPerson> authors = new ArrayList<IPerson>();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

            for (Integer authorId : idsInternalAuthors) {
                IPerson author = (IPerson) persistentPerson.readByOID(Person.class, authorId);
                authors.add(author);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return authors;
    }

}