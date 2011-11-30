<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:html xhtml="true"/>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl" %>
<%@page import="net.sourceforge.fenixedu.domain.Person" %>

<h2>
	Load Testing
</h2>

<html:link styleId="fakeEnrollments" page="/loadTesting.do?method=manageFakeEnrollments">
	Fake Enrollments
</html:link>

<br/>

<html:link styleId="fakeShifts" page="/loadTesting.do?method=viewAFewRandomFakeShifts">
	Fake Shifts
</html:link>
