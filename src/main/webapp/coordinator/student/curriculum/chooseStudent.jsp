<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


<logic:present role="COORDINATOR">
	<h2><bean:message key="title.student.curriculum" /></h2>

	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>

	<html:form focus="studentNumber" method="post" action="/viewStudentCurriculumSearch.do">
		<html:hidden property="degreeCurricularPlanID" value="<%= request.getAttribute("degreeCurricularPlanID").toString() %>"/>
		<html:hidden property="method" value="showStudentCurriculum"/>
		<html:hidden property="executionDegreeId" />
		<html:hidden property="degreeCurricularPlanId" />

		<table class="tstyle5">
			<tr>
				<td><bean:message key="label.choose.student" /></td>
				<td><html:text property="studentNumber"
					alt="input.studentNumber" maxlength="5" size="5"></html:text></td>
			</tr>
		</table>

		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			styleClass="inputbutton">
			<bean:message key="button.submit.student" />
		</html:submit></p>
	</html:form>


</logic:present>