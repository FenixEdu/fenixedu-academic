<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<logic:present name="siteView" property="component" > 
	<bean:define id="component" name="siteView" property="component"/>
	<logic:notEmpty name="component" property="evaluationElements">
	<h2><bean:message key="label.evaluation" /></h2>	
	<p>
		<bean:write name="component" property="evaluationElements" filter="false"/>
	</p>
	</logic:notEmpty>
	<logic:empty name="component" property="evaluationElements">
		<h2><bean:message key="message.evaluation.not.available"/></h2>
	</logic:empty>	
</logic:present>
<logic:notPresent name="siteView" property="component">
	<h2><bean:message key="message.evaluation.not.available"/></h2>
</logic:notPresent>