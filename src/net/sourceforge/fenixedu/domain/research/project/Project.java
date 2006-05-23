package net.sourceforge.fenixedu.domain.research.project;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Period;

public class Project extends Project_Base {
    
    public  Project() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete(){
        for (;this.hasAnyProjectParticipations(); getProjectParticipations().get(0).delete());
        for (;this.hasAnyAssociatedEvents(); getAssociatedEvents().get(0).delete());
        removeRootDomainObject();
        deleteDomainObject();
    }
    
    /**
     * This method is responsible for checking if the object still has active connections
     *if not, the object is deleted.
     */
    public void sweep(){
        if (!(this.hasAnyAssociatedEvents() || this.hasAnyProjectParticipations())){
            delete();
        }
    }
    
    public Period getPeriod () {
        return new Period(getStartDate(), getEndDate());
    }
    
    public int getDurationInMonths () {
        Period period = getPeriod();
        return period.getYears() * 12 + period.getMonths();
    }
    
    public static List<Project> getProjectsByTitle (String searchedTitle){
        final String searcherTitleLower = searchedTitle.toLowerCase();
        List<Project> result = new ArrayList<Project>();
        for(Project project : RootDomainObject.getInstance().getProjects()) {
            //First try to match with the default aplication language title
            //perhaps this sould be changed to try to match with the title in the
            //language the user is currently viewing
            if (project.getTitle().getContent().toLowerCase().contains(searcherTitleLower)) {
                result.add(project);
                continue;
            }
            //If there is no match try to match the searchedTitles with the existing
            //titles in other languages
            for(String title : project.getTitle().getAllContents()) {
                if(title.toLowerCase().contains(searcherTitleLower)){
                    result.add(project);
                    break;
                }
            }
        }
        return result;
    }
    
}
