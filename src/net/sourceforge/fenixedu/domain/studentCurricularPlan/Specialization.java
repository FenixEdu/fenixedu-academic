/*
 * Specialization.java
 * 
 * Created on 18 de Novembro de 2002, 17:32
 */

/**
 * 
 * Authors : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.domain.studentCurricularPlan;

public enum Specialization {

    STUDENT_CURRICULAR_PLAN_MASTER_DEGREE,

    STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE,

    STUDENT_CURRICULAR_PLAN_SPECIALIZATION;

    public String getName() {
        return name();
    }

}
