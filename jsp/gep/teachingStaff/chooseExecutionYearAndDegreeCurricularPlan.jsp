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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearID" property="executionYearID" value="44"/>

	<!--<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID"
		onchange="this.form.method.value='selectExecutionYear';this.form.submit();">
		<html:options collection="executionYears" property="idInternal"
			labelProperty="year" />
	</html:select>

	<p />
	
	-->
	
	<logic:present name="degreeCurricularPlans">
		<table>
			<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans">
				<logic:equal name="degreeCurricularPlan" property="infoDegree.tipoCurso.name" value="DEGREE" >
					<tr>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.degreeCurricularPlanID" property="degreeCurricularPlanID" idName="degreeCurricularPlan" value="idInternal" />
						</td>
						<td><bean:write name="degreeCurricularPlan"
							property="name" /></td>
						<td><bean:write name="degreeCurricularPlan"
							property="infoDegree.nome" /></td>
					</tr>
				</logic:equal>
			</logic:iterate>
		</table>
		<p/>
		<html:submit>
			<bean:message key="button.inquiries.choose" bundle="INQUIRIES_RESOURCES"/>
		</html:submit>
	</logic:present>
</html:form>
