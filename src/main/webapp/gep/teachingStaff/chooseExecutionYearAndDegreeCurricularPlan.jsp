<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<p>
	<strong>
		<bean:message key="label.inquiries.chooseCurricularPlan" bundle="INQUIRIES_RESOURCES"/>:
	</strong>	
</p>

<html:form action="/teachingStaff">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectExecutionDegree"/>
	<bean:define id="executionYearID" type="java.lang.String" name="executionYear" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearID" property="executionYearID" value="<%= executionYearID.toString() %>"/>

	<logic:present name="degreeCurricularPlans">
		<table>
			<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans">
					<tr>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.degreeCurricularPlanID" property="degreeCurricularPlanID" idName="degreeCurricularPlan" value="externalId" />
						</td>
						<td>
							<bean:message name="degreeCurricularPlan" property="infoDegree.degreeType.name" bundle="ENUMERATION_RESOURCES"/>
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
