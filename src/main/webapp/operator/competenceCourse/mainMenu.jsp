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
	<li>
		<html:link page="/createEditCompetenceCourse.do?method=prepareCreate">
			<bean:message key="label.insert.edit.competency.course" bundle="MANAGER_RESOURCES" />
		</html:link>
	</li>
	<li>
		<html:link page="/curricularCoursesCompetenceCourse.do?method=readDegrees" module="/manager">
			<bean:message key="label.manager.create.competence.course" bundle="MANAGER_RESOURCES" />
		</html:link>
	</li>
</ul>