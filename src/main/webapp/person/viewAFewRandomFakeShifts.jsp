<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:html xhtml="true"/>

<bean:define id="fakeShifts" name="fakeShifts" type="java.util.Collection" />

<h2>
	Here's a few random Fake Shifts:
</h2>

<h3>
	<%= fakeShifts.size() + " fake shifts found." %> 
</h3>

<div id="fakeShifts">
	<fr:view name="fakeShifts">
		<fr:schema type="net.sourceforge.fenixedu.domain.FakeShift" bundle="APPLICATION_RESOURCES" >
			<fr:slot name="name"/>
			<fr:slot name="capacity"/>
			<fr:slot name="vacancies"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:link label="view" name="view" link="/loadTesting.do?method=viewFakeShift&fakeShift=${externalId}" />
		</fr:layout>
	</fr:view>
</div>

<br/><br/><br/><br/><br/>
<hr/>
<br/>

<html:link styleId="manageFakeShifts" page="/loadTesting.do?method=manageFakeShifts">
	Manage All Fake Shifts
</html:link>
