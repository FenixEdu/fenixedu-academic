package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.MultiLanguageString;

public interface IEnrolment {

    Integer getIdInternal();
    
    MultiLanguageString getName();
    
    String getGrade();
    
    String getDescription();
    
}
