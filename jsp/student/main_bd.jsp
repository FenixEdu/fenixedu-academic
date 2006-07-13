<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p><img src="<%= request.getContextPath() %>/images/portalEst-id.gif" alt="<bean:message key="portalEst-id" bundle="IMAGE_RESOURCES" />" /></p>
<span class="error"><html:errors/></span>
	<div class="photo" align="center">
		<img src="<%= request.getContextPath() %>/images/student_photo1.jpg" width="150px" height="100px" />
		<img src="<%= request.getContextPath() %>/images/student_photo2.jpg" width="150px" height="100px" />
		<img src="<%= request.getContextPath() %>/images/student_photo3.jpg" width="150px" height="100px" />
	</div>
<p><bean:message key="message.info.student" /></p>