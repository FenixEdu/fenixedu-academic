/*
 * Created on 16/Dez/2003
 *
 */
package ServidorAplicacao.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 *
 */
import java.util.Iterator;
import java.util.List;

import Dominio.Exam;
import Dominio.IExam;
import Dominio.IPeriod;
import Dominio.IRoomOccupation;
import Dominio.RoomOccupation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.notAuthorizedPersistentDeleteException;

public class DeleteExamNew implements IServico
{

    private static DeleteExamNew _servico = new DeleteExamNew();
    /**
     * The singleton access method of this class.
     **/
    public static DeleteExamNew getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     **/
    private DeleteExamNew()
    {
    }

    /**
     * Devolve o nome do servico
     **/
    public final String getNome()
    {
        return "DeleteExamNew";
    }

    public Object run(Integer examOID) throws FenixServiceException
    {

        boolean result = false;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExam examToDelete = (IExam) sp.getIPersistentExam().readByOID(Exam.class, examOID);
            List roomOccupations = examToDelete.getAssociatedRoomOccupation();

            sp.getIPersistentExam().simpleLockWrite(examToDelete);
            sp.getIPersistentExam().delete(examToDelete);
            
            Iterator iter = roomOccupations.iterator();
			IPeriod period = null;
            while (iter.hasNext())
            {
                IRoomOccupation roomOccupation = (RoomOccupation) iter.next();
				period = roomOccupation.getPeriod();
				sp.getIPersistentRoomOccupation().simpleLockWrite(roomOccupation);
				sp.getIPersistentRoomOccupation().delete(roomOccupation);
            }
            if (period != null)
			{
				sp.getIPersistentPeriod().simpleLockWrite(period);
				sp.getIPersistentPeriod().delete(period);	
			}
            result = true;
        }
        catch (notAuthorizedPersistentDeleteException ex)
        {
            throw new notAuthorizedServiceDeleteException(ex);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException("Error deleting exam");
        }

        return new Boolean(result);

    }

}