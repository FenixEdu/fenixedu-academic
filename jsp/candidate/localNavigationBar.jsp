<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->

<logic:present role="CANDIDATE">
	<ul>
		<li><html:link page="/changePersonalData.do?method=prepare" ><bean:message key="link.changePersonalData"/></html:link></li>
		<li><html:link page="/viewCandidacies.do?method=prepare" ><bean:message key="link.viewCandidacies"/></html:link></li>
	</ul>	
</logic:present>
