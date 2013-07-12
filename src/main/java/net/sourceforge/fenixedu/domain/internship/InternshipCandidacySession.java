package net.sourceforge.fenixedu.domain.internship;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;

public class InternshipCandidacySession extends InternshipCandidacySession_Base {
    public InternshipCandidacySession(Interval interval) {
        super();
        setCandidacyInterval(interval);
        setRootDomainObject(RootDomainObject.getInstance());
    }

    /**
     * This will deep erase the candidacy session, all candidacies created in
     * this session will be wiped out.
     */
    @Atomic
    public void delete() {
        for (; hasAnyInternshipCandidacy(); getInternshipCandidacy().iterator().next().delete()) {
            ;
        }
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public String getPresentationName() {
        return getCandidacyInterval().getStart().toLocalDate() + "/" + getCandidacyInterval().getEnd().toLocalDate();
    }

    public static InternshipCandidacySession getMostRecentCandidacySession() {
        InternshipCandidacySession current = null;
        for (InternshipCandidacySession session : RootDomainObject.getInstance().getInternshipCandidacySessionSet()) {
            if (current == null || session.getCandidacyInterval().isAfter(current.getCandidacyInterval())) {
                current = session;
            }
        }
        return current;
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacy> getInternshipCandidacy() {
        return getInternshipCandidacySet();
    }

    @Deprecated
    public boolean hasAnyInternshipCandidacy() {
        return !getInternshipCandidacySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit> getUniversity() {
        return getUniversitySet();
    }

    @Deprecated
    public boolean hasAnyUniversity() {
        return !getUniversitySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Country> getDestination() {
        return getDestinationSet();
    }

    @Deprecated
    public boolean hasAnyDestination() {
        return !getDestinationSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCandidacyInterval() {
        return getCandidacyInterval() != null;
    }

}
