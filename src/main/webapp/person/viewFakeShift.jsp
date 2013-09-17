<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:html xhtml="true"/>


<h2>
	Viewing the Fake Shift:
</h2>

<bean:define id="fakeShift" name="fakeShift" type="net.sourceforge.fenixedu.domain.FakeShift"/>

Name: <bean:write name="fakeShift" property="name"/>
<br/>
Capacity: <bean:write name="fakeShift" property="capacity"/>
<br/>
Vacancies: <bean:write name="fakeShift" property="vacancies"/>
<br>

<logic:messagesPresent message="true">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<br><br><br>
<html:link styleId="create" page="<%= "/loadTesting.do?method=createFakeShiftEnrollment&fakeShift=" + fakeShift.getExternalId() %>">
	Create Fake Shift Enrollment
</html:link>
<br>
<html:link styleId="delete" page="<%= "/loadTesting.do?method=resetFakeShiftEnrollments&fakeShift=" + fakeShift.getExternalId() %>">
	Reset this Shift's Enrollments
</html:link>
<br><br><br>
<%
	int fakeShiftEnrollmentsSize = fakeShift.getFakeShiftEnrollmentsForCurrentUser().size();
	request.setAttribute("fakeShiftEnrollmentsSize", fakeShiftEnrollmentsSize);
%>

<logic:notEqual name="fakeShiftEnrollmentsSize" value="0">
	<h3>
		<%= "You have created <strong>" + fakeShiftEnrollmentsSize + "</strong> fake enrollments in this fake shift. Congratulations!" %> 
	</h3>
</logic:notEqual>
