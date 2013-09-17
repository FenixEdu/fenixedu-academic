<%@page import="net.sourceforge.fenixedu.domain.Degree"%>
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:html xhtml="true"/>

<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl" %>
<%@page import="net.sourceforge.fenixedu.domain.Person" %>

<h2>
	Load Testing
</h2>

<% Degree degree = (Degree) FenixFramework.getDomainObject((String)request.getAttribute("degreeOID")); %>

<%= RequestRewriter.HAS_CONTEXT_PREFIX %><a id="viewRandom" href="<%= request.getContextPath()
							+ "/publico/degreeSite/showDegreeCurricularPlanBolonha.faces?degreeID=" + request.getAttribute("degreeID")
							+ "&degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() 
							+ "&executionPeriodOID=" + request.getAttribute("executionPeriodOID")
							+ "&organizeBy=groups&showRules=false&hideCourses=false"
							+ "&" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME + "=/cursos/" + degree.getSigla() + "/plano-curricular"%>" >
	View a Random Degree Curricular Plan
</a>

<br/>

<html:link styleId="fakeEnrollments" page="/loadTesting.do?method=manageFakeEnrollments">
	Fake Enrollments
</html:link>

<br/>

<html:link styleId="fakeShifts" page="/loadTesting.do?method=viewAFewRandomFakeShifts">
	Fake Shifts
</html:link>
