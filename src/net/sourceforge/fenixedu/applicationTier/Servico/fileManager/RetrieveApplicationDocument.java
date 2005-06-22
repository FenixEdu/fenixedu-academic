package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ApplicationDocumentType;
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

public class RetrieveApplicationDocument implements IService {

    public RetrieveApplicationDocument () {}

    public FileSuportObject run(Integer personId, ApplicationDocumentType adt) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPerson person = (IPerson) persistentPerson.readByOID(Person.class, personId);

            IFileSuport fileSuport = FileSuport.getInstance();

            if (adt == ApplicationDocumentType.CURRICULUM_VITAE)
                    return retrieveDocumentByType(fileSuport, person.getSlideNameForCandidateDocuments(), "candidateCV");
            if (adt == ApplicationDocumentType.INTEREST_LETTER)
                    return retrieveDocumentByType(fileSuport, person.getSlideNameForCandidateDocuments(), "candidateCMI");
            if (adt == ApplicationDocumentType.HABILITATION_CERTIFICATE)
                return retrieveDocumentByType(fileSuport, person.getSlideNameForCandidateDocuments(), "candidateCH");
            if (adt == ApplicationDocumentType.SECOND_HABILITATION_CERTIFICATE)
                return retrieveDocumentByType(fileSuport, person.getSlideNameForCandidateDocuments(), "candidateCH2");
            
            return retrieveDocumentByType(fileSuport, person.getSlideNameForCandidateDocuments(), "candidateCV");

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        } catch (SlideException e) {
            throw new FenixServiceException(e);
        }

    }

    private FileSuportObject retrieveDocumentByType (IFileSuport fileSuport, String slideNameForDocuments, String type)
            throws SlideException {
        FileSuportObject file = null;
        List filesList = fileSuport.getDirectoryFiles(slideNameForDocuments + "/");
        for (int iter = 0; iter < filesList.size(); iter++) {
            FileSuportObject tempFile = (FileSuportObject) filesList.get(iter);
            if (tempFile.getFileName().indexOf(type) != -1) {
                file = fileSuport.retrieveFile(slideNameForDocuments + "/" + tempFile.getFileName());
                return file;
            }
        }
        return null;
    }
}