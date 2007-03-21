<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="title.student.marksSheetConsult"/></h2>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>
</p>


<table class="mtop15">

<html:form action="/chooseExecutionYearAndDegreeCurricularPlan">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseDegreeCurricularPlan"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	

		<tr>
			<td>
				<bean:message key="label.curriculumHistoric.chooseExecutionYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/>:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="executionYearID" onchange="this.form.submit()">
					<option value=""><bean:message key="label.curriculumHistoric.chooseExecutionYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/></option>
					<logic:notEmpty name="executionYears">
						<html:options collection="executionYears" property="idInternal" labelProperty="year" />
					</logic:notEmpty>
				</html:select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>

</html:form>

<html:form action="/chooseExecutionYearAndDegreeCurricularPlan">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showActiveCurricularCourseScope"/>
	<bean:define id="executionYearID" name="executionYearDegreeCurricularPlanForm" property="executionYearID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearID" property="executionYearID" value="<%= executionYearID.toString() %>"/>
	<logic:empty name="degreeCurricularPlans">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	</logic:empty>
	<logic:notEmpty name="degreeCurricularPlans">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>


			<tr>
				<td>
					<bean:message key="label.curriculumHistoric.chooseDegreeCurricularPlan" bundle="CURRICULUM_HISTORIC_RESOURCES"/>:
				</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="degreeCurricularPlanID">
					<html:options collection="degreeCurricularPlans" property="value" labelProperty="label" />
				</html:select>
				</td>
			</tr>

	</logic:notEmpty>

	<tr>
		<td>
			<p>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message key="button.continue" bundle="CURRICULUM_HISTORIC_RESOURCES"/></html:submit>
			</p>
		</td>
	</tr>
</html:form>

</table>