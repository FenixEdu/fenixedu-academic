/*
 * LerAlunosDeTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:41
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * Servi�o LerAlunosDeTurno.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;

public class LerAlunosDeTurno extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static List<InfoStudent> run(ShiftKey keyTurno) {

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(keyTurno.getInfoExecutionCourse()
		.getIdInternal());
	final Shift shift = executionCourse.findShiftByName(keyTurno.getShiftName());

	List<Registration> alunos = shift.getStudents();

	List<InfoStudent> infoAlunos = new ArrayList<InfoStudent>(alunos.size());
	for (final Iterator<Registration> iterator = alunos.iterator(); iterator.hasNext();) {
	    Registration elem = iterator.next();
	    infoAlunos.add(new InfoStudent(elem));
	}

	return infoAlunos;
    }

}