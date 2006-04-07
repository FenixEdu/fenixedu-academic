/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Student;


/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class Candidacy extends Candidacy_Base {

	public Candidacy() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public void delete() {
        for (CaseStudyChoice choice : getCaseStudyChoices()) {
            choice.delete();
        }
        
        removeCurricularCourse();
        removeModality();
        removeSeminary();
        removeStudent();
        removeTheme();
        
        removeRootDomainObject();
        deleteDomainObject();
    }

    public static List<Candidacy> getByStudentAndSeminary(Student student, Seminary seminary) {
        List<Candidacy> candidacies = new ArrayList<Candidacy>();
        
        for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacys()) {
            if (! candidacy.getStudent().equals(student)) {
                continue;
            }
            
            if (! candidacy.getSeminary().equals(student)) {
                continue;
            }
            
            candidacies.add(candidacy);
        }
        
        return candidacies;
    }

    public static List<Candidacy> getAllCandidacies() {
        return RootDomainObject.getInstance().getCandidacys();
    }

}
