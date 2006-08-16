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
	<li>
		<html:link page="/executionCourse.do?method=evaluationMethod"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.evaluationMethod"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=bibliographicReference"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.bibliography"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=schedule"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="label.schedule"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=shifts"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.shifts"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=evaluations"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.evaluations"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=groupings"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.groupings"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=section"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.section"/>
		</html:link>
	</li>
	<li>
		<html:link page="/executionCourse.do?method=rss"
				paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message  key="link.rss"/>
		</html:link>
	</li>
</ul>
