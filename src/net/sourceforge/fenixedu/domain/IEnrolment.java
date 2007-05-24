package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.MultiLanguageString;

public interface IEnrolment {

    Integer getIdInternal();
    
    MultiLanguageString getName();
    
    String getGrade();
    
    Integer getFinalGrade();
    
    String getDescription();
    
    Double getEctsCredits();
    
    Double getWeigth();
    
}
