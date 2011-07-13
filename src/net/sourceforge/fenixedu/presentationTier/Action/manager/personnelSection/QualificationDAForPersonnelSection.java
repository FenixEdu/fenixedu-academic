package net.sourceforge.fenixedu.presentationTier.Action.manager.personnelSection;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "personnelSection", path = "/qualification", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "qualification", path = "/manager/qualifications/qualification.jsp"),
		@Forward(name = "showQualifications", path = "/manager/qualifications/showQualifications.jsp"),
		@Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp") })
public class QualificationDAForPersonnelSection extends net.sourceforge.fenixedu.presentationTier.Action.manager.QualificationDA {
}