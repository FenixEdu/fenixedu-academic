/*
 * ReadExamsMapByRoom.java
 * 
 * Created on 2004/02/19
 */

package ServidorAplicacao.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.InfoRoomExamsMap;
import DataBeans.util.Cloner;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRoomOccupation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExamsMapByRooms implements IServico
{

    private static ReadExamsMapByRooms servico = new ReadExamsMapByRooms();
    /**
     * The singleton access method of this class.
     */
    public static ReadExamsMapByRooms getService()
    {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadExamsMapByRooms()
    {
    }

    /**
     * Devolve o nome do servico
     */
    public String getNome()
    {
        return "ReadExamsMapByRooms";
    }

    public List run(InfoExecutionPeriod infoExecutionPeriod, List infoRooms)
    {

        // Object to be returned
        List infoRoomExamMapList = new ArrayList();

        // Exam seasons hardcoded because this information
        // is not yet available from the database
        // TODO ISTO NÂO PODE TAR HARDCODED
        Calendar startSeason1 = Calendar.getInstance();
        startSeason1.set(Calendar.YEAR, 2004);
        startSeason1.set(Calendar.MONTH, Calendar.JUNE);
        startSeason1.set(Calendar.DAY_OF_MONTH, 14);
        startSeason1.set(Calendar.HOUR_OF_DAY, 0);
        startSeason1.set(Calendar.MINUTE, 0);
        startSeason1.set(Calendar.SECOND, 0);
        startSeason1.set(Calendar.MILLISECOND, 0);
        Calendar endSeason2 = Calendar.getInstance();
        endSeason2.set(Calendar.YEAR, 2004);
        endSeason2.set(Calendar.MONTH, Calendar.JULY);
        endSeason2.set(Calendar.DAY_OF_MONTH, 24);
        endSeason2.set(Calendar.HOUR_OF_DAY, 0);
        endSeason2.set(Calendar.MINUTE, 0);
        endSeason2.set(Calendar.SECOND, 0);
        endSeason2.set(Calendar.MILLISECOND, 0);		

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            //List rooms = sp.getISalaPersistente().readForRoomReservation();
			InfoRoom room = null;
			InfoRoomExamsMap infoExamsMap = null;
			
			// Translate to execute following queries
			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

            for (int i = 0; i < infoRooms.size(); i++)
            {				
                room = (InfoRoom) infoRooms.get(i);

                infoExamsMap = new InfoRoomExamsMap();

                // Set Exam Season info
                infoExamsMap.setInfoRoom(room);
                infoExamsMap.setStartSeason1(startSeason1);
                infoExamsMap.setEndSeason1(null);
                infoExamsMap.setStartSeason2(null);
                infoExamsMap.setEndSeason2(endSeason2);

                List exams =
                    sp.getIPersistentExam().readByRoomAndExecutionPeriod(Cloner.copyInfoRoom2Room(room), executionPeriod);
                infoExamsMap.setExams((List) CollectionUtils.collect(exams, TRANSFORM_EXAM_TO_INFOEXAM));

                infoRoomExamMapList.add(infoExamsMap);
            }

        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }

        return infoRoomExamMapList;
    }

    private Transformer TRANSFORM_EXAM_TO_INFOEXAM = new Transformer()
    {
        public Object transform(Object exam)
        {
            InfoExam infoExam = Cloner.copyIExam2InfoExam((IExam) exam);
            infoExam.setInfoExecutionCourse(
                (InfoExecutionCourse) Cloner.get(
                    (IExecutionCourse) ((IExam) exam).getAssociatedExecutionCourses().get(0)));
            return infoExam;
        }
    };

}