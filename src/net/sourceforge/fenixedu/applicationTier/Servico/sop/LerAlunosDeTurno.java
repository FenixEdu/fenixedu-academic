/*
 * LerAlunosDeTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:41
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerAlunosDeTurno.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerAlunosDeTurno extends Service {

    public Object run(ShiftKey keyTurno) throws ExcepcaoPersistencia {
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(keyTurno.getInfoExecutionCourse().getIdInternal());
    	final Shift shift = executionCourse.findShiftByName(keyTurno.getShiftName());

        List<Registration> alunos = shift.getStudents();

        List infoAlunos = new ArrayList(alunos.size());
        for (final Iterator iterator = alunos.iterator(); iterator.hasNext();) {
            Registration elem = (Registration) iterator.next();
            infoAlunos.add(new InfoStudent(elem));
        }

        return infoAlunos;
    }

}
