/*
 * ReadExamsMap.java
 * 
 * Created on 2003/05/25
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.InfoRoomExamsMap;
import DataBeans.util.Cloner;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadRoomExamsMap implements IServico
{

    private static ReadRoomExamsMap servico = new ReadRoomExamsMap();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadRoomExamsMap getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private ReadRoomExamsMap()
    {
    }

    /**
	 * Devolve o nome do servico
	 */
    public String getNome()
    {
        return "ReadRoomExamsMap";
    }

    public InfoRoomExamsMap run(InfoRoom infoRoom, InfoExecutionPeriod infoExecutionPeriod)
    {

        // Object to be returned
        InfoRoomExamsMap infoExamsMap = new InfoRoomExamsMap();

        // Exam seasons hardcoded because this information
        // is not yet available from the database
        Calendar startSeason1 = Calendar.getInstance();
        startSeason1.set(Calendar.YEAR, 2004);
        startSeason1.set(Calendar.MONTH, Calendar.JANUARY);
        startSeason1.set(Calendar.DAY_OF_MONTH, 5);
        startSeason1.set(Calendar.HOUR_OF_DAY, 0);
        startSeason1.set(Calendar.MINUTE, 0);
        startSeason1.set(Calendar.SECOND, 0);
        startSeason1.set(Calendar.MILLISECOND, 0);
        Calendar endSeason2 = Calendar.getInstance();
        endSeason2.set(Calendar.YEAR, 2004);
        endSeason2.set(Calendar.MONTH, Calendar.FEBRUARY);
        endSeason2.set(Calendar.DAY_OF_MONTH, 14);
        endSeason2.set(Calendar.HOUR_OF_DAY, 0);
        endSeason2.set(Calendar.MINUTE, 0);
        endSeason2.set(Calendar.SECOND, 0);
        endSeason2.set(Calendar.MILLISECOND, 0);

        // Set Exam Season info
        infoExamsMap.setStartSeason1(startSeason1);
        infoExamsMap.setEndSeason1(null);
        infoExamsMap.setStartSeason2(null);
        infoExamsMap.setEndSeason2(endSeason2);

        // Translate to execute following queries
        IExecutionPeriod executionPeriod =
            Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
        ISala room = Cloner.copyInfoRoom2Room(infoRoom);

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            List exams = sp.getIPersistentExam().readBy(room, executionPeriod);
            infoExamsMap.setExams((List) CollectionUtils.collect(exams, TRANSFORM_EXAM_TO_INFOEXAM));
        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }

        return infoExamsMap;
    }

    private Transformer TRANSFORM_EXAM_TO_INFOEXAM = new Transformer()
    {
        public Object transform(Object exam)
        {
            InfoExam infoExam = Cloner.copyIExam2InfoExam((IExam) exam);
            infoExam.setInfoExecutionCourse(
                Cloner.copyIExecutionCourse2InfoExecutionCourse(
                    (IExecutionCourse) ((IExam) exam).getAssociatedExecutionCourses().get(0)));
            return infoExam;
        }
    };

}