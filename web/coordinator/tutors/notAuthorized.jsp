<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.teacher.tutor.operations"/></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<span class="error"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<span class="error"><!-- Error messages go here --><html:errors /></span><br/>