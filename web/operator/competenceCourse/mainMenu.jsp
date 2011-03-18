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
	<li>
		<html:link page="/createEditCompetenceCourse.do?method=prepareCreate">
			<bean:message key="label.insert.edit.competency.course" bundle="MANAGER_RESOURCES" />
		</html:link>
	</li>
	<!--<li>
		<html:link page="/curricularCoursesCompetenceCourse.do?method=readDegrees" module="/manager">
			<bean:message key="label.manager.create.competence.course" bundle="MANAGER_RESOURCES" />
		</html:link>
	</li>-->
</ul>