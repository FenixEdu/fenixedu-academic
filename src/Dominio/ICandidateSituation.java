/*
 * ICandidateSituation.java
 *
 * Created on 1 de Novembro de 2002, 15:51
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
package Dominio;

import java.util.Date;

import Util.State;
import Util.SituationName;

public interface ICandidateSituation {
    
    // Set Methods
    void setDate(Date date);
    void setRemarks(String remarks);
    void setValidation(State validation);
    void setMasterDegreeCandidate (IMasterDegreeCandidate masterDegreeCandidate);
    void setSituation(SituationName situation);
    
    
    // Get Methods
    Date getDate();
    String getRemarks();
    State getValidation();
    IMasterDegreeCandidate getMasterDegreeCandidate();    
    SituationName getSituation();

}



