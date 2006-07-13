<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
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

<li class="navheader">
	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement"/>
</li><li>	<html:link module="/manager" module="/manager" page="<%="/insertExecutionCourse.do?method=prepareInsertExecutionCourse"%>">		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.insert.executionCourse" />	</html:link></li><li>	<html:link module="/manager" module="/manager" page="<%="/editExecutionCourseChooseExPeriod.do?method=prepareEditECChooseExecutionPeriod"%>">		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse" />	</html:link></li><%--<li>	<html:link module="/manager" module="/manager" page="/readExecutionPeriods.do">		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.insert.executionCourse" />	</html:link></li>--%><li>	<html:link module="/manager" module="/manager" page="/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod">		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.join.executionCourse" />	</html:link></li><li>
	<html:link module="/manager" module="/manager" page="/executionCourseManagement/createCourseReportsForExecutionPeriod.faces">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.createCourseReports" />
	</html:link>
</li>

<li>
	<html:link module="/manager" module="/manager" page="/createExecutionCourses.do?method=chooseDegreeType">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.createExecutionCourses" />
	</html:link>
</li>

</ul>
