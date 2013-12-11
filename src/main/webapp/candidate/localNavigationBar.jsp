<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:present role="role(CANDIDATE)">
	<ul>
		<li><html:link page="/viewCandidacies.do?method=prepare" ><bean:message key="link.candidacies"/></html:link></li>
	</ul>	
</logic:present>
