/*
 * DegreeType.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package net.sourceforge.fenixedu.domain.degree;

import net.sourceforge.fenixedu.domain.GradeScale;

/**
 * 
 * @author Nuno Nunes & Joana Mota
 */
public enum DegreeType {

    DEGREE(GradeScale.TYPE20), MASTER_DEGREE(GradeScale.TYPE5);

    private GradeScale gradeScale;

    private DegreeType(GradeScale gradeScale) {
        this.gradeScale = gradeScale;
    }

    public GradeScale getGradeScale() {
        return this.gradeScale;
    }

}