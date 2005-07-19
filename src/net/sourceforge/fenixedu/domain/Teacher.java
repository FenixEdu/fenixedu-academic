package net.sourceforge.fenixedu.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;


public class Teacher extends Teacher_Base {
    private Map creditsMap = new HashMap();
    
    
    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/
    
    public void addToTeacherSheet(IPublication publication, PublicationArea publicationArea) throws DomainException {

        checkForMaximumPublicationsInTeacherSheet(publicationArea);

        new PublicationTeacher(publication, this, publicationArea);
        
    }
    
    public void removeFromTeacherSheet(IPublication publication) throws DomainException {
        Iterator<IPublicationTeacher> iterator = getTeacherPublications().iterator();

        while(iterator.hasNext())
        {
            IPublicationTeacher publicationTeacher = iterator.next();
            if(publicationTeacher.getPublication().equals(publication))
            {
                iterator.remove();
                publicationTeacher.delete();
                return;
            }
        }
        //In case the publication isn't associated with the teacher
        throw new DomainException(publication.getTitle());
    }

    /********************************************************************
     *                          OTHER METHODS                           *
     ********************************************************************/    
    
    public String toString() {
        String result = "[Dominio.Teacher ";
        result += ", teacherNumber=" + getTeacherNumber();
        result += ", person=" + getPerson();
        result += ", category= " + getCategory();
        result += "]";
        return result;
    }

    public InfoCredits getExecutionPeriodCredits(IExecutionPeriod executionPeriod) {
        InfoCredits credits = (InfoCredits) creditsMap.get(executionPeriod);
        if (credits == null) {
            credits = InfoCreditsBuilder.build(this, executionPeriod);
            creditsMap.put(executionPeriod, credits);
        }
        return credits;
    }

    public void notifyCreditsChange(CreditsEvent creditsEvent, ICreditsEventOriginator originator) {
        Iterator iterator = this.creditsMap.keySet().iterator();
        while (iterator.hasNext()) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) iterator.next();
            if (originator.belongsToExecutionPeriod(executionPeriod)) {
                iterator.remove();
            }
        }
    }
    
    /********************************************************************
     *                         PRIVATE METHODS                          *
     ********************************************************************/

    private void checkForMaximumPublicationsInTeacherSheet(PublicationArea publicationArea) throws DomainException {
        int publicationCount = 0;
        for (IPublicationTeacher publicationTeacher : getTeacherPublications()) {
            if (publicationTeacher.getPublicationArea().equals(publicationArea)) {
                publicationCount++;
            }
        }
        if (publicationCount >= 5) {
            throw new DomainException("error.teacherSheetFull", publicationArea.getName());
        }
    }

}
