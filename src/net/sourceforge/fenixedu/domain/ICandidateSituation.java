/*
 * ICandidateSituation.java
 *
 * Created on 1 de Novembro de 2002, 15:51
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public interface ICandidateSituation extends IDomainObject {

    // Set Methods
    void setDate(Date date);

    void setRemarks(String remarks);

    void setValidation(State validation);

    void setMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidate);

    void setSituation(SituationName situation);

    // Get Methods
    Date getDate();

    String getRemarks();

    State getValidation();

    IMasterDegreeCandidate getMasterDegreeCandidate();

    SituationName getSituation();

}

