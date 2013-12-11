package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;

public class ProfessorshipPermissions extends ProfessorshipPermissions_Base {

    public ProfessorshipPermissions(final Professorship professorship) {
        super();
        setRootDomainObject(Bennu.getInstance());
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

}
