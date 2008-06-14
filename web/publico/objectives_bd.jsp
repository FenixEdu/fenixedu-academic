<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<logic:present name="siteView" property="component" >
	<bean:define id="component" name="siteView" property="component"/>
	<logic:empty name="component" property="generalObjectives">
	<h2><bean:message key="message.objectives.not.available"/></h2>
	</logic:empty>

	<logic:notEmpty name="component" property="generalObjectives">
		<h2><bean:message key="label.generalObjectives" />	</h2>
		<p>
			<bean:write name="component" property="generalObjectives" filter="false"/>
		</p>
	</logic:notEmpty>
	<logic:notEmpty name="component" property="generalObjectives">
		<h2><bean:message key="label.operacionalObjectives" /></h2>
		<p>
			<bean:write name="component" property="operacionalObjectives" filter="false"/>
		</p>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="siteView" property="component">
<h2><bean:message key="message.objectives.not.available"/>
</h2>
</logic:notPresent>