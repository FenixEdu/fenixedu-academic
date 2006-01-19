/*
 * Created on Aug 10, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class RetrievePhoto implements IService {

    public FileSuportObject run(Integer personId) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        final Person person = (Person) persistentPerson.readByOID(Person.class, personId);
        final Collection<FileSuportObject> fileSupportObjects = JdbcMysqlFileSupport.listFiles(person.getSlideName());
        for (final FileSuportObject fileSupportObject : fileSupportObjects) {
            if (fileSupportObject.getFileName().indexOf("personPhoto") != -1) {
                return JdbcMysqlFileSupport.retrieveFile(person.getSlideName(), fileSupportObject.getFileName());
            }
        }
        return null;
    }

}