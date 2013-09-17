<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<logic:messagesPresent message="true" property="successHead">
	<p>
		<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="successHead">
			<span class="success5"><bean:write name="messages" /></span>
		</html:messages>
	</p>
	<logic:messagesPresent message="true" property="successDCP">
		<ul>
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="successDCP">
				<li><span class="success2"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
</logic:messagesPresent>
