<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView">
	<bean:define id="component" name="siteView" property="component"/>

	<h2><bean:message key="title.evaluationMethod"/></h2>				
	<logic:notEmpty name="component" property="evaluationElements">	
		<p><bean:write name="component" property="evaluationElements" filter="false"/></p>
	</logic:notEmpty>

	<logic:notEmpty name="component" property="evaluationElementsEn">	
		<h2><bean:message key="title.evaluationMethod.eng"/></h2>
		<p><bean:write name="component" property="evaluationElementsEn" filter="false"/></p>
	</logic:notEmpty>
</logic:present> 