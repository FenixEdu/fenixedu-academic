/*
 * ICurriculumPersistente.java
 * 
 * Created on 6 de Janeiro de 2003, 21:21
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IExecutionYear;

public interface IPersistentCurriculum extends IPersistentObject {

    public ICurriculum readCurriculumByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;

    public List readCurriculumHistoryByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;

    public ICurriculum readCurriculumByCurricularCourseAndExecutionYear(
            ICurricularCourse curricularCourse, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    public void delete(ICurriculum curriculum) throws ExcepcaoPersistencia;

}