/*
 * ReadDegreesClassesLessons.java
 * 
 * Created on 2003/07/17
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoViewClassSchedule;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurma;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadDegreesClassesLessons implements IServico
{

    private static ReadDegreesClassesLessons _servico = new ReadDegreesClassesLessons();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadDegreesClassesLessons getService()
    {
        return _servico;
    }

    /**
	 * The actor of this class.
	 */
    private ReadDegreesClassesLessons()
    {
    }

    /**
	 * Devolve o nome do servico
	 */
    public final String getNome()
    {
        return "ReadDegreesClassesLessons";
    }

    public List run(List infoExecutionDegrees, InfoExecutionPeriod infoExecutionPeriod)
    {

        List infoViewClassScheduleList = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurmaPersistente classDAO = sp.getITurmaPersistente();
            ITurnoAulaPersistente shiftLessonDAO = sp.getITurnoAulaPersistente();

            // Read executionDegrees classes
            List classes = new ArrayList();
            for (int i = 0; i < infoExecutionDegrees.size(); i++)
            {
                List degreeClasses =
                    classDAO.readByExecutionDegreeAndExecutionPeriod(
                        Cloner.copyInfoExecutionDegree2ExecutionDegree(
                            (InfoExecutionDegree) infoExecutionDegrees.get(i)),
                        Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod));
                Iterator iterator = degreeClasses.iterator();
                CollectionUtils.addAll(classes, iterator);
            }

            for (int i = 0; i < classes.size(); i++)
            {
                InfoViewClassSchedule infoViewClassSchedule = new InfoViewClassSchedule();
                ITurma turma = (ITurma) classes.get(i);

                // read class lessons
                List shiftList = sp.getITurmaTurnoPersistente().readByClass(turma);
                Iterator iterator = shiftList.iterator();
                List infoLessonList = new ArrayList();
                while (iterator.hasNext())
                {
                    ITurno shift = (ITurno) iterator.next();
                    InfoShift infoShift = (InfoShift) Cloner.get(shift);
                    List lessonList = shiftLessonDAO.readByShift(shift);
                    Iterator lessonIterator = lessonList.iterator();
                    while (lessonIterator.hasNext())
                    {
                        IAula elem = (IAula) lessonIterator.next();
                        InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
                        infoLesson.getInfoShiftList().add(infoShift);
                        infoLessonList.add(infoLesson);
                    }
                }

                infoViewClassSchedule.setInfoClass(Cloner.copyClass2InfoClass(turma));
                infoViewClassSchedule.setClassLessons(infoLessonList);
                infoViewClassScheduleList.add(infoViewClassSchedule);
            }

        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }
        return infoViewClassScheduleList;
    }
}