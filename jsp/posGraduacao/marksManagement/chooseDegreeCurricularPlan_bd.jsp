<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.students.listMarks" /></h2>
<logic:present name="degreeCurricularPlans">
	<table width="100%">
		<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans" length="1">	
			<tr>
				<td class="infoselected">
					<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</b>
					<bean:write name="degreeCurricularPlan" property="infoDegree.nome" />
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<span class="error"><html:errors/></span>
	<bean:message key="label.masterDegree.administrativeOffice.chooseDegreeCurricularPlan" />:
	<br /><br />
	<table>
	   <!-- Degree Curricular Plans -->
		<logic:iterate id="degreeCurricularPlanElem" name="degreeCurricularPlans" type="net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan">
			<tr>
				<td>
					<html:link page="<%= "/marksManagement.do?method=chooseCurricularCourses&amp;degreeId=" + degreeCurricularPlanElem.getInfoDegree().getIdInternal() + "&amp;objectCode=" + degreeCurricularPlanElem.getIdInternal()%>">
						<bean:write name="degreeCurricularPlanElem" property="infoDegree.nome"/>
						&nbsp;-&nbsp;<bean:write name="degreeCurricularPlanElem" property="name"/>
					</html:link>
				</td>
	   		</tr>
	   	</logic:iterate>
	</table>
	<br />
</logic:present>
<logic:notPresent name="degreeCurricularPlans">
	<bean:message key="error.masterDegree.noDegreeCurricularPlan" />
</logic:notPresent>