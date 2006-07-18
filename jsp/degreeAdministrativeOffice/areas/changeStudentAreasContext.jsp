<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
			<logic:present name="studentNumber">
				<b><bean:message key="label.student.enrollment.number"/></b>&nbsp;
				<bean:write name="studentNumber"/>
				<br/>
			</logic:present>
			<logic:present name="degreeCurricularPlan">
				<b><bean:message key="label.degreeCurricularPlan"/>:</b>&nbsp;
				<bean:write name="degreeCurricularPlan" property="name"/>
				<br/>
			</logic:present>
			<logic:present name="specializationAreaName">
				<b><bean:message key="label.specializationAreaName"/>:</b>&nbsp;
				<bean:write name="specializationAreaName"/>
				<br/>
			</logic:present>
			<logic:present name="secondaryAreaName">
				<b><bean:message key="label.secondaryAreaName"/>:</b>&nbsp;
				<bean:write name="secondaryAreaName"/>
			</logic:present>
		</td>
	</tr>
</table>
