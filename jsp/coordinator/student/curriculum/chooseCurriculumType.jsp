<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.student.curriculum" /></h2>

<span class="error0"><!-- Error messages go here --><html:errors /></span>


<logic:present role="COORDINATOR">

	<bean:define id="studentNumber" name="student" property="number" />
	<bean:define id="studentId" name="student" property="idInternal" />
	<bean:define id="executionDegreeId" name="viewStudentCurriculumForm"
		property="executionDegreeId" />
	<bean:define id="degreeCurricularPlanId"
		name="viewStudentCurriculumForm" property="degreeCurricularPlanId" />
	<table>
		<tr>
			<td><html:link
				action="<%="/viewCurriculum.do?method=prepareReadByStudentNumber&studentNumber=" + studentNumber.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanId.toString() + "&executionDegreeId=" + executionDegreeId.toString()%>">
				<bean:message key="label.preBolonha" bundle="APPLICATION_RESOURCES" />
			</html:link></td>
		</tr>
		<tr>

			<td><html:link
				action="<%="/bolonhaTransitionManagement.do?method=prepare&studentId=" + studentId.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanId.toString() + "&executionDegreeId=" + executionDegreeId.toString() %>">
				<bean:message key="label.bolonha" bundle="APPLICATION_RESOURCES" />
			</html:link></td>
		</tr>
	</table>

</logic:present>