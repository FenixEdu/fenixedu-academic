<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<ul>
	<li>
		<html:link page="/back.do">
			<bean:message key="label.return" bundle="MANAGER_RESOURCES" />
		</html:link>
	</li>
		
	<li class="navheader"><bean:message key="title.manager.executionDegreeManagement" bundle="MANAGER_RESOURCES" /></li>
		<li>
			<html:link page="/degree/chooseDegreeType.faces" module="/manager">
				<bean:message key="label.manager.createExecutionDegrees" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
    	<li>
			<html:link page="/executionDegreesManagement.do?method=readDegreeCurricularPlans">
				<bean:message key="label.manager.editExecutionDegrees" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
    
</ul>