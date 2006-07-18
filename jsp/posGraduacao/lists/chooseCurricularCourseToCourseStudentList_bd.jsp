<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.studentListByCourse" /></h2>
<span class="error"><html:errors/></span>
<br />
<logic:present name="curricularCourses">
	<logic:notEmpty name="curricularCourses">
	<logic:iterate id="curricularCourseElem" name="curricularCourses" length="1">
		<b><bean:message key="label.degree" />:</b>&nbsp;<bean:write name="curricularCourseElem" property="infoDegreeCurricularPlan.name"/>
		<br /><br />
	</logic:iterate>
	<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />
	<br /><br />
	<table>
		<!-- CurricularCourse -->
		<logic:iterate id="curricularCourseElem" name="curricularCourses">
		   	<bean:define id="courseID" name="curricularCourseElem" property="idInternal"/>
		   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>
		   	<bean:define id="curricularPlanID" name="curricularCourseElem" property="infoDegreeCurricularPlan.idInternal"/>

			<html:link page="<%= "/listCourseStudents.do?method=getStudentsFromCourse&amp;scopeCode=" + courseID + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse")+"&amp;curricularPlanID=" + curricularPlanID%>">
				<bean:write name="curricularCourseElem" property="name"/>
			</html:link>
			<br />
		</logic:iterate>
	</table>
	</logic:notEmpty>
	<logic:empty name="curricularCourses">
		<bean:message key="error.nonExisting.AssociatedCurricularCourses"/>
	</logic:empty>
</logic:present>