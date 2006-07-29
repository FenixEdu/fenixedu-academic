<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.studentListByCourse" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<br />
<logic:present name="curricularCourses">
	<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />
	<br /><br />
	<bean:define id="path" value="/listStudentsByCourse" />
	<table>
		<!-- CurricularCourse -->
		<logic:iterate id="curricularCourseElem" name="curricularCourses">
		   	<bean:define id="courseID" name="curricularCourseElem" property="idInternal"/>
		   	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
			<html:link page="<%= path + ".do?method=chooseCurricularCourseByID&amp;courseID=" + courseID + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
				<bean:write name="curricularCourseElem" property="name"/>
			</html:link>
			<br />
		</logic:iterate>
	</table>
</logic:present>