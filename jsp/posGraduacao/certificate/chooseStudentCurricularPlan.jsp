<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:equal name="path" value="Certificate">
	<h2><bean:message key="label.certificate.create" /></h2>
</logic:equal>
<logic:equal name="path" value="Declaration">
	<h2><bean:message key="label.certificate.declaration.create" /></h2>
</logic:equal>
<logic:equal name="path" value="FinalResult">
	<h2><bean:message key="label.certificate.finalResult.create" /></h2>
</logic:equal>


<logic:present name="studentCurricularPlans">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<logic:iterate id="studentCurricularPlan" name="studentCurricularPlans" length="1">
					<b><bean:message key="student" /></b>&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.number" />&nbsp;<bean:write name="studentCurricularPlan" property="infoStudent.infoPerson.nome" /><br>

				</logic:iterate>
			</td>
		</tr>
	</table>
	<p/>
	<logic:iterate id="studentCurricularPlan" name="studentCurricularPlans">
		<bean:define id="studentCurricularPlanID" name="studentCurricularPlan" property="idInternal" />
		<bean:define id="path" name="path" />
	
		<html:link page="<%= "/choose" + pageContext.getAttribute("path") + "InfoAction.do?method=chooseFinal&amp;studentCurricularPlanID=" + pageContext.getAttribute("studentCurricularPlanID")%>"><bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.name" /></html:link>
		&nbsp;<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;
		<bean:define id="initialDate" name="studentCurricularPlan" property="infoDegreeCurricularPlan.initialDate" />		
		<%= initialDate.toString().substring(initialDate.toString().lastIndexOf(" ")+1) %>
		<logic:notEmpty name="studentCurricularPlan" property="infoDegreeCurricularPlan.endDate">
			<bean:define id="endDate" name="studentCurricularPlan" property="infoDegreeCurricularPlan.endDate" />	
		-<%= endDate.toString().substring(endDate.toString().lastIndexOf(" ")) %><br/><br/>
		</logic:notEmpty>
	</logic:iterate>
</logic:present>