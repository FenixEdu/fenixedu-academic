<%@page import="net.sourceforge.fenixedu.domain.Degree"%>
<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:html xhtml="true"/>

<%@page import="pt.ist.fenixframework.pstm.AbstractDomainObject"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl" %>
<%@page import="net.sourceforge.fenixedu.domain.Person" %>

<h2>
	Load Testing
</h2>

<% Degree degree = (Degree) AbstractDomainObject.fromExternalId((String)request.getAttribute("degreeOID")); %>

<%= RequestRewriter.HAS_CONTEXT_PREFIX %><a id="viewRandom" href="<%= request.getContextPath()
							+ "/publico/degreeSite/showDegreeCurricularPlanBolonha.faces?degreeID=" + request.getAttribute("degreeID")
							+ "&degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() 
							+ "&executionPeriodOID=" + request.getAttribute("executionPeriodOID")
							+ "&organizeBy=groups&showRules=false&hideCourses=false"
							+ "&contentContextPath_PATH=/cursos/" + degree.getSigla() + "/plano-curricular"%>" >
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
