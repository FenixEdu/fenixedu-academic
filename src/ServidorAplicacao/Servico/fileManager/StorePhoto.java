/*
 * Created on Aug 10, 2004
 *
 */
package ServidorAplicacao.Servico.fileManager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPerson;
import Dominio.Person;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.FileAlreadyExistsServiceException;
import ServidorAplicacao.Servico.exceptions.FileNameTooLongServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import fileSuport.FileSuport;
import fileSuport.FileSuportObject;
import fileSuport.IFileSuport;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class StorePhoto implements IService {

    public StorePhoto() {

    }

    public Boolean run(FileSuportObject file, Integer personId) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPerson person = (IPerson) persistentPerson.readByOID(Person.class, personId);

            file.setUri(person.getSlideName());
            IFileSuport fileSuport = FileSuport.getInstance();
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
                    boolean result = fileSuport.updateFile(file);//storeFile(file);
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
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}