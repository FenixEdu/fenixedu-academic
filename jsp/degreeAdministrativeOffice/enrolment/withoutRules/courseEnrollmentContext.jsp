<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="infoStudentEnrolmentContext">
	<bean:define id="studentName" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome" />
	<bean:define id="studentNumber" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number" />
</logic:present>


<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
			<b><bean:message key="label.student.enrollment.number"/></b>&nbsp;
			<logic:present name="studentName">
				<ben:write name="studentName" />				
			</logic:present>
			&nbsp;
			<logic:present name="studentNumber">
				<ben:write name="studentNumber" />
			</logic:present>
			
			<br/>
			<b><bean:message key="label.executionYear" /></b>&nbsp;
			<logic:present name="executionYear">
				<ben:write name="executionYear" />
			</logic:present>
		</td>
	</tr>
</table>
