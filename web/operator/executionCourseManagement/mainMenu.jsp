<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
	<li>
		<html:link page="/back.do">
			<bean:message key="label.return" bundle="MANAGER_RESOURCES" />
		</html:link>
	</li>
		
	<li class="navheader"><bean:message key="label.manager.executionCourseManagement" bundle="MANAGER_RESOURCES" /></li>
		<li>
			<html:link page="/insertExecutionCourse.do?method=prepareInsertExecutionCourse">
				<bean:message key="label.manager.executionCourseManagement.insert.executionCourse" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
    	<li>
			<html:link page="/editExecutionCourse.do?method=editExecutionCourse">
				<bean:message key="label.manager.executionCourseManagement.edit.executionCourse" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>
    
	<!--<li class="navheader"><bean:message key="" bundle="MANAGER_RESOURCES" /></li>
    	<li>
			<html:link page="">
				<bean:message key="" bundle="MANAGER_RESOURCES" />
			</html:link>
		</li>-->

</ul>