/*
 * TurmaTurnoOJB.java
 * 
 * Created on 19 de Outubro de 2002, 15:23
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * @author Pedro Santos & Rita Carvalho
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurmaTurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class TurmaTurnoVO extends VersionedObjectsBase implements ITurmaTurnoPersistente {

    public ISchoolClassShift readByTurmaAndTurno(Integer turmaOID, Integer turnoOID)
            throws ExcepcaoPersistencia {

        ISchoolClass schoolClass = (ISchoolClass) readByOID(SchoolClass.class, turmaOID);
        IShift shift = (IShift) readByOID(Shift.class, turnoOID);

        List<ISchoolClassShift> schoolClassShifts = schoolClass.getSchoolClassShifts();

        for (ISchoolClassShift schoolClassShift : schoolClassShifts) {
            if (schoolClassShift.getTurma().getNome().equals(schoolClass.getNome())
                    && schoolClassShift.getTurma().getExecutionPeriod().getName().equals(
                            schoolClass.getExecutionPeriod().getName())
                    && schoolClassShift.getTurma().getExecutionPeriod().getExecutionYear().getYear()
                            .equals(schoolClass.getExecutionPeriod().getExecutionYear().getYear())
                    && schoolClassShift.getTurma().getExecutionDegree().getExecutionYear().getYear()
                            .equals(schoolClass.getExecutionDegree().getExecutionYear().getYear())
                    && schoolClassShift
                            .getTurma()
                            .getExecutionDegree()
                            .getDegreeCurricularPlan()
                            .getName()
                            .equals(schoolClass.getExecutionDegree().getDegreeCurricularPlan().getName())
                    && schoolClassShift.getTurma().getExecutionDegree().getDegreeCurricularPlan()
                            .getDegree().getSigla().equals(
                                    schoolClass.getExecutionDegree().getDegreeCurricularPlan()
                                            .getDegree().getSigla())
                    && schoolClassShift.getTurno().getNome().equals(shift.getNome())
                    && schoolClassShift.getTurno().getDisciplinaExecucao().getSigla().equals(
                            shift.getDisciplinaExecucao().getSigla())
                    && schoolClassShift.getTurno().getDisciplinaExecucao().getExecutionPeriod()
                            .getName().equals(
                                    shift.getDisciplinaExecucao().getExecutionPeriod().getName())
                    && schoolClassShift.getTurno().getDisciplinaExecucao().getExecutionPeriod()
                            .getExecutionYear().getYear().equals(
                                    shift.getDisciplinaExecucao().getExecutionPeriod()
                                            .getExecutionYear().getYear())) {
                return schoolClassShift;
            }

        }
        return null;
    }

    public List readByClass(Integer schoolClassOID) throws ExcepcaoPersistencia {

        ISchoolClass schoolClass = (ISchoolClass) readByOID(SchoolClass.class, schoolClassOID);
        List<ISchoolClassShift> schoolClassShifts = schoolClass.getSchoolClassShifts();
        List result = new ArrayList();

        for (ISchoolClassShift schoolClassShift : schoolClassShifts) {
            if (schoolClassShift.getTurma().getNome().equals(schoolClass.getNome())
                    && schoolClassShift.getTurma().getExecutionPeriod().getName().equals(
                            schoolClass.getExecutionPeriod().getName())
                    && schoolClassShift.getTurma().getExecutionPeriod().getExecutionYear().getYear()
                            .equals(schoolClass.getExecutionPeriod().getExecutionYear().getYear())
                    && schoolClassShift.getTurma().getExecutionDegree().getExecutionYear().getYear()
                            .equals(schoolClass.getExecutionDegree().getExecutionYear().getYear())
                    && schoolClassShift
                            .getTurma()
                            .getExecutionDegree()
                            .getDegreeCurricularPlan()
                            .getName()
                            .equals(schoolClass.getExecutionDegree().getDegreeCurricularPlan().getName())
                    && schoolClassShift.getTurma().getExecutionDegree().getDegreeCurricularPlan()
                            .getDegree().getSigla().equals(
                                    schoolClass.getExecutionDegree().getDegreeCurricularPlan()
                                            .getDegree().getSigla())) {

                result.add(schoolClassShift);
            }

        }

        List shiftList = new ArrayList();
        Iterator resultIterator = result.iterator();
        while (resultIterator.hasNext()) {
            ISchoolClassShift classShift = (ISchoolClassShift) resultIterator.next();
            shiftList.add(classShift.getTurno());
        }
        return shiftList;

    }

    public List readClassesWithShift(Integer turnoOID) throws ExcepcaoPersistencia {

        IShift shift = (IShift) readByOID(Shift.class, turnoOID);
        List<ISchoolClassShift> schoolClassShifts = shift.getSchoolClassShifts();

        List<ISchoolClassShift> result = new ArrayList();

        for (ISchoolClassShift schoolClassShift : schoolClassShifts) {
            if (schoolClassShift.getTurno().equals(shift.getNome())
                    && schoolClassShift.getTurno().getDisciplinaExecucao().getSigla().equals(
                            shift.getDisciplinaExecucao().getSigla())
                    && schoolClassShift.getTurno().getDisciplinaExecucao().getExecutionPeriod()
                            .getName().equals(
                                    shift.getDisciplinaExecucao().getExecutionPeriod().getName())
                    && schoolClassShift.getTurno().getDisciplinaExecucao().getExecutionPeriod()
                            .getExecutionYear().getYear().equals(
                                    shift.getDisciplinaExecucao().getExecutionPeriod()
                                            .getExecutionYear().getYear())) {

            }
            result.add(schoolClassShift);
        }
        return result;
    }
}