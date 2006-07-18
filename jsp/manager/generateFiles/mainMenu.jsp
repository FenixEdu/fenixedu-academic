<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<center>
	<img src="<%= request.getContextPath() %>/images/logo-fenix.gif" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" width="100" height="100"/>
</center>

<!--
<div style="font-size: 1.20em;">
</div>
-->

<ul>
	<li>
	<html:link module="/manager" module="/manager" page="/teachersManagement.do?method=mainPage">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
	</html:link>
	</li>
	<li class="navheader">
	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles"/>
	</li>
	<li>
	<html:link module="/manager" module="/manager" page="/generateFiles.do?method=prepareChooseForGenerateFiles&amp;file=sibs">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.SIBS" />
	</html:link>
	</li>
	<li>
	<html:link module="/manager" module="/manager" page="/generateFiles.do?method=prepareChooseForGenerateFiles&amp;file=letters">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.letters" />
	</html:link>
	</li>
</ul>
