package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExam;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.Sala;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRooms implements IServico {

	ISuportePersistente sp = null;
	ISalaPersistente persistentRoom = null;
	IPersistentExam persistentExam = null;

	private static EditExamRooms serviceInstance = new EditExamRooms();
	/**
	 * The singleton access method of this class.
	 **/
	public static EditExamRooms getService() {
		return serviceInstance;
	}

	/**
	 * The actor of this class.
	 **/
	private EditExamRooms() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EditExamRooms";
	}

	public InfoExam run(InfoExam infoExam, final List roomsForExam)
		throws FenixServiceException {

		//RemovalAwareCollection
		//ManageableCollection

		ServiceSetUp();

		List finalRoomList = new ArrayList();
		CollectionUtils.collect(roomsForExam, TRANSFORM_ROOMID_TO_ROOM, finalRoomList);

		try {
			Exam examQuery = new Exam();
			examQuery.setIdInternal(infoExam.getIdInternal());
			final Exam exam = (Exam) persistentRoom.readByOId(examQuery);
			persistentExam.lockWrite(exam);

			// Remove all elements
			exam.getAssociatedRooms().clear();
//			List tempRooms = exam.getAssociatedRooms();
//			for (int i = 0; i < tempRooms.size(); i++) {
//				exam.getAssociatedRooms().remove(tempRooms.get(i));
//			}
			
			// Add all elements
			CollectionUtils.forAllDo(finalRoomList, new Closure() {
				public void execute(Object objRoom) {
					exam.getAssociatedRooms().add(objRoom);
				}
			});

			return Cloner.copyIExam2InfoExam(exam);

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new FenixServiceException(e);
		}

	}

	private void ServiceSetUp() throws FenixServiceException {
		try {
			sp = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e1) {
			e1.printStackTrace(System.out);
			throw new FenixServiceException();
		}
		persistentRoom = sp.getISalaPersistente();
		persistentExam = sp.getIPersistentExam();
	}

	private Transformer TRANSFORM_ROOMID_TO_ROOM = new Transformer() {
		public Object transform(Object id) {
			Sala room = new Sala();
			room.setIdInternal((Integer) id);
			try {
				return persistentRoom.readByOId(room);
			} catch (ExcepcaoPersistencia e) {
				return null;
			}
		}
	};

}