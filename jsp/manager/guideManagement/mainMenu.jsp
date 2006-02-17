<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<div style="font-size: 1.20em;">

<p><strong>&raquo; 
	<html:link module="/manager" page="/teachersManagement.do?method=mainPage">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
	</html:link>
</strong></p>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.guidesManagement"/></h2>

<p><strong>&raquo;
	<html:link module="/manager" page="/guideManagement.do?method=prepareChooseGuide">
		<bean:message bundle="MANAGER_RESOURCES" key="label.editGuide" />
	</html:link>
</strong></p>

</div>
