<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert definition="show-teacher-professorships-layout">
	<tiles:put name="infoTeacher" beanName="infoTeacher"/>
	<tiles:put name="executionCourseLink">
		/manageTeacherExecutionCourseSupportLessons.do?page=0&amp;method=showForm&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>
	</tiles:put>
	<tiles:put name="infoProfessorshipList" beanName="infoProfessorshipList"/>
	<tiles:put name="paramId" value="executionCourseId"/>
</tiles:insert>
