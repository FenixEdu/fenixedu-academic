/*
 * Created on 15/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.teacher;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IWeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public interface IPersistentWeeklyOcupation extends IPersistentObject {
    public IWeeklyOcupation readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;
}