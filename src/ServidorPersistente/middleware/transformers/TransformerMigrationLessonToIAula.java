/*
 * Created on 1/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.transformers;

import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.collections.Transformer;

import Dominio.Aula;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.ISala;
import ServidorPersistente.middleware.MigrationExecutionCourse;
import ServidorPersistente.middleware.MigrationLesson;
import ServidorPersistente.middleware.Utils.LessonTypeUtils;
import Util.DiaSemana;

/**
 * @author jpvl
 */
public class TransformerMigrationLessonToIAula implements Transformer
{

    private HashMap roomHashMap;

    public TransformerMigrationLessonToIAula(HashMap roomHashMap)
    {
        this.roomHashMap = roomHashMap;

    }

    /* (non-Javadoc)
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object obj)
    {
        IAula lesson = null;
        if (obj instanceof MigrationLesson)
        {
            MigrationLesson migrationLesson = (MigrationLesson) obj;
            if (migrationLesson.getLesson() == null)
            {

                lesson = new Aula();

                lesson.setInicio(convertHourToCalendar(migrationLesson.getStartIndex(), true));
                lesson.setFim(convertHourToCalendar(migrationLesson.getEndIndex(), false));
                lesson.setDiaSemana(conversorDiaSemana(migrationLesson.getDay()));
                lesson.setTipo(LessonTypeUtils.convertLessonType(migrationLesson.getLessonType()));
                lesson.setSala(getRoomFromName(migrationLesson.getRoom()));

                MigrationExecutionCourse migrationExecutionCourse =
                    migrationLesson.getMigrationExecutionCourse();
                TransformerMigrationExecutionCourse2ExecutionCourse transf =
                    new TransformerMigrationExecutionCourse2ExecutionCourse();
                IExecutionCourse executionCourse =
                    (IExecutionCourse) transf.transform(migrationExecutionCourse);
                lesson.setDisciplinaExecucao(executionCourse);

                migrationLesson.setLesson(lesson);
            } else
            {
                lesson = migrationLesson.getLesson();
            }
            return lesson;
        }
        return null;
    }

    private ISala getRoomFromName(String roomName)
    {
        ISala room = (ISala) roomHashMap.get(roomName);
        if (room != null)
            return room;
        else
            throw new IllegalArgumentException("Unknown room:" + roomName);
    }

    private Calendar convertHourToCalendar(int hora, boolean start)
    {
        Calendar calendar = Calendar.getInstance();

        double aux = 8.0 + ((double) (hora - 1) / 2);

        int hour = (int) Math.floor(aux);
        int minutes = (int) ((aux - hour) * 60);
        if (!start)
        {
            if (minutes == 0)
            {
                minutes = 30;
            } else
            {
                hour++;
                minutes = 0;
            }

        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    private DiaSemana conversorDiaSemana(int day)
    {
        int[] diasSemana =
            {
                DiaSemana.SEGUNDA_FEIRA,
                DiaSemana.TERCA_FEIRA,
                DiaSemana.QUARTA_FEIRA,
                DiaSemana.QUINTA_FEIRA,
                DiaSemana.SEXTA_FEIRA,
                DiaSemana.SABADO,
                DiaSemana.DOMINGO };
        return new DiaSemana(diasSemana[day - 1]);
    }

}
