<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:html xhtml="true"/>

<bean:define id="fakeShifts" name="fakeShifts" type="java.util.Collection" />

<h2>
	All Fake Shifts
</h2>

<h3>
	<%= fakeShifts.size() + " fake shifts found." %> 
</h3>

<div id="fakeShifts">
	<fr:view name="fakeShifts">
		<fr:schema type="net.sourceforge.fenixedu.domain.FakeShift" bundle="APPLICATION_RESOURCES" >
			<fr:slot name="name"/>
			<fr:slot name="capacity"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:link label="view" name="view" link="/loadTesting.do?method=viewFakeShift&fakeShift=${externalId}" />
		</fr:layout>
	</fr:view>
</div>

<br/><br/><br/><br/><br/>
<hr/>
<br/>

<html:link styleId="import" page="/loadTesting.do?method=importFakeShifts">
	Import Fake Shifts (from real Shifts data)
</html:link>
<br/>
<html:link styleId="delete" page="/loadTesting.do?method=deleteFakeShifts">
	Delete All Fake Shifts
</html:link>
