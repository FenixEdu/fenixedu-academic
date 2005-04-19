/*
 * Created on 21/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.util.PublicationType;


/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class PublicationsNumber extends PublicationsNumber_Base implements IPublicationsNumber {
    
    private PublicationType publicationType;

    public PublicationType getPublicationType() {        
        return publicationType;
    }

    public void setPublicationType(PublicationType publicationType) {
        this.publicationType = publicationType;        
    }

}