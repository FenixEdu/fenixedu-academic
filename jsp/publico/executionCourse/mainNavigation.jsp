<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li>
		<html:link page="/executionCourse.do?method=firstPage"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.inicialPage"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=announcements"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.announcements"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=summaries"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.summaries"/>
		</html:link>
	</li>
</ul>
