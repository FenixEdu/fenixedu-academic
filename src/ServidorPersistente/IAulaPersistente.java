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

import Dominio.ILesson;
import Dominio.IExecutionPeriod;
import Dominio.IRoom;
import Dominio.IShift;
import Util.DiaSemana;
import Util.TipoAula;

public interface IAulaPersistente extends IPersistentObject {
    public ILesson readByDiaSemanaAndInicioAndFimAndSala(DiaSemana diaSemana, Calendar inicio,
            Calendar fim, IRoom sala, IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(ILesson aula) throws ExcepcaoPersistencia;

    public List readByRoomAndExecutionPeriod(IRoom room, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readByRoomNamesAndExecutionPeriod(List roomNames, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readLessonsInBroadPeriod(ILesson newLesson, ILesson oldLesson,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia;

    public List readLessonsInBroadPeriodInAnyRoom(ILesson lesson, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readLessonsByStudent(String username) throws ExcepcaoPersistencia;

    public List readLessonsByShift(IShift shift) throws ExcepcaoPersistencia;

    public List readLessonsByShiftAndLessonType(IShift shift, TipoAula lessonType)
            throws ExcepcaoPersistencia;

}