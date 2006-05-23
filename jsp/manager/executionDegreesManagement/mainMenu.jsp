<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<ul>

	<li>
		<html:link module="/manager" page="/index.do">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
		</html:link>
	</li>
	
	<li class="navheader"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegreeManagement"/></li>
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