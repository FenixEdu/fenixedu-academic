package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.precedences.Precedence;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoPrecedence extends InfoObject {

    protected InfoCurricularCourse infoCurricularCourse;

    public InfoPrecedence() {
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
	return infoCurricularCourse;
    }

    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
	this.infoCurricularCourse = infoCurricularCourse;
    }

    public void copyFromDomain(Precedence precedence) {
	super.copyFromDomain(precedence);
	this.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(precedence.getCurricularCourse()));
    }

    public static InfoPrecedence newInfoFromDomain(Precedence precedence) {

	InfoPrecedence infoPrecedence = null;

	if (precedence != null) {
	    infoPrecedence = new InfoPrecedence();
	    infoPrecedence.copyFromDomain(precedence);
	}

	return infoPrecedence;
    }

}