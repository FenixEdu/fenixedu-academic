package net.sourceforge.fenixedu.presentationTier.Action.student.prices.student;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "student", path = "/prices", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "viewPrices", path = "prices.viewPrices") })
public class StudentPricesDispatchActionForStudent extends
        net.sourceforge.fenixedu.presentationTier.Action.student.prices.StudentPricesDispatchAction {
}