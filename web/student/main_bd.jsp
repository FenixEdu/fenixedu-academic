<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<p>
	<img src="<%= request.getContextPath() %>/images/portalEst-id.gif" alt="<bean:message key="portalEst-id" bundle="IMAGE_RESOURCES" />" />
</p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<div class="photo" align="center">
	<img src="<%= request.getContextPath() %>/images/student_photo1.jpg" alt="<bean:message key="student_photo1" bundle="IMAGE_RESOURCES" />" width="150" height="100" />
	<img src="<%= request.getContextPath() %>/images/student_photo2.jpg" alt="<bean:message key="student_photo2" bundle="IMAGE_RESOURCES" />" width="150" height="100" />
	<img src="<%= request.getContextPath() %>/images/student_photo3.jpg" alt="<bean:message key="student_photo3" bundle="IMAGE_RESOURCES" />" width="150" height="100" />
</div>
<p>
	<bean:message key="message.info.student" />
</p>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>
