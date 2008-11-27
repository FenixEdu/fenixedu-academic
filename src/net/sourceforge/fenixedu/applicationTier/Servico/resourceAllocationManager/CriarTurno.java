/*
 * CriarTurno.java Created on 27 de Outubro de 2002, 18:48
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * Servi�o CriarTurno
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;

public class CriarTurno extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static InfoShift run(InfoShiftEditor infoTurno) {
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoTurno.getInfoDisciplinaExecucao()
		.getIdInternal());
	final Shift newShift = new Shift(executionCourse, infoTurno.getTipos(), infoTurno.getLotacao());
	return InfoShift.newInfoFromDomain(newShift);
    }
}