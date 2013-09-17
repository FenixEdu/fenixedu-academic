<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:html xhtml="true"/>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl" %>
<%@page import="net.sourceforge.fenixedu.domain.Person" %>

<h2>
	Fake Enrollments
</h2>
<html:link styleId="create" page="/loadTesting.do?method=createFakeEnrollment">
	Create Fake Enrollment
</html:link>
<br/>
<html:link styleId="reset" page="/loadTesting.do?method=resetFakeEnrollments">
	Reset All Fake Enrollments
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
