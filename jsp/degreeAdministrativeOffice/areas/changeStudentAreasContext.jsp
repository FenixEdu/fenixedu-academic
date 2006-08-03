<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:xhtml/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
			<logic:present name="studentCurricularPlan">
			
				<b><bean:message key="label.student.enrollment.number"/></b>&nbsp;
				<bean:write name="studentCurricularPlan" property="student.number" />
				<br/>
	
				<b><bean:message key="label.degreeCurricularPlan"/>:</b>&nbsp;
				<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.name"/>
				<br/>

				<logic:present name="studentCurricularPlan" property="branch">
					<b><bean:message key="label.specializationAreaName"/>:</b>&nbsp;
					<bean:write name="studentCurricularPlan" property="branch.name" />
					<br/>
				</logic:present>
				<logic:present name="studentCurricularPlan" property="secundaryBranch">
					<b><bean:message key="label.secondaryAreaName"/>:</b>&nbsp;
					<bean:write name="studentCurricularPlan" property="secundaryBranch.name"/>
				</logic:present>
		</logic:present>
		</td>
	</tr>
</table>
