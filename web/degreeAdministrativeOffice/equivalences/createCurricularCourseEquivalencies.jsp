<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message bundle="DEGREE_ADM_OFFICE" key="title.equivalencies.curricular.course"/></h2>

<html:form action="/curricularCourseEquivalencies">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<bean:define id="degreeID" name="curricularCourseEquivalenciesForm" property="degreeID" type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeID" property="degreeID" value="<%= degreeID %>"/>
	<bean:define id="degreeCurricularPlanID" name="curricularCourseEquivalenciesForm" property="degreeCurricularPlanID" type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID %>"/>

	<br />
	<br />

<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.old.curricular.course"/>
	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.degree.old"/>
			</th>
			<td class="listClasses">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.oldDegreeID" property="oldDegreeID" onchange="this.form.method.value='prepareCreate';this.form.submit();">
					<html:option value=""/>
					<html:options collection="infoDegrees" labelProperty="name" property="idInternal"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<logic:present name="oldInfoDegreeCurricularPlans">
			<tr>
				<th class="listClasses-header">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.degree.curricular.plan.old"/>
				</th>
				<td class="listClasses">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.oldDegreeCurricularPlanID" property="oldDegreeCurricularPlanID" onchange="this.form.method.value='prepareCreate';this.form.submit();">
						<html:option value=""/>
						<html:options collection="oldInfoDegreeCurricularPlans" labelProperty="name" property="idInternal"/>
					</html:select>
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="DEGREE_ADM_OFFICE" key="button.submit"/>
					</html:submit>
				</td>
			</tr>
		</logic:present>
		<logic:present name="oldInfoCurricularCourses">
			<tr>
				<th class="listClasses-header">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.curricular.course.old"/>
				</th>
				<td class="listClasses">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.oldCurricularCourseID" property="oldCurricularCourseID">
						<html:option value=""/>
						<html:options collection="oldInfoCurricularCourses" labelProperty="nameAndCode" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
	</table>

<br />
<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.equivalent.curricular.course"/>
	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.degree"/>
			</th>
			<td class="listClasses">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeID" property="degreeID" disabled="true">
					<html:option value=""/>
					<html:options collection="infoDegrees" labelProperty="name" property="idInternal"/>
				</html:select>
			</td>
		</tr>
		<logic:present name="infoDegreeCurricularPlans">
			<tr>
				<th class="listClasses-header">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.degree.curricular.plan"/>
				</th>
				<td class="listClasses">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeCurricularPlanID" property="degreeCurricularPlanID" disabled="true">
						<html:option value=""/>
						<html:options collection="infoDegreeCurricularPlans" labelProperty="name" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
		<logic:present name="infoDegreeCurricularPlans">
			<tr>
				<th class="listClasses-header">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.curricular.course"/>
				</th>
				<td class="listClasses">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularCourseID" property="curricularCourseID">
						<html:option value=""/>
						<html:options collection="infoCurricularCourses" labelProperty="nameAndCode" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
	</table>

	<br/>
	<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="DEGREE_ADM_OFFICE" key="link.curricular.course.equivalence.create"/>
	</html:submit>
</html:form>
