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
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerAlunosDeTurno extends Service {

    public Object run(ShiftKey keyTurno) throws ExcepcaoPersistencia {
    	final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(keyTurno.getInfoExecutionCourse().getIdInternal());
    	final Shift shift = executionCourse.findShiftByName(keyTurno.getShiftName());

        List<Student> alunos = shift.getStudents();

        List infoAlunos = new ArrayList(alunos.size());
        for (final Iterator iterator = alunos.iterator(); iterator.hasNext();) {
            Student elem = (Student) iterator.next();
            InfoPerson infoPessoa = new InfoPerson();
            infoPessoa.setNome(elem.getPerson().getNome());
            infoPessoa.setUsername(elem.getPerson().getUsername());
            infoPessoa.setPassword(elem.getPerson().getPassword());
            infoPessoa.setEmail(elem.getPerson().getEmail());
            infoAlunos.add(new InfoStudent(elem.getNumber(), elem.getState(), infoPessoa, elem
                    .getDegreeType()));
        }

        return infoAlunos;
    }

}
