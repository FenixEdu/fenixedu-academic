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
 *         Created at Jul 23, 2003, 9:49:19 AM
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

        setCurricularCourse(null);
        setModality(null);
        setSeminary(null);
        setStudent(null);
        setTheme(null);

        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static List<SeminaryCandidacy> getByStudentAndSeminary(Registration registration, Seminary seminary) {
        List<SeminaryCandidacy> candidacies = new ArrayList<SeminaryCandidacy>();

        for (SeminaryCandidacy candidacy : RootDomainObject.getInstance().getCandidacys()) {
            if (!candidacy.getStudent().equals(registration)) {
                continue;
            }

            if (!candidacy.getSeminary().equals(registration)) {
                continue;
            }

            candidacies.add(candidacy);
        }

        return candidacies;
    }

    public static List<SeminaryCandidacy> getAllCandidacies() {
        return RootDomainObject.getInstance().getCandidacys();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.CaseStudyChoice> getCaseStudyChoices() {
        return getCaseStudyChoicesSet();
    }

    @Deprecated
    public boolean hasAnyCaseStudyChoices() {
        return !getCaseStudyChoicesSet().isEmpty();
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasTheme() {
        return getTheme() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMotivation() {
        return getMotivation() != null;
    }

    @Deprecated
    public boolean hasApproved() {
        return getApproved() != null;
    }

    @Deprecated
    public boolean hasModality() {
        return getModality() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

    @Deprecated
    public boolean hasSeminary() {
        return getSeminary() != null;
    }

}
