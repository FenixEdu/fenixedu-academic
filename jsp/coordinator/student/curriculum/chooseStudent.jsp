<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<logic:present role="COORDINATOR">
	<h2><bean:message key="title.student.curriculum" /></h2>

	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>

	<html:form focus="studentNumber" method="post" action="/viewStudentCurriculum.do">

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