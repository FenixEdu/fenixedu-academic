/*
 * TurmaTurnoOJB.java
 * 
 * Created on 19 de Outubro de 2002, 15:23
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClassShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurmaTurnoPersistente;

import org.apache.ojb.broker.query.Criteria;

public class TurmaTurnoOJB extends PersistentObjectOJB implements ITurmaTurnoPersistente {

    public ISchoolClassShift readByTurmaAndTurno(ISchoolClass turma, IShift turno) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("turma.nome", turma.getNome());
        crit.addEqualTo("turma.executionPeriod.name", turma.getExecutionPeriod().getName());
        crit.addEqualTo("turma.executionPeriod.executionYear.year", turma.getExecutionPeriod()
                .getExecutionYear().getYear());
        crit.addEqualTo("turma.executionDegree.executionYear.year", turma.getExecutionDegree()
                .getExecutionYear().getYear());
        crit.addEqualTo("turma.executionDegree.curricularPlan.name", turma.getExecutionDegree()
                .getDegreeCurricularPlan().getName());
        crit.addEqualTo("turma.executionDegree.curricularPlan.degree.sigla", turma.getExecutionDegree()
                .getDegreeCurricularPlan().getDegree().getSigla());
        crit.addEqualTo("turno.nome", turno.getNome());
        crit.addEqualTo("turno.disciplinaExecucao.sigla", turno.getDisciplinaExecucao().getSigla());
        crit.addEqualTo("turno.disciplinaExecucao.executionPeriod.name", turno.getDisciplinaExecucao()
                .getExecutionPeriod().getName());
        crit.addEqualTo("turno.disciplinaExecucao.executionPeriod.executionYear.year", turno
                .getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        return (ISchoolClassShift) queryObject(SchoolClassShift.class, crit);

    }

    public void delete(ISchoolClassShift turmaTurno) throws ExcepcaoPersistencia {
        super.delete(turmaTurno);
    }

    /**
     * Returns a shift list
     * 
     * @see ServidorPersistente.ITurmaTurnoPersistente#readByClass(ISchoolClass)
     */
    public List readByClass(ISchoolClass group) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("turma.nome", group.getNome());
        crit.addEqualTo("turma.executionPeriod.name", group.getExecutionPeriod().getName());
        crit.addEqualTo("turma.executionPeriod.executionYear.year", group.getExecutionPeriod()
                .getExecutionYear().getYear());
        IExecutionDegree executionDegree = group.getExecutionDegree();
        crit.addEqualTo("turma.executionDegree.executionYear.year", executionDegree.getExecutionYear()
                .getYear());
        crit.addEqualTo("turma.executionDegree.curricularPlan.name", executionDegree.getDegreeCurricularPlan()
                .getName());
        crit.addEqualTo("turma.executionDegree.curricularPlan.degree.sigla", executionDegree
                .getDegreeCurricularPlan().getDegree().getSigla());

        List result = queryList(SchoolClassShift.class, crit);

        List shiftList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            ISchoolClassShift classShift = (ISchoolClassShift) resultIterator.next();
            shiftList.add(classShift.getTurno());
        }
        return shiftList;

    }

    /**
     * Returns a class list
     * 
     * @see ServidorPersistente.ITurmaTurnoPersistente#readByClass(ISchoolClass)
     */
    public List readByShift(IShift group) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("turno.idInternal", group.getIdInternal());

        List result = queryList(SchoolClassShift.class, crit);

        List classList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            ISchoolClassShift classShift = (ISchoolClassShift) resultIterator.next();
            classList.add(classShift.getTurma());
        }
        return classList;

    }

    public List readClassesWithShift(IShift turno) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("turno.nome", turno.getNome());
        crit.addEqualTo("turno.disciplinaExecucao.sigla", turno.getDisciplinaExecucao().getSigla());
        crit.addEqualTo("turno.disciplinaExecucao.executionPeriod.name", turno.getDisciplinaExecucao()
                .getExecutionPeriod().getName());
        crit.addEqualTo("turno.disciplinaExecucao.executionPeriod.executionYear.year", turno
                .getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        return queryList(SchoolClassShift.class, crit);

    }

    public List readByShiftAndExecutionDegree(IShift turno, IExecutionDegree execucao)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("turno.nome", turno.getNome());
        criteria.addEqualTo("turma.executionDegree.curricularPlan.degree.sigla", execucao
                .getDegreeCurricularPlan().getDegree().getSigla());
        return queryList(SchoolClassShift.class, criteria);
    }

}