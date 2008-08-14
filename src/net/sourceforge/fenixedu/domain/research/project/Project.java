package net.sourceforge.fenixedu.domain.research.project;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import org.joda.time.Period;

/**
 * This class represents the research project. The research Project is inserted
 * in the investigation and intelectual production module. It is intended to
 * represent all the projects allready existent in the institution in other
 * applications other than fenix and the new projects created in the fenix
 * interface.
 */
public class Project extends Project_Base {

    /**
     * The constructor for this class. It is only used when a project is being
     * created in the interface. If your intention is to import projects from
     * other applications you must not use this Constructor
     */
    public Project() {
	super();
	// Only internal projects are created
	this.setProjectType(ProjectType.INTERNAL_PROJECT);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Project(MultiLanguageString title, ProjectType type) {
	super();
	this.setProjectType(type);
	this.setTitle(title);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    /**
     * This method removes any existing connection from any other entity to the
     * project and in the end it deletes the object.
     */
    public void delete() {
	for (; this.hasAnyProjectParticipations(); getProjectParticipations().get(0).delete())
	    ;
	for (; this.hasAnyAssociatedEvents(); getAssociatedEvents().get(0).delete())
	    ;
	removeRootDomainObject();
	deleteDomainObject();
    }

    /**
     * This method is responsible for checking if the object still has active
     * connections if not, the delete method is called.
     */
    public void sweep() {
	if (!(this.hasAnyAssociatedEvents() || this.hasAnyProjectParticipations())) {
	    delete();
	}
    }

    /**
     * Method responsible for returning the life period of this project
     * 
     * @return the life period of the project.
     */
    public Period getPeriod() {
	return new Period(getStartDate(), getEndDate());
    }

    /**
     * @return an integer representing the duration in months of the project.
     */
    public int getDurationInMonths() {
	Period period = getPeriod();
	return period.getYears() * 12 + period.getMonths();
    }

    /**
     * Method responsible for searching all the projects trying to match it with
     * a given string
     * 
     * @param searchedTitle
     *            the title of the projects to search
     * @return a list containing all the projects that in its title contain the
     *         string 'searchedTitle'
     */
    public static List<Project> getProjectsByTitle(String searchedTitle) {
	final String searcherTitleLower = searchedTitle.toLowerCase();
	List<Project> result = new ArrayList<Project>();
	for (Project project : RootDomainObject.getInstance().getProjects()) {
	    // First try to match with the default aplication language title
	    // perhaps this sould be changed to try to match with the title in
	    // the
	    // language the user is currently viewing
	    if (project.getTitle().getContent().toLowerCase().contains(searcherTitleLower)) {
		result.add(project);
		continue;
	    }
	    // If there is no match try to match the searchedTitles with the
	    // existing
	    // titles in other languages
	    for (String title : project.getTitle().getAllContents()) {
		if (title.toLowerCase().contains(searcherTitleLower)) {
		    result.add(project);
		    break;
		}
	    }
	}
	return result;
    }

}
