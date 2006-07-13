<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img src="<%= request.getContextPath() %>/images/logo-fenix.gif" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" width="100" height="100"/>
</center>

<ul>
	<li>
	<html:link module="/manager" page="/teachersManagement.do?method=mainPage">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
	</html:link>
	</li>
	<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="label.manageFiles"/></li>
	<li>
	<html:link module="/manager" page="/uploadFiles.do?method=prepareChooseForUploadFiles&amp;file=sibs">
		<bean:message bundle="MANAGER_RESOURCES" key="label.uploadFiles.SIBS" />
	</html:link>
	</li>
</ul>

