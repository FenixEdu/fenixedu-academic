package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ApplicationDocumentType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;

public class RetrieveApplicationDocument extends Service {

    public FileSuportObject run(Integer personId, ApplicationDocumentType adt)
            throws FenixServiceException, ExcepcaoPersistencia {
        Person person = (Person) persistentObject.readByOID(Person.class, personId);

        if (adt == ApplicationDocumentType.CURRICULUM_VITAE)
            return retrieveDocumentByType(person.getSlideNameForCandidateDocuments(), "candidateCV");
        if (adt == ApplicationDocumentType.INTEREST_LETTER)
            return retrieveDocumentByType(person.getSlideNameForCandidateDocuments(), "candidateCMI");
        if (adt == ApplicationDocumentType.HABILITATION_CERTIFICATE)
            return retrieveDocumentByType(person.getSlideNameForCandidateDocuments(), "candidateCH");
        if (adt == ApplicationDocumentType.SECOND_HABILITATION_CERTIFICATE)
            return retrieveDocumentByType(person.getSlideNameForCandidateDocuments(), "candidateCH2");

        return retrieveDocumentByType(person.getSlideNameForCandidateDocuments(), "candidateCV");
    }

    private FileSuportObject retrieveDocumentByType(final String slideNameForDocuments, final String type) {
        final Collection<FileSuportObject> fileSupportObjects = JdbcMysqlFileSupport.listFiles(slideNameForDocuments);
        for (final FileSuportObject fileSuportObject : fileSupportObjects) {
            if (fileSuportObject.getFileName().indexOf(type) != -1) {
                return JdbcMysqlFileSupport.retrieveFile(slideNameForDocuments, fileSuportObject.getFileName());
            }
        }
        return null;
    }

}