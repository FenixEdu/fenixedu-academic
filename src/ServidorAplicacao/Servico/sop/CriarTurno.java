/*
 * CriarTurno.java Created on 27 de Outubro de 2002, 18:48
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarTurno
 * 
 * @author tfc130
 */
import java.util.ArrayList;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CriarTurno implements IService {

    /**
     * The actor of this class.
     */
    public CriarTurno() {
    }

    public InfoShift run(InfoShift infoTurno) throws FenixServiceException {

        ITurno turno = null;
        InfoShift result = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

            IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoTurno
                    .getInfoDisciplinaExecucao().getInfoExecutionPeriod());

            IExecutionCourse executionCourse = executionCourseDAO
                    .readByExecutionCourseInitialsAndExecutionPeriod(infoTurno
                            .getInfoDisciplinaExecucao().getSigla(), executionPeriod);

            turno = new Turno(infoTurno.getNome(), infoTurno.getTipo(), infoTurno.getLotacao(),
                    executionCourse);
            try {
                sp.getITurnoPersistente().simpleLockWrite(turno);
                // TODO : this is required to write shifts.
                //        I'm not sure of the significance, nor do I know if it is to
                //        be attributed by SOP users. So for now just set it to 0.
                Integer availabilityFinal = new Integer(new Double(Math.ceil(1.10 * turno.getLotacao()
                        .doubleValue())).intValue());
                turno.setAvailabilityFinal(availabilityFinal);
                turno.setAssociatedLessons(new ArrayList());
                turno.setAssociatedClasses(new ArrayList());
                turno.setAssociatedShiftProfessorship(new ArrayList());

            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            }

            result = (InfoShift) Cloner.get(turno);
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return result;
    }

}