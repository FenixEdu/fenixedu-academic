/*
 * CriarTurno.java Created on 27 de Outubro de 2002, 18:48
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarTurno
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.ArrayList;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurno;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CriarTurno implements IService {

    public InfoShift run(InfoShift infoTurno) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        final ITurno newShift = Cloner.copyInfoShift2Shift(infoTurno);

        final ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

        final ITurno existingShift = shiftDAO.readByNameAndExecutionCourse(newShift.getNome(), newShift
                .getDisciplinaExecucao());

        if (existingShift != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoTurno.getNome());
        }

        IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

        IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoTurno
                .getInfoDisciplinaExecucao().getInfoExecutionPeriod());

        IExecutionCourse executionCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriod(infoTurno.getInfoDisciplinaExecucao()
                        .getSigla(), executionPeriod);

        sp.getITurnoPersistente().simpleLockWrite(newShift);
        Integer availabilityFinal = new Integer(new Double(Math.ceil(1.10 * newShift.getLotacao()
                .doubleValue())).intValue());
        newShift.setAvailabilityFinal(availabilityFinal);
        newShift.setAssociatedLessons(new ArrayList());
        newShift.setAssociatedClasses(new ArrayList());
        newShift.setAssociatedShiftProfessorship(new ArrayList());

        return Cloner.copyShift2InfoShift(newShift);

    }

}