/*
 * EditarTurno.java Created on 27 de Outubro de 2002, 21:00
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * Servi�o EditarTurno.
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;

public class EditarTurno extends Service {

    public Object run(InfoShift infoShiftOld, InfoShiftEditor infoShiftNew) {
	final Shift shiftToEdit = rootDomainObject.readShiftByOID(infoShiftOld.getIdInternal());
	final ExecutionCourse newExecutionCourse = rootDomainObject.readExecutionCourseByOID(infoShiftNew.getInfoDisciplinaExecucao().getIdInternal());
	shiftToEdit.edit(infoShiftNew.getTipos(), infoShiftNew.getLotacao(), newExecutionCourse, infoShiftNew.getNome());
	return InfoShift.newInfoFromDomain(shiftToEdit);
    }
}