<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="definition.student.curricular.course.enrolment" flush="true">
	<tiles:put name="body" value="/student/acceptEnrolment_bd.jsp" />
</tiles:insert>