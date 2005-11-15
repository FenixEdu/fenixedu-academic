<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<p><strong>&raquo; 
	<html:link module="/manager" page="/teachersManagement.do?method=mainPage">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
	</html:link>
</strong></p>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manageFiles"/></h2>

<p><strong>&raquo;
	<html:link module="/manager" page="/uploadFiles.do?method=prepareChooseForUploadFiles&amp;file=sibs">
		<bean:message bundle="MANAGER_RESOURCES" key="label.uploadFiles.SIBS" />
	</html:link>
</strong></p>

