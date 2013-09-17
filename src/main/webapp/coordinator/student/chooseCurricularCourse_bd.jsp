<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<h2><bean:message key="title.studentListByCourse" /></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:present name="curricularCourses">
	<p>
		<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />:
	</p>

	<bean:define id="path" value="/listStudentsByCourse" />
	<ul>
		<!-- CurricularCourse -->
		<logic:iterate id="curricularCourseElem" name="curricularCourses">
		   	<bean:define id="courseID" name="curricularCourseElem" property="externalId"/>
		   	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
		   	<li>
				<html:link page="<%= path + ".do?method=chooseCurricularCourseByID&amp;courseID=" + courseID + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
					<bean:write name="curricularCourseElem" property="name"/>
				</html:link>
			</li>
		</logic:iterate>
	</ul>
</logic:present>