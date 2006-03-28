package net.sourceforge.fenixedu.domain.research.project;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.Period;

public class Project extends Project_Base {
    
    public  Project() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public Period getPeriod () {
        return new Period(getStartDate(), getEndDate());
    }
    
    public int getDurationInMonths () {
        Period period = getPeriod();
        return period.getYears() * 12 + period.getMonths();
    }
    
    /** This method is responsible for creating the logic of what is a translation
     * If the project hasn't got a given attribute in the wanted language it 
     * substitutes it by the application's delfault language. If the project hasn't
     * got any attribute in the wanted language a domainException is thrown.
     * @param language the language we want the translation
     * @return the projecttranslation in the given language
     */
    public ProjectTranslation getTranslation(Language language){
        boolean hasTitle = true;
        boolean hasAbstract = true;
        
        ProjectTranslation translation = new ProjectTranslation(language);
        if (this.getTitle().hasLanguage(language)) {
            translation.setTitle(this.getTitle().getContent(language));
        }
        else { 
            hasTitle = false;
            translation.setTitle(this.getTitle().getContent(Language.getApplicationLanguage()));
        }
        
        if (this.getProjectAbstract().hasLanguage(language)) {
            translation.setProjectAbstract(this.getProjectAbstract().getContent(language));
        }
        else {
            hasAbstract = false;
            translation.setProjectAbstract(this.getProjectAbstract().getContent(Language.getApplicationLanguage()));
        }
        
        if (!hasTitle && !hasAbstract) {
            throw new DomainException("errors.project.inexistantTranslation");
        }
        
        return translation;
    }
    
    public class ProjectTranslation {
        private Language language;
        private String title;
        private String projectAbstract;
        
        public Language getLanguage() {
			return language;
		}

		public void setLanguage(Language language) {
			this.language = language;
		}

		public String getProjectAbstract() {
			return projectAbstract;
		}

		public void setProjectAbstract(String projectAbstract) {
			this.projectAbstract = projectAbstract;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		ProjectTranslation (Language language){
            this.language = language;
            title = new String();
            projectAbstract = new String();
        }
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
