package net.sourceforge.fenixedu.presentationTier.Action.masterDegreeCandidate;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "masterDegreeCandidate",
		path = "/exceptionHandling",
		attribute = "errorMailForm",
		formBean = "errorMailForm",
		scope = "request",
		parameter = "method")
public class ExceptionHandlingActionForMasterDegreeCandidate extends
		net.sourceforge.fenixedu.presentationTier.Action.ExceptionHandlingAction {
}