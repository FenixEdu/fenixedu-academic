/*
 * Created on Aug 10, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.slide.common.SlideException;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class RetrievePhoto implements IService {

    public RetrievePhoto() {

    }

    public FileSuportObject run(Integer personId) throws FenixServiceException {
        FileSuportObject file = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPerson person = (IPerson) persistentPerson.readByOID(Person.class, personId);

            IFileSuport fileSuport = FileSuport.getInstance();

            List filesList = fileSuport.getDirectoryFiles(person.getSlideName() + "/");
            for (int iter = 0; iter < filesList.size(); iter++) {
                FileSuportObject tempFile = (FileSuportObject) filesList.get(iter);
                if (tempFile.getFileName().indexOf("personPhoto") != -1) {
                    file = fileSuport.retrieveFile(person.getSlideName() + "/" + tempFile.getFileName());
                    return file;
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (SlideException e) {
            throw new FenixServiceException(e);
        }
        return null;

    }
}