/*
 * AdicionarTurno.java Created on 27 de Outubro de 2002, 12:31
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço AdicionarTurno.
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClassShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AdicionarTurno implements IService {

    /**
     * The actor of this class.
     */
    public AdicionarTurno() {
    }

    public Boolean run(InfoClass infoClass, InfoShift infoShift) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISchoolClassShift turmaTurno = null;
        boolean result = false;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoClass
                .getInfoExecutionPeriod());
        IExecutionDegree executionDegree = Cloner.copyInfoExecutionDegree2ExecutionDegree(infoClass
                .getInfoExecutionDegree());
        IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift
                .getInfoDisciplinaExecucao());

        ISchoolClass group = sp.getITurmaPersistente().readByNameAndExecutionDegreeAndExecutionPeriod(
                infoClass.getNome(), executionDegree.getIdInternal(), executionPeriod.getIdInternal());
        IShift shift = sp.getITurnoPersistente().readByNameAndExecutionCourse(infoShift.getNome(),
                executionCourse.getIdInternal());

        turmaTurno = new SchoolClassShift(group, shift);

        try {
            sp.getITurmaTurnoPersistente().simpleLockWrite(turmaTurno);
        } catch (ExistingPersistentException e) {
            throw new ExistingServiceException(e);
        }

        result = true;

        return new Boolean(result);
    }

}