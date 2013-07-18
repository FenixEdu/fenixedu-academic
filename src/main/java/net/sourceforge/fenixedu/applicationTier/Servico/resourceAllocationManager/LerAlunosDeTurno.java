/*
 * LerAlunosDeTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:41
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class LerAlunosDeTurno {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static List<InfoStudent> run(ShiftKey keyTurno) {

        final ExecutionCourse executionCourse =
                RootDomainObject.getInstance().readExecutionCourseByOID(keyTurno.getInfoExecutionCourse().getIdInternal());
        final Shift shift = executionCourse.findShiftByName(keyTurno.getShiftName());

        List<Registration> alunos = shift.getStudents();

        List<InfoStudent> infoAlunos = new ArrayList<InfoStudent>(alunos.size());
        for (Registration elem : alunos) {
            infoAlunos.add(new InfoStudent(elem));
        }

        return infoAlunos;
    }

}