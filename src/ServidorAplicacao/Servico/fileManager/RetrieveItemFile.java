/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.fileManager;

import org.apache.slide.common.SlideException;

import Dominio.IItem;
import Dominio.Item;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import fileSuport.FileSuport;
import fileSuport.FileSuportObject;
import fileSuport.IFileSuport;

/**
 *fenix-head
 *ServidorAplicacao.Servico.fileManager
 * @author João Mota
 *17/Set/2003
 *
 */
public class RetrieveItemFile implements IServico {

	private static RetrieveItemFile service = new RetrieveItemFile();

	public static RetrieveItemFile getService() {

		return service;
	}

	private RetrieveItemFile() {

	}

	public final String getNome() {

		return "RetrieveItemFile";
	}

	public FileSuportObject run(Integer itemId, String fileName)
		throws FenixServiceException {
			FileSuportObject file = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentItem persistentItem = sp.getIPersistentItem();
			IItem item = new Item(itemId);
			item = (IItem) persistentItem.readByOId(item, false);
			IFileSuport fileSuport = FileSuport.getInstance();
             try {
                fileSuport.beginTransaction();
            } catch (Exception e1) {
                throw new FenixServiceException(e1);
            }    
			 file = fileSuport.retrieveFile(item.getSlideName()+"/"+fileName);
             try {
                fileSuport.commitTransaction();
            } catch (Exception e2) {
                try {
                    fileSuport.abortTransaction();
                    throw new FenixServiceException(e2);
                } catch (Exception e3) {
                    throw new FenixServiceException(e3);
                } 
            }
             
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} catch (SlideException e) {
			throw new FenixServiceException(e);
		}
		return file;
		
	}
}
