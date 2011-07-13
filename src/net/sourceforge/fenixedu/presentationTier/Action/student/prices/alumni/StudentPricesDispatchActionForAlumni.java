package net.sourceforge.fenixedu.presentationTier.Action.student.prices.alumni;

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

@Mapping(module = "alumni", path = "/prices", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "viewPrices", path = "/alumni/administrativeOfficeServices/prices/viewPrices.jsp") })
public class StudentPricesDispatchActionForAlumni extends net.sourceforge.fenixedu.presentationTier.Action.student.prices.StudentPricesDispatchAction {
}