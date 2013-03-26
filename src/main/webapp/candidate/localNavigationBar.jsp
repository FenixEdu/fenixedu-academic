<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="CANDIDATE">
	<ul>
		<li><html:link page="/viewCandidacies.do?method=prepare" ><bean:message key="link.candidacies"/></html:link></li>
	</ul>	
</logic:present>
