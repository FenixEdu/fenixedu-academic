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

    MASTER_DEGREE,

    INTEGRATED_MASTER_DEGREE,

    SPECIALIZATION;

    public String getName() {
        return name();
    }

}
