/*
 * Created on Aug 10, 2004
 *
 */
package ServidorAplicacao.Servico.fileManager;

import java.util.List;

import org.apache.slide.common.SlideException;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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
public class RetrievePhoto implements IService {

    public RetrievePhoto() {

    }

    public FileSuportObject run(Integer personId) throws FenixServiceException {
        FileSuportObject file = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPessoa person = (IPessoa) persistentPerson.readByOID(Pessoa.class, personId);

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