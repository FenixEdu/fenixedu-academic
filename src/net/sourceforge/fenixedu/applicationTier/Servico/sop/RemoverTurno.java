package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço RemoverTurno
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClassShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class RemoverTurno implements IService {

    public RemoverTurno() {
    }

    public Object run(InfoShift infoShift, InfoClass infoClass) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IShift shift = Cloner.copyInfoShift2IShift(infoShift);
        ISchoolClass classTemp = Cloner.copyInfoClass2Class(infoClass);

        // Read From Database

        IShift shiftToDelete = sp.getITurnoPersistente().readByNameAndExecutionCourse(shift.getNome(),
                shift.getDisciplinaExecucao().getIdInternal());
        ISchoolClass classToDelete = sp.getITurmaPersistente()
                .readByNameAndExecutionDegreeAndExecutionPeriod(classTemp.getNome(),
                        classTemp.getExecutionDegree().getIdInternal(),
                        classTemp.getExecutionPeriod().getIdInternal());
        ISchoolClassShift turmaTurnoToDelete = null;
        if ((shiftToDelete != null) && (classToDelete != null)) {
            turmaTurnoToDelete = sp.getITurmaTurnoPersistente().readByTurmaAndTurno(
                    classToDelete.getIdInternal(), shiftToDelete.getIdInternal());
        } else
            return Boolean.FALSE;

        // Check if exists
        if (turmaTurnoToDelete != null) {

            turmaTurnoToDelete.getTurma().getSchoolClassShifts().remove(turmaTurnoToDelete);
            turmaTurnoToDelete.setTurma(null);

            turmaTurnoToDelete.getTurno().getSchoolClassShifts().remove(turmaTurnoToDelete);
            turmaTurnoToDelete.setTurno(null);

            sp.getITurmaTurnoPersistente().deleteByOID(SchoolClassShift.class,
                    turmaTurnoToDelete.getIdInternal());
           
            // sp.getITurmaTurnoPersistente().delete(turmaTurnoToDelete);
        }

        else
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

}