/*
 * LerAlunosDeTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:41
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerAlunosDeTurno {

    @Atomic
    public static List<InfoStudent> run(ShiftKey keyTurno) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(keyTurno.getInfoExecutionCourse().getExternalId());
        final Shift shift = executionCourse.findShiftByName(keyTurno.getShiftName());

        Collection<Registration> alunos = shift.getStudents();

        List<InfoStudent> infoAlunos = new ArrayList<InfoStudent>(alunos.size());
        for (Registration elem : alunos) {
            infoAlunos.add(new InfoStudent(elem));
        }

        return infoAlunos;
    }

}