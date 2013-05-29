package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;

public class InfoProfessorship extends InfoObject {

    private final Professorship professorship;

    private InfoProfessorship(final Professorship professorship) {
        this.professorship = professorship;
    }

    public static InfoProfessorship newInfoFromDomain(Professorship professorship) {
        return (professorship != null) ? new InfoProfessorship(professorship) : null;
    }

    public InfoExecutionCourse getInfoExecutionCourse() {
        return InfoExecutionCourse.newInfoFromDomain(this.getProfessorship().getExecutionCourse());
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(this.getProfessorship().getTeacher());
    }

    public Double getHours() {
        return this.getProfessorship().getHours();
    }

    public Boolean getResponsibleFor() {
        return this.getProfessorship().isResponsibleFor();
    }

    @Override
    public String getExternalId() {
        return this.getProfessorship().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    private Professorship getProfessorship() {
        return professorship;
    }

    public Person getPerson() {
        return this.getProfessorship().getPerson();
    }
}