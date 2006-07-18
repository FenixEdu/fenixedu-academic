<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<center>
	<img src="<%= request.getContextPath() %>/images/logo-fenix.gif" alt="<bean:message key="logo-fenix" bundle="IMAGE_RESOURCES" />" width="100" height="100"/>
</center>

<br/>
	
<ul>
	<li>
		<html:link module="/manager" page="/index.do">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
		</html:link>
	</li>
	
	<li class="navheader">Gest�o de <bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegreeManagement"/></li>
	<li>
	  	<html:link module="/manager" page="/degree/chooseDegreeType.faces">
	  		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.createExecutionDegrees"/>
	  	</html:link>
	</li>
	
	<li>
	  	<html:link module="/manager" page="/executionDegreesManagement.do?method=readDegreeCurricularPlans">
	  		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.editExecutionDegrees"/>
	  	</html:link>
	</li>

</ul>