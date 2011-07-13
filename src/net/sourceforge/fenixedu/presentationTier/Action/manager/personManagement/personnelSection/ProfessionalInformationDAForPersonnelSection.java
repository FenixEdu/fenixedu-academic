package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.personnelSection;

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

@Mapping(module = "personnelSection", path = "/professionalInformation", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showProfessionalInformation", path = "/manager/personManagement/contracts/showProfessionalInformation.jsp") })
public class ProfessionalInformationDAForPersonnelSection extends net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.ProfessionalInformationDA {
}