/*
 * LerAulasDeTurma.java
 * 
 * Created on 29 de Outubro de 2002, 22:18
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeTurma
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.InfoLesson;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.IAula;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeTurma implements IService
{

    /**
	 * The actor of this class.
	 */
    public LerAulasDeTurma()
    {
    }

    public List run(InfoClass infoClass)
    {
        ArrayList infoLessonList = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurnoAulaPersistente shiftLessonDAO = sp.getITurnoAulaPersistente();
            ITurmaPersistente persistentDomainClass = sp.getITurmaPersistente();
            ITurma group = null;
            if (infoClass.getIdInternal() != null)
            {
                group = new Turma(infoClass.getIdInternal());
                group = (ITurma) persistentDomainClass.readByOId(group, false);
            }
            else
            {
                group = Cloner.copyInfoClass2Class(infoClass);
            }

            List shiftList = sp.getITurmaTurnoPersistente().readByClass(group);

            Iterator iterator = shiftList.iterator();

            infoLessonList = new ArrayList();

            while (iterator.hasNext())
            {
                ITurno shift = (ITurno) iterator.next();
                List lessonList = shift.getAssociatedLessons();
                Iterator lessonIterator = lessonList.iterator();
                while (lessonIterator.hasNext())
                {
                    IAula elem = (IAula) lessonIterator.next();
                    IAula lesson = (IAula) sp.getIAulaPersistente().readByOID(Aula.class, elem.getIdInternal());
                    //InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
					InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);

                    if (infoLesson != null) {
                    	infoLessonList.add(infoLesson);
                    }

                }
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace(System.out);
        }

        return infoLessonList;
    }

}