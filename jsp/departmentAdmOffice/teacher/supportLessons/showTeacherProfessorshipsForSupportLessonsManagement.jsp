<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<bean:parameter id="executionPeriodId" name="executionPeriodId"/>
<tiles:insert definition="show-teacher-professorships-layout">
	<tiles:put name="infoTeacher" beanName="infoTeacher"/>
	<tiles:put name="executionCourseLink">
		/manageTeacherExecutionCourseSupportLessons.do?page=0&amp;method=showForm&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;executionPeriodId=<bean:write name="executionPeriodId"/>
	</tiles:put>
	<tiles:put name="detailedProfessorshipList" beanName="detailedProfessorshipList"/>
	<tiles:put name="paramId" value="executionCourseId"/>
</tiles:insert>
