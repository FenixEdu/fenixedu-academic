package net.sourceforge.fenixedu.domain;

public class ProfessorshipPermissions extends ProfessorshipPermissions_Base {

    public ProfessorshipPermissions(final Professorship professorship) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        if (professorship == null) {
            throw new NullPointerException();
        }
        setProfessorship(professorship);

        setPersonalization(true);
        setSiteArchive(true);
        setAnnouncements(true);
        setSections(true);
        setSummaries(true);
        setStudents(true);
        setPlanning(true);
        setEvaluationSpecific(true);
        setEvaluationWorksheets(true);
        setEvaluationProject(true);
        setEvaluationTests(true);
        setEvaluationExams(true);
        setEvaluationFinal(true);
        setWorksheets(true);
        setGroups(true);
        setShift(true);

        setEvaluationMethod(true);
        setBibliografy(true);
    }

    public ProfessorshipPermissions copyPremissions(Professorship professorship) {
        ProfessorshipPermissions p = professorship.getPermissions();
        p.setPersonalization(getPersonalization());
        p.setSiteArchive(getSiteArchive());
        p.setAnnouncements(getAnnouncements());
        p.setSections(getSections());
        p.setSummaries(getSummaries());
        p.setStudents(getStudents());
        p.setPlanning(getPlanning());
        p.setEvaluationSpecific(getEvaluationSpecific());
        p.setEvaluationWorksheets(getEvaluationWorksheets());
        p.setEvaluationProject(getEvaluationProject());
        p.setEvaluationTests(getEvaluationTests());
        p.setEvaluationExams(getEvaluationExams());
        p.setWorksheets(getWorksheets());
        p.setGroups(getGroups());
        p.setShift(getShift());

        p.setEvaluationMethod(getEvaluationMethod());
        p.setBibliografy(getBibliografy());
        return p;
    }

    public void delete() {
        setRootDomainObject(null);
        setProfessorship(null);
        deleteDomainObject();
    }

    public void logEditProfessorship() {
        ExecutionCourse ec = getProfessorship().getExecutionCourse();
        ProfessorshipManagementLog.createLog(ec, "resources.MessagingResources", "log.executionCourse.professorship.edited",
                getProfessorship().getPerson().getPresentationName(), ec.getNome(), ec.getDegreePresentationString());
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSiteArchive() {
        return getSiteArchive() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasEvaluationProject() {
        return getEvaluationProject() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

    @Deprecated
    public boolean hasEvaluationFinal() {
        return getEvaluationFinal() != null;
    }

    @Deprecated
    public boolean hasStudents() {
        return getStudents() != null;
    }

    @Deprecated
    public boolean hasSections() {
        return getSections() != null;
    }

    @Deprecated
    public boolean hasEvaluationSpecific() {
        return getEvaluationSpecific() != null;
    }

    @Deprecated
    public boolean hasGroups() {
        return getGroups() != null;
    }

    @Deprecated
    public boolean hasBibliografy() {
        return getBibliografy() != null;
    }

    @Deprecated
    public boolean hasPersonalization() {
        return getPersonalization() != null;
    }

    @Deprecated
    public boolean hasEvaluationTests() {
        return getEvaluationTests() != null;
    }

    @Deprecated
    public boolean hasWorksheets() {
        return getWorksheets() != null;
    }

    @Deprecated
    public boolean hasPlanning() {
        return getPlanning() != null;
    }

    @Deprecated
    public boolean hasAnnouncements() {
        return getAnnouncements() != null;
    }

    @Deprecated
    public boolean hasEvaluationExams() {
        return getEvaluationExams() != null;
    }

    @Deprecated
    public boolean hasEvaluationMethod() {
        return getEvaluationMethod() != null;
    }

    @Deprecated
    public boolean hasEvaluationWorksheets() {
        return getEvaluationWorksheets() != null;
    }

    @Deprecated
    public boolean hasSummaries() {
        return getSummaries() != null;
    }

}
