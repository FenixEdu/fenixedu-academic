<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="link.masterDegree.administrativeOffice.gratuity.insertExemption" /></h2>

<logic:present name="studentCurricularPlans">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<logic:iterate id="studentCurricularPlan" name="studentCurricularPlans" length="1">
					<b><bean:message key="student" /></b>&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.number" />&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.infoPerson.nome" /><br>
					<b><bean:message key="title.studentCurricularPlan"/></b>&nbsp;<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;
					<bean:define id="initialDate" name="studentCurricularPlan" property="infoDegreeCurricularPlan.initialDate" />		
					<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
					<logic:notEmpty name="studentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
						<bean:define id="endDate" name="studentCurricularPlan" property="infoDegreeCurricularPlan.endDate" />	
					-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %>
					</logic:notEmpty>
				</logic:iterate>
			</td>
		</tr>
	</table>
	
	<logic:iterate id="studentCurricularPlan" name="studentCurricularPlans">
		<bean:define id="studentCurricularPlanID" name="studentCurricularPlan" property="idInternal" />
		<p>
			<html:link page="<%= "/manageExemptionGratuity.do?method=readExemptionGratuity&amp;page=0&amp;studentCurricularPlanID=" + pageContext.getAttribute("studentCurricularPlanID") + "&amp;executionYear=" + request.getAttribute("executionYear")%>">
				<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.name" />
				 - 
				<bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
			</html:link>
		</p>
	</logic:iterate>
</logic:present>