/*
 * TurmaTurnoOJB.java
 * 
 * Created on 19 de Outubro de 2002, 15:23
 */

package ServidorPersistente.OJB;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICursoExecucao;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.TurmaTurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ITurmaTurnoPersistente;

public class TurmaTurnoOJB extends PersistentObjectOJB implements ITurmaTurnoPersistente {

    public ITurmaTurno readByTurmaAndTurno(ITurma turma, ITurno turno) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("turma.nome", turma.getNome());
        crit.addEqualTo("turma.executionPeriod.name", turma.getExecutionPeriod().getName());
        crit.addEqualTo("turma.executionPeriod.executionYear.year", turma.getExecutionPeriod()
                .getExecutionYear().getYear());
        crit.addEqualTo("turma.executionDegree.executionYear.year", turma.getExecutionDegree()
                .getExecutionYear().getYear());
        crit.addEqualTo("turma.executionDegree.curricularPlan.name", turma.getExecutionDegree()
                .getCurricularPlan().getName());
        crit.addEqualTo("turma.executionDegree.curricularPlan.degree.sigla", turma.getExecutionDegree()
                .getCurricularPlan().getDegree().getSigla());
        crit.addEqualTo("turno.nome", turno.getNome());
        crit.addEqualTo("turno.disciplinaExecucao.sigla", turno.getDisciplinaExecucao().getSigla());
        crit.addEqualTo("turno.disciplinaExecucao.executionPeriod.name", turno.getDisciplinaExecucao()
                .getExecutionPeriod().getName());
        crit.addEqualTo("turno.disciplinaExecucao.executionPeriod.executionYear.year", turno
                .getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        return (ITurmaTurno) queryObject(TurmaTurno.class, crit);

    }

    public void delete(ITurmaTurno turmaTurno) throws ExcepcaoPersistencia {
        super.delete(turmaTurno);
    }

    /**
     * Returns a shift list
     * 
     * @see ServidorPersistente.ITurmaTurnoPersistente#readByClass(ITurma)
     */
    public List readByClass(ITurma group) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("turma.nome", group.getNome());
        crit.addEqualTo("turma.executionPeriod.name", group.getExecutionPeriod().getName());
        crit.addEqualTo("turma.executionPeriod.executionYear.year", group.getExecutionPeriod()
                .getExecutionYear().getYear());
        ICursoExecucao executionDegree = group.getExecutionDegree();
        crit.addEqualTo("turma.executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("turma.executionDegree.curricularPlan.name", executionDegree.getCurricularPlan()
                .getName());
        crit.addEqualTo("turma.executionDegree.curricularPlan.degree.sigla", executionDegree
                .getCurricularPlan().getDegree().getSigla());

        List result = queryList(TurmaTurno.class, crit);

        List shiftList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            ITurmaTurno classShift = (ITurmaTurno) resultIterator.next();
            shiftList.add(classShift.getTurno());
        }
        return shiftList;

    }

    /**
     * Returns a class list
     * 
     * @see ServidorPersistente.ITurmaTurnoPersistente#readByClass(ITurma)
     */
    public List readByShift(ITurno group) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("turno.idInternal", group.getIdInternal());

        List result = queryList(TurmaTurno.class, crit);

        List classList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            ITurmaTurno classShift = (ITurmaTurno) resultIterator.next();
            classList.add(classShift.getTurma());
        }
        return classList;

    }

    public List readClassesWithShift(ITurno turno) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("turno.nome", turno.getNome());
        crit.addEqualTo("turno.disciplinaExecucao.sigla", turno.getDisciplinaExecucao().getSigla());
        crit.addEqualTo("turno.disciplinaExecucao.executionPeriod.name", turno.getDisciplinaExecucao()
                .getExecutionPeriod().getName());
        crit.addEqualTo("turno.disciplinaExecucao.executionPeriod.executionYear.year", turno
                .getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        return queryList(TurmaTurno.class, crit);

    }

    public List readByShiftAndExecutionDegree(ITurno turno, ICursoExecucao execucao)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("turno.nome", turno.getNome());
        criteria.addEqualTo("turma.executionDegree.curricularPlan.degree.sigla", execucao
                .getCurricularPlan().getDegree().getSigla());
        return queryList(TurmaTurno.class, criteria);
    }

}