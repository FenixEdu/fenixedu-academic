/*
 * Created on 16/Dez/2003
 *  
 */
package ServidorAplicacao.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 *  
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import Dominio.Exam;
import Dominio.IExam;
import Dominio.IPeriod;
import Dominio.IRoomOccupation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteExamNew implements IServico {

    private static DeleteExamNew _servico = new DeleteExamNew();

    /**
     * The singleton access method of this class.
     */
    public static DeleteExamNew getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private DeleteExamNew() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "DeleteExamNew";
    }

    public Object run(Integer examOID) throws FenixServiceException {

        boolean result = false;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExam examToDelete = (IExam) sp.getIPersistentExam().readByOID(
                    Exam.class, examOID);
            if (examToDelete == null) { throw new FenixServiceException(
                    "The exam does not exist"); }

            IPersistentExamStudentRoom persistentExamStudentRoom = sp
                    .getIPersistentExamStudentRoom();
            List examStudentRoomList = persistentExamStudentRoom
                    .readBy(examToDelete);
            if (examStudentRoomList != null && examStudentRoomList.size() > 0) { throw new notAuthorizedServiceDeleteException(
                    "error.notAuthorizedExamDelete.withStudent"); }

            List roomOccupations = examToDelete.getAssociatedRoomOccupation();
            if (roomOccupations != null && !roomOccupations.isEmpty()) {
                Iterator iter = roomOccupations.iterator();
                IPeriod period = null;
                if (!examToDelete.getAssociatedRoomOccupation().isEmpty()) {
                    period = ((IRoomOccupation) roomOccupations.get(0))
                            .getPeriod();
                }

                boolean isEmpty = false;
                List periodRoomOccupations = period.getRoomOccupations();
                Collection otherRoomOccupations = CollectionUtils.disjunction(
                        roomOccupations, periodRoomOccupations);
                isEmpty = otherRoomOccupations.isEmpty();
                while (iter.hasNext()) {
                    IRoomOccupation roomOccupation = (IRoomOccupation) iter
                            .next();
                    sp.getIPersistentRoomOccupation().delete(roomOccupation);
                }
                if (period != null) {
                    if (isEmpty) {
                        sp.getIPersistentPeriod().delete(period);
                    }
                }
            }

            sp.getIPersistentExam().delete(examToDelete);

            result = true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException("Error deleting exam");
        }

        return new Boolean(result);

    }

}