<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:html xhtml="true"/>


<h2>
	Viewing the Fake Shift:
</h2>

<bean:define id="fakeShift" name="fakeShift"/>

Name: <bean:write name="fakeShift" property="name"/>
<br/>
Capacity: <bean:write name="fakeShift" property="capacity"/>
