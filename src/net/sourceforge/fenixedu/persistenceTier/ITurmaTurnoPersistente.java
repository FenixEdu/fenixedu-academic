/*
 * ITurmaTurnoPersistente.java
 *
 * Created on 19 de Outubro de 2002, 15:21
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;

public interface ITurmaTurnoPersistente extends IPersistentObject {
    public ISchoolClassShift readByTurmaAndTurno(ISchoolClass turma, IShift turno) throws ExcepcaoPersistencia;

    public void delete(ISchoolClassShift turmaTurno) throws ExcepcaoPersistencia;

    public List readByClass(ISchoolClass group) throws ExcepcaoPersistencia;

    public List readClassesWithShift(IShift turno) throws ExcepcaoPersistencia;

    public List readByShift(IShift group) throws ExcepcaoPersistencia;

    public List readByShiftAndExecutionDegree(IShift turno, IExecutionDegree execucao)
            throws ExcepcaoPersistencia;
}