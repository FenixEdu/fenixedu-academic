package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Professorship;

public class InfoProfessorship extends InfoObject {
    
    private Professorship professorship;
    
    private InfoProfessorship(final Professorship professorship) {
	this.professorship = professorship;
    }
    
    public static InfoProfessorship newInfoFromDomain(Professorship professorship) {
	return (professorship != null) ? new InfoProfessorship(professorship) : null;
    }

    public InfoExecutionCourse getInfoExecutionCourse() {
        return InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(this.professorship.getExecutionCourse());
    }

    public InfoTeacher getInfoTeacher() {
        return InfoTeacher.newInfoFromDomain(this.professorship.getTeacher());
    }

    public Double getHours() {
        return this.professorship.getHours();
    }

    public Boolean getResponsibleFor(){
        return this.professorship.isResponsibleFor();
    }
    
    @Override
    public Integer getIdInternal() {
        return this.professorship.getIdInternal();
    }
    
    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }
}