/*
 * Created on 5/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoSiteClasses;
import DataBeans.InfoSiteTimetable;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 *  
 */
public class PublicSiteComponentBuilder
{

    private static PublicSiteComponentBuilder instance = null;

    public PublicSiteComponentBuilder()
    {
    }

    public static PublicSiteComponentBuilder getInstance()
    {
        if (instance == null)
        {
            instance = new PublicSiteComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, ITurma domainClass)
        throws FenixServiceException
    {

        if (component instanceof InfoSiteTimetable)
        {
            return getInfoSiteTimetable((InfoSiteTimetable) component, domainClass);
        }
        else if (component instanceof InfoSiteClasses)
        {
            return getInfoSiteClasses((InfoSiteClasses) component, domainClass);
        }

        return null;

    }

    /**
	 * @param classes
	 * @return
	 */
    private ISiteComponent getInfoSiteClasses(InfoSiteClasses component, ITurma domainClass)
        throws FenixServiceException
    {
        List classes = new ArrayList();
        List infoClasses = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurmaPersistente classDAO = sp.getITurmaPersistente();

            IExecutionPeriod executionPeriod = domainClass.getExecutionPeriod();
            ICursoExecucao executionDegree = domainClass.getExecutionDegree();

            classes =
                classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(
                    executionPeriod,
                    domainClass.getAnoCurricular(),
                    executionDegree);

            for (int i = 0; i < classes.size(); i++)
            {
                ITurma taux = (ITurma) classes.get(i);
                infoClasses.add(Cloner.copyClass2InfoClass(taux));
            }

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        component.setClasses(infoClasses);
        return component;
    }

    /**
	 * @param timetable
	 * @param site
	 * @return
	 */
    private ISiteComponent getInfoSiteTimetable(InfoSiteTimetable component, ITurma domainClass)
        throws FenixServiceException
    {
        ArrayList infoLessonList = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoAulaPersistente shiftLessonDAO = sp.getITurnoAulaPersistente();
            List shiftList = sp.getITurmaTurnoPersistente().readByClass(domainClass);
            Iterator iterator = shiftList.iterator();
            infoLessonList = new ArrayList();

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
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        component.setLessons(infoLessonList);
        return component;
    }

}
