/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;


/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class SeminaryCandidacy extends SeminaryCandidacy_Base {

	public SeminaryCandidacy() {
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

    public static List<SeminaryCandidacy> getByStudentAndSeminary(Registration student, Seminary seminary) {
        List<SeminaryCandidacy> candidacies = new ArrayList<SeminaryCandidacy>();
        
        for (SeminaryCandidacy candidacy : RootDomainObject.getInstance().getCandidacys()) {
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

    public static List<SeminaryCandidacy> getAllCandidacies() {
        return RootDomainObject.getInstance().getCandidacys();
    }

}
