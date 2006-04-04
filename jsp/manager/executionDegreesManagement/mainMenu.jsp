<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<div style="font-size: 1.20em;">
	
	<p><strong>&raquo; 
		<html:link module="/manager" page="/index.do">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.mainPage" />
		</html:link>
	</strong></p>
	
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegreeManagement"/></h3>

	<p><strong>&raquo; 
  	<html:link module="/manager" page="/degree/chooseDegreeType.faces">
  		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.createExecutionDegrees"/>
  	</html:link>
	</strong></p>
	
	<p><strong>&raquo; 
  	<html:link module="/manager" page="/executionDegreesManagement.do?method=readDegreeCurricularPlans">
  		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.editExecutionDegrees"/>
  	</html:link>
	</strong></p>

</div>