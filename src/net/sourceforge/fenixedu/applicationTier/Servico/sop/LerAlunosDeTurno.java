/*
 * LerAlunosDeTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:41
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço LerAlunosDeTurno.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class LerAlunosDeTurno implements IServico {

    private static LerAlunosDeTurno _servico = new LerAlunosDeTurno();

    /**
     * The singleton access method of this class.
     */
    public static LerAlunosDeTurno getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private LerAlunosDeTurno() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "LerAlunosDeTurno";
    }

    public Object run(ShiftKey keyTurno) {

        List alunos = null;
        List infoAlunos = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IShift shift = new Shift();

            IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(keyTurno
                    .getInfoExecutionCourse());

            //      shift.setDisciplinaExecucao(executionCourse);
            //      shift.setNome(keyTurno.getShiftName());

            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            shift = persistentShift.readByNameAndExecutionCourse(keyTurno.getShiftName(),
                    executionCourse);

            alunos = sp.getITurnoAlunoPersistente().readByShift(shift);

            Iterator iterator = alunos.iterator();
            infoAlunos = new ArrayList();
            while (iterator.hasNext()) {
                IStudent elem = (IStudent) iterator.next();
                InfoPerson infoPessoa = new InfoPerson();
                infoPessoa.setNome(elem.getPerson().getNome());
                infoPessoa.setUsername(elem.getPerson().getUsername());
                infoPessoa.setPassword(elem.getPerson().getPassword());
                infoPessoa.setEmail(elem.getPerson().getEmail());
                infoAlunos.add(new InfoStudent(elem.getNumber(), elem.getState(), infoPessoa, elem
                        .getDegreeType()));
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoAlunos;
    }

}