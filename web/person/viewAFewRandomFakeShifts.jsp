<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:html xhtml="true"/>

<bean:define id="fakeShifts" name="fakeShifts" type="java.util.Collection" />

<h2>
	Here's a few random Fake Shifts:
</h2>

<h3>
	<%= fakeShifts.size() + " fake shifts found." %> 
</h3>

<fr:view name="fakeShifts">
	<fr:schema type="net.sourceforge.fenixedu.domain.FakeShift" bundle="APPLICATION_RESOURCES" >
		<fr:slot name="name"/>
		<fr:slot name="capacity"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:link label="view" name="view" link="/loadTesting.do?method=viewFakeShift&fakeShift=${externalId}" />
	</fr:layout>
</fr:view>

<br/><br/><br/><br/><br/>
<hr/>
<br/>

<html:link styleId="manageFakeShifts" page="/loadTesting.do?method=manageFakeShifts">
	Manage All Fake Shifts
</html:link>
