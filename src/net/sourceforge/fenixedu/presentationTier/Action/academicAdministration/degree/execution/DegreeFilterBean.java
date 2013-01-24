package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.degree.execution;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class DegreeFilterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DegreeType degreeType;
    private Degree degree;

    public DegreeType getDegreeType() {
	return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
	this.degreeType = degreeType;
    }

    public Degree getDegree() {
	return degree;
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }
}
