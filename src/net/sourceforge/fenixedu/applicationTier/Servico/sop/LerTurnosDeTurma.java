/*
 * LerTurnosDeTurma.java
 *
 * Created on 28 de Outubro de 2002, 20:26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerTurnosDeTurma
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaTurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class LerTurnosDeTurma implements IService {

    public Object run(String className, InfoExecutionDegree infoExecutionDegree,
            InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException {

        List infoShiftAndLessons = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            ITurmaTurnoPersistente classShiftDAO = sp.getITurmaTurnoPersistente();

            IExecutionPeriod executionPeriod = Cloner
                    .copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

            ISchoolClass group = new SchoolClass();

            group.setExecutionDegree(executionDegree);
            group.setExecutionPeriod(executionPeriod);
            group.setNome(className);

            List shiftList = classShiftDAO.readByClass(group);

            Iterator iterator = shiftList.iterator();
            //			infoTurnos = new ArrayList();

            while (iterator.hasNext()) {
                IShift turno = (IShift) iterator.next();
                InfoShift infoTurno = (InfoShift) Cloner.get(turno);

                List aulas = turno.getAssociatedLessons();
                Iterator itLessons = aulas.iterator();

                List infoLessons = new ArrayList();
                InfoLesson infoLesson;

                while (itLessons.hasNext()) {
                    infoLesson = Cloner.copyILesson2InfoLesson((ILesson) itLessons.next());

                    infoLesson.setInfoShift(infoTurno);
                    infoLessons.add(infoLesson);
                }

                infoTurno.setInfoLessons(infoLessons);
                infoShiftAndLessons.add(infoTurno);

            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoShiftAndLessons;

    }

}