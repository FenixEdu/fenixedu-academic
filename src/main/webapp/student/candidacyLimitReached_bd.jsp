<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>

<bean:define name="limit" type="java.lang.Integer" id="limit"/>
<logic:present name="limit">
	<bean:message key="error.seminaries.candidaciesLimitReached" arg0="<%=limit.toString()%>"/><br/>
	<html:link page="/listAllSeminaries.do">
		<bean:message key="label.seminaries.showCandidaciesLimitExceeded.Back"/>
	</html:link>
</logic:present>