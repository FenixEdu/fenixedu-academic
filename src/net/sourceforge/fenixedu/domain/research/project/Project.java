package net.sourceforge.fenixedu.domain.research.project;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.Period;

public class Project extends Project_Base {
    
    public  Project() {
        super();
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
     * substitutes it by Portuguese(?). If the project hasn't got any attribute in
     * the wanted language a domainException is thrown.
     * @param language the language we want the translation
     * @return the projecttranslation in the given language
     */
    public ProjectTranslation getTranslation(Language language){
        boolean hasTitle = true;
        boolean hasAbstract = true;
        
        ProjectTranslation translation = new ProjectTranslation(language);
        if (this.getTitle().hasLanguage(language)) {
            translation.title = this.getTitle().getContent(language);
        }
        else { 
            hasTitle = false;
            translation.title = this.getTitle().getContent(Language.PT);
        }
        
        if (this.getProjectAbstract().hasLanguage(language)) {
            translation.projectAbstract = this.getProjectAbstract().getContent(language);
        }
        else {
            hasAbstract = false;
            translation.projectAbstract = this.getProjectAbstract().getContent(Language.PT);
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
        
        ProjectTranslation (Language language){
            this.language = language;
            title = new String();
            projectAbstract = new String();
        }
    }
    
}
