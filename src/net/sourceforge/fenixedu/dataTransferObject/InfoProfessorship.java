package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Professorship;

public class InfoProfessorship extends InfoObject {

    private DomainReference<Professorship> professorship;

    private InfoProfessorship(final Professorship professorship) {
        this.professorship = new DomainReference<Professorship>(professorship);
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
    public Integer getIdInternal() {
        return this.getProfessorship().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

    private Professorship getProfessorship() {
        return professorship == null ? null : professorship.getObject();
    }
}