/*
 * Created on 2004/02/17
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Aula;
import Dominio.IAula;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class CalculateFirstDayOfLesson implements IService {

	public Calendar run(Integer lessonId) throws ExcepcaoPersistencia {
	    ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
	    IAulaPersistente persistentLesson = persistentSupport.getIAulaPersistente();

	    IAula lesson = (IAula) persistentLesson.readByOID(Aula.class, lessonId);
	    Calendar startDate = lesson.getRoomOccupation().getPeriod().getStartDate();
	    return startDate;
    }

}