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
public class StoreItemFile implements IServico {

	private static StoreItemFile service = new StoreItemFile();

	public static StoreItemFile getService() {

		return service;
	}

	private StoreItemFile() {

	}

	public final String getNome() {

		return "StoreItemFile";
	}

	public Boolean run(FileSuportObject file,Integer itemId)
		throws FenixServiceException {
			
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentItem persistentItem = sp.getIPersistentItem();
			IItem item = new Item(itemId);
			item = (IItem) persistentItem.readByOId(item, false);
			file.setUri(item.getSlideName());
			file.setRootUri(item.getSection().getSite().getExecutionCourse().getSlideName());
			IFileSuport fileSuport = FileSuport.getInstance();
			if (fileSuport.isStorageAllowed(file)) {
				fileSuport.storeFile(file);
				return new Boolean(true);
			}else {
				return new Boolean(false);
			}
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} catch (SlideException e) {
			throw new FenixServiceException(e);
		}
		
		
	}
}
