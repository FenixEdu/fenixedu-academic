<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<p><strong>&raquo; 
	<html:link page="/teachersManagement.do?method=mainPage">
		<bean:message key="label.manager.mainPage" />
	</html:link>
</strong></p>

<h2><bean:message key="label.manageFiles"/></h2>

<p><strong>&raquo;
	<html:link page="/uploadFiles.do?method=prepareChooseForUploadFiles&amp;file=sibs">
		<bean:message key="label.uploadFiles.SIBS" />
	</html:link>
</strong></p>

