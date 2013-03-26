package net.sourceforge.fenixedu.dataTransferObject.alumni.formation;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;

public class AlumniFormationBean implements Serializable {

    private Alumni alumni;
    private AlumniFormation alumniFormation;

    public AlumniFormationBean(Alumni alumni) {
        setAlumni(alumni);
        alumniFormation = new AlumniFormation();
    }

    public Alumni getAlumni() {
        return this.alumni;
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public AlumniFormation getAlumniFormation() {
        return alumniFormation;
    }

    public void setAlumniFormation(AlumniFormation alumniFormation) {
        this.alumniFormation = alumniFormation;
    }

    public int getSize() {
        return this.alumni.getFormations().size();
    }

}
