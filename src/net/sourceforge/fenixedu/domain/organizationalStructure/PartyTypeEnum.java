/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum PartyTypeEnum {
      
    ADMINISTRATIVE_OFFICE_UNIT,
    SCIENTIFIC_AREA,    
    COMPETENCE_COURSE_GROUP,       
    AGGREGATE_UNIT,        
    PLANET,
    COUNTRY,
    UNIVERSITY,    
    SCHOOL,    
    DEPARTMENT,
    DEGREE_UNIT,    
    SECTION,
    RESEARCH_UNIT;                  
                   
    public String getName() {
        return name();
    }      
}
