/*
 * ITurnoOJB.java
 * 
 * Created on 17 de Outubro de 2002, 19:35
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.SchoolClassShift;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.ojb.broker.query.Criteria;

public class TurnoOJB extends ObjectFenixOJB implements ITurnoPersistente {

    public IShift readByNameAndExecutionCourse(String shiftName, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        crit.addEqualTo("nome", shiftName);
        return (IShift) queryObject(Shift.class, crit);
    }

    public void delete(IShift turno) throws ExcepcaoPersistencia {

        //        Criteria crit = new Criteria();
        //        crit.addEqualTo("chaveTurno", turno.getIdInternal());
        //        ITurnoAula turnoAula = null;
        //        TurnoAulaOJB turnoAulaOJB = new TurnoAulaOJB();
        //
        //        List result = queryList(TurnoAula.class, crit);
        //        lockRead(result);
        //        Iterator iterador = result.iterator();
        //        while (iterador.hasNext())
        //        {
        //            turnoAula = (ITurnoAula) iterador.next();
        //            turnoAulaOJB.delete(turnoAula);
        //        }

        //        Criteria crit = new Criteria();
        //        crit.addEqualTo("chaveTurno", turno.getIdInternal());
        //        ISchoolClassShift turmaTurno = null;
        //        TurmaTurnoOJB turmaTurnoOJB = new TurmaTurnoOJB();
        //        List result1 = queryList(SchoolClassShift.class, crit);
        //        Iterator iterador1 = result1.iterator();
        //        while (iterador1.hasNext())
        //        {
        //            turmaTurno = (ISchoolClassShift) iterador1.next();
        //            turmaTurnoOJB.delete(turmaTurno);
        //        }
        //        IShiftStudent turnoAluno = null;
        //        TurnoAlunoOJB turnoAlunoOJB = new TurnoAlunoOJB();
        //
        //        Criteria criteria = new Criteria();
        //        criteria.addEqualTo("shift.nome", turno.getNome());
        //        criteria.addEqualTo("shift.disciplinaExecucao.sigla",
        // turno.getDisciplinaExecucao().getSigla());
        //        criteria.addEqualTo(
        //            "shift.disciplinaExecucao.executionPeriod.name",
        //            turno.getDisciplinaExecucao().getExecutionPeriod().getName());
        //        criteria.addEqualTo(
        //            "shift.disciplinaExecucao.executionPeriod.executionYear.year",
        //            turno.getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        //        List result2 = queryList(ShiftStudent.class, criteria);
        //
        //        Iterator iterador2 = result2.iterator();
        //        while (iterador2.hasNext())
        //        {
        //            turnoAluno = (IShiftStudent) iterador2.next();
        //            turnoAlunoOJB.delete(turnoAluno);
        //        }

        super.delete(turno);

    }

    public Integer countAllShiftsOfAllClassesAssociatedWithShift(IShift shift)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("turno.nome", shift.getNome());
        criteria.addEqualTo("turno.disciplinaExecucao.sigla", shift.getDisciplinaExecucao().getSigla());
        criteria.addEqualTo("turno.disciplinaExecucao.executionPeriod.name", shift
                .getDisciplinaExecucao().getExecutionPeriod().getName());
        criteria.addEqualTo("turno.disciplinaExecucao.executionPeriod.executionYear.year", shift
                .getDisciplinaExecucao().getExecutionPeriod().getExecutionYear().getYear());
        criteria.addEqualTo("turno.tipo", shift.getTipo().getTipo());

        List result = queryList(SchoolClassShift.class, criteria);

        //return new Integer(result.size());
        List result2 = null;
        for (int i = 0; i != result.size(); i++) {
            criteria = new Criteria();
            criteria.addEqualTo("turno.tipo", new Integer(TipoAula.PRATICA));
            criteria.addEqualTo("turma.nome", ((SchoolClassShift) (result.get(i))).getSchoolClass().getName());
            if (i == 0) {
                result2 = queryList(SchoolClassShift.class, criteria);
            } else {
                List result_tmp = queryList(SchoolClassShift.class, criteria);
                for (int j = 0; j != result_tmp.size(); j++)
                    if (!result2.contains(result_tmp.get(j)))
                        result2.add(result_tmp.get(j));
            }
        }
        return new Integer(result2.size());
    }

    public List readByDisciplinaExecucao(String sigla, String anoLectivo, String siglaLicenciatura)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.sigla", sigla);
        crit.addEqualTo("disciplinaExecucao.executionPeriod.executionYear.year", anoLectivo);
        crit.addEqualTo(
                "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.degree.sigla",
                siglaLicenciatura);
        return queryList(Shift.class, crit);

    }

    public List readByExecutionCourseAndType(IExecutionCourse executionCourse, Integer type)
            throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        crit.addEqualTo("tipo", type);
        return queryList(Shift.class, crit);

    }

    /**
     * @see ServidorPersistente.ITurnoPersistente#readByExecutionCourse(Dominio.IDisciplinaExecucao)
     */
    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("disciplinaExecucao.idInternal", executionCourse.getIdInternal());
        return queryList(Shift.class, crit);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ITurnoPersistente#readByExecutionDegreeAndCurricularYear(Dominio.IExecutionDegree,
     *      Dominio.ICurricularYear)
     */
    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYear(
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree,
            ICurricularYear curricularYear) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();

        criteria
                .addEqualTo(
                        "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
                        curricularYear.getIdInternal());
        criteria.addEqualTo(
                "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.semester",
                executionPeriod.getSemester());
        criteria.addEqualTo(
                "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                executionDegree.getDegreeCurricularPlan().getIdInternal());
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", executionPeriod
                .getIdInternal());

        List shifts = queryList(Shift.class, criteria, true);
        return shifts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ITurnoPersistente#readAvailableShiftsForClass(Dominio.ISchoolClass)
     */
    public List readAvailableShiftsForClass(ISchoolClass schoolClass) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria
                .addEqualTo(
                        "disciplinaExecucao.associatedCurricularCourses.scopes.curricularSemester.curricularYear.idInternal",
                        schoolClass.getCurricularYear());
        criteria.addEqualTo(
                "disciplinaExecucao.associatedCurricularCourses.degreeCurricularPlan.idInternal",
                schoolClass.getExecutionDegree().getDegreeCurricularPlan().getIdInternal());
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", schoolClass
                .getExecutionPeriod().getIdInternal());

        List shifts = queryList(Shift.class, criteria, true);

        List classShifts = PersistenceSupportFactory.getDefaultPersistenceSupport().getITurmaTurnoPersistente().readByClass(
                schoolClass);

        shifts.removeAll(classShifts);

        return shifts;
    }

    //by gedl AT rnl DOT ist DOT utl DOT pt, September the 17th, 2003
    public List readByExecutionCourseID(Integer id) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("chaveDisciplinaExecucao", id);
        return queryList(Shift.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ITurnoPersistente#readByLesson()
     */
    public List readByLesson(ILesson lesson) throws ExcepcaoPersistencia {
        if (lesson != null) {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("associatedLessons.idInternal", lesson.getIdInternal());
            return queryList(Shift.class, criteria);
        }

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ITurnoPersistente#readByLesson()
     */
    public IShift readShiftByLesson(ILesson lesson) throws ExcepcaoPersistencia {
        if (lesson != null) {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("associatedLessons.idInternal", lesson.getIdInternal());
            return (IShift) queryObject(Shift.class, criteria);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ITurmaPersistente#readShiftsThatContainsStudentAttendsOnExecutionPeriod(Dominio.IStudent,
     *      Dominio.IExecutionPeriod)
     */
    public List readShiftsThatContainsStudentAttendsOnExecutionPeriod(IStudent student,
            IExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("disciplinaExecucao.attendingStudents.idInternal", student.getIdInternal());
        criteria.addEqualTo("disciplinaExecucao.executionPeriod.idInternal", executionPeriod
                .getIdInternal());
        return queryList(Shift.class, criteria, true);
    }

}