/*
 * CriarTurno.java Created on 27 de Outubro de 2002, 18:48
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * Servi�o CriarTurno
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;

public class CriarTurno extends Service {

    public InfoShift run(InfoShiftEditor infoTurno) {
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoTurno.getInfoDisciplinaExecucao()
		.getIdInternal());
	final Shift newShift = new Shift(executionCourse, infoTurno.getTipos(), infoTurno.getLotacao());
	return InfoShift.newInfoFromDomain(newShift);
    }
}
