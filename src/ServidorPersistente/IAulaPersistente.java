/*
 * IAulaPersistente.java
 *
 * Created on 17 de Outubro de 2002, 20:55
 */

package ServidorPersistente;

/**
 * 
 * @author tfc130
 */
import java.util.Calendar;
import java.util.List;

import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import Dominio.ITurno;
import Util.DiaSemana;
import Util.TipoAula;

public interface IAulaPersistente extends IPersistentObject {
    public IAula readByDiaSemanaAndInicioAndFimAndSala(DiaSemana diaSemana, Calendar inicio,
            Calendar fim, ISala sala, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IAula aula) throws ExcepcaoPersistencia;

    public List readByRoomAndExecutionPeriod(ISala room, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readByRoomNamesAndExecutionPeriod(List roomNames, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readLessonsInBroadPeriod(IAula newLesson, IAula oldLesson,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readLessonsInBroadPeriodInAnyRoom(IAula lesson, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readLessonsByStudent(String username) throws ExcepcaoPersistencia;

    public List readLessonsByShift(ITurno shift) throws ExcepcaoPersistencia;

    public List readLessonsByShiftAndLessonType(ITurno shift, TipoAula lessonType)
            throws ExcepcaoPersistencia;

}