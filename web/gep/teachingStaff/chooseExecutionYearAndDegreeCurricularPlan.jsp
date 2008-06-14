<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<p>
	<strong>
		<bean:message key="label.inquiries.chooseCurricularPlan" bundle="INQUIRIES_RESOURCES"/>:
	</strong>	
</p>

<html:form action="/teachingStaff">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectExecutionDegree"/>
	<bean:define id="executionYearID" type="java.lang.Integer" name="executionYear" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearID" property="executionYearID" value="<%= executionYearID.toString() %>"/>

	<logic:present name="degreeCurricularPlans">
		<table>
			<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans">
					<tr>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.degreeCurricularPlanID" property="degreeCurricularPlanID" idName="degreeCurricularPlan" value="idInternal" />
						</td>
						<td>
							<bean:message name="degreeCurricularPlan" property="infoDegree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
						</td>
						<td>
							<bean:write name="degreeCurricularPlan" property="name" />
						</td>
						<td>
							<bean:write name="degreeCurricularPlan" property="infoDegree.nome" />
						</td>
					</tr>
			</logic:iterate>
		</table>
		<p/>
		<html:submit>
			<bean:message key="button.inquiries.choose" bundle="INQUIRIES_RESOURCES"/>
		</html:submit>
	</logic:present>
</html:form>
