<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/teachersManagement.do?method=mainPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
	</html:link>
</strong></p>

<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles"/></h2>

<p><strong>&raquo;
	<html:link module="/manager" module="/manager" page="/generateFiles.do?method=prepareChooseForGenerateFiles&amp;file=sibs">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.SIBS" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link module="/manager" module="/manager" page="/generateFiles.do?method=prepareChooseForGenerateFiles&amp;file=letters">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.letters" />
	</html:link>
</strong></p>