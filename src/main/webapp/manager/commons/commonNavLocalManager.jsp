<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<center>
	<img src="<%= request.getContextPath() %>/images/ID_FenixEdu.png" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />"  />
</center>
<br/>

<jsp:include page="/commons/functionalities/side-menu.jsp"/>
