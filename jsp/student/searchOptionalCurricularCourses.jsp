<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert definition="definition.student.masterPage" flush="true">
	<tiles:put name="body" value="/student/searchOptionalCurricularCourses_bd.jsp" />
</tiles:insert>