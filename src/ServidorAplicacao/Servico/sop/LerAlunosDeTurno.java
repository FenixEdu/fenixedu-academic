/*
 * LerAlunosDeTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:41
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço LerAlunosDeTurno.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoPerson;
import DataBeans.InfoStudent;
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurno shift = new Turno();

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