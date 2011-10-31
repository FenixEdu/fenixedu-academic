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
	Fake Enrollment Creation
</h2>

<html:link styleId="create" page="/fakeEnrollment.do?method=create">
	Create
</html:link>

<%
	Person person = AccessControl.getPerson();
	request.setAttribute("person", person);
%>

<logic:notEqual name="person" property="fakeEnrollmentCount" value="0">
	<h3>
		<%= "You have created <strong>" + person.getFakeEnrollmentCount() + "</strong> fake enrollments. Congratulations!" %> 
	</h3>
</logic:notEqual>
