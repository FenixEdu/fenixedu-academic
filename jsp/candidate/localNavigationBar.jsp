<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="CANDIDATE">
	<ul>
		<li><html:link page="/changePersonalData.do?method=prepare" ><bean:message key="link.changePersonalData"/></html:link></li>
		<li><html:link page="/viewCandidacies.do?method=prepare" ><bean:message key="link.viewCandidacies"/></html:link></li>
	</ul>	
</logic:present>
