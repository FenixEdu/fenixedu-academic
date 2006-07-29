<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="label.students.listMarks"/></h2>
<logic:present name="oneInfoEnrollment">
	<table width="100%">
		<tr>
			<td class="infoselected">
				<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</b>
				<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome" />
				<br />
				<b><bean:message key="label.curricularPlan" />:</b>
				<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.name" />
				<br />
				<b><bean:message key="label.curricularCourse"/>:</b>
				<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.name" />
			</td>
		</tr>
	</table>
</logic:present>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/changeMarkDispatchAction?method=chooseStudentMarks" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.courseId" property="courseId" value="<%= pageContext.findAttribute("courseId").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= pageContext.findAttribute("degreeId").toString() %>" />
	<table>
    	<tr>
        	<td><bean:message key="label.number"/>: </td>
         	<td>
         		<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" value=""/>      
         	</td>
        </tr>
	</table>
	<br />
	<br /><br />	
 	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.next"/>
  	</html:submit>
</html:form>