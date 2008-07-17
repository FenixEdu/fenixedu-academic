package net.sourceforge.fenixedu.dataTransferObject.alumni.formation;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.DomainReference;

public class AlumniFormationBean implements Serializable {

    private DomainReference<Alumni> alumni;
    private AlumniFormation alumniFormation;

    public AlumniFormationBean(Alumni alumni) {
	setAlumni(alumni);
	alumniFormation = new AlumniFormation();
    }

    public Alumni getAlumni() {
	return (this.alumni != null) ? this.alumni.getObject() : null;
    }

    public void setAlumni(Alumni alumni) {
	this.alumni = new DomainReference<Alumni>(alumni);
    }

    public AlumniFormation getAlumniFormation() {
	return alumniFormation;
    }

    public void setAlumniFormation(AlumniFormation alumniFormation) {
	this.alumniFormation = alumniFormation;
    }
    
    public int getSize() {
	return this.alumni.getObject().getFormations().size();
    }

}
