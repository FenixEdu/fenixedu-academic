/*
 * ICurriculumPersistente.java
 * 
 * Created on 6 de Janeiro de 2003, 21:21
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Curriculum;

public interface IPersistentCurriculum extends IPersistentObject {

    public Curriculum readCurriculumByCurricularCourse(Integer curricularCourseOID)
            throws ExcepcaoPersistencia;

    public Curriculum readCurriculumByCurricularCourseAndExecutionYear(
            Integer curricularCourseOID, Date executionYearEndDate)
            throws ExcepcaoPersistencia;
}