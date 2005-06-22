package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileAlreadyExistsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileNameTooLongServiceException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class StoreApplicationDocuments implements IService {

    public StoreApplicationDocuments () {}

    public Boolean[] run(FileSuportObject CVFile, FileSuportObject CMIFile, 
    		FileSuportObject CHFile, FileSuportObject CHFile2, Integer personId)
            throws FenixServiceException {

    	// create the results vector and initialize it
        Boolean [] result = new Boolean[4];
        result[0] = result[1] = result[2] = result[3] = false;
        
        try {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
        IPerson person = (IPerson) persistentPerson.readByOID(Person.class, personId);
        IFileSuport fileSuport = FileSuport.getInstance();
        String uri = person.getSlideNameForCandidateDocuments();

        // store Curriculum Vitae
        if (CVFile != null)
            result[0] = storeSingleDocument(CVFile, uri, fileSuport);

        // store Carta de Manifestacao de Interesse
        if (CMIFile != null)
            result[1] = storeSingleDocument(CMIFile, uri, fileSuport);

        // store Certificado de Habilitacoes
        if (CHFile != null)
            result[2] = storeSingleDocument(CHFile, uri, fileSuport);

        // store do segundo Certificado de Habilitacoes
        if (CHFile2 != null)
            result[3] = storeSingleDocument(CHFile2, uri, fileSuport);
        
        } catch (Exception e) { return result; }

        return result;
    }

    private Boolean storeSingleDocument (FileSuportObject file, String uri, IFileSuport fileSuport)
            throws FenixServiceException {

        file.setUri(uri);

        try {
            fileSuport.beginTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
        try {
            if (!fileSuport.isFileNameValid(file)) {
                try {
                    fileSuport.abortTransaction();
                } catch (Exception e1) {
                    throw new FenixServiceException(e1);
                }
                throw new FileNameTooLongServiceException();
            }
            if (fileSuport.isStorageAllowed(file)) {
                boolean result = fileSuport.updateFile(file);
                if (!result) {
                    try {
                        fileSuport.abortTransaction();
                    } catch (Exception e1) {
                        throw new FenixServiceException(e1);
                    }
                    throw new FileAlreadyExistsServiceException();
                }
                fileSuport.commitTransaction();
                return new Boolean(true);
            }
            fileSuport.commitTransaction();
            return new Boolean(false);

        } catch (FileNameTooLongServiceException e1) {
            throw e1;
        } catch (FileAlreadyExistsServiceException e1) {
            throw e1;
        } catch (FenixServiceException e1) {
            throw e1;
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new FenixServiceException(e1);
        }
    }
}
