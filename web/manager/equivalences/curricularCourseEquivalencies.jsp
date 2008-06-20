<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message bundle="DEGREE_ADM_OFFICE" key="title.equivalencies.curricular.course"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/curricularCourseEquivalencies">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

	<br />
	<br />

	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.degree"/>
			</th>
			<td class="listClasses">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeID" property="degreeID" onchange="this.form.submit();">
					<html:option value=""/>
					<html:options collection="infoDegrees" labelProperty="name" property="idInternal"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<logic:present name="infoDegreeCurricularPlans">
			<tr>
				<th class="listClasses-header">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="label.choose.degree.curricular.plan"/>
				</th>
				<td class="listClasses">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeCurricularPlanID" property="degreeCurricularPlanID" onchange="this.form.submit();">
						<html:option value=""/>
						<html:options collection="infoDegreeCurricularPlans" labelProperty="name" property="idInternal"/>
					</html:select>
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="DEGREE_ADM_OFFICE" key="button.submit"/>
					</html:submit>
				</td>
			</tr>
		</logic:present>
	</table>

</html:form>

<logic:present name="curricularCourseEquivalences">

	<br/>
	<br/>

	<html:form action="/curricularCourseEquivalencies">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareCreate"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
		<bean:define id="degreeID" name="curricularCourseEquivalenciesForm" property="degreeID" type="java.lang.String"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeID" property="degreeID" value="<%= degreeID %>"/>
		<bean:define id="degreeCurricularPlanID" name="curricularCourseEquivalenciesForm" property="degreeCurricularPlanID" type="java.lang.String"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID %>"/>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="DEGREE_ADM_OFFICE" key="link.curricular.course.equivalence.create"/>
		</html:submit>
	</html:form>

	<br/>
	<br/>

	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="DEGREE_ADM_OFFICE" key="label.old.curricular.course"/>
			</th>
			<th colspan="2" class="listClasses-header">
				<bean:message bundle="DEGREE_ADM_OFFICE" key="label.equivalente.curricular.course"/>
			</th>
			<th class="listClasses-header">
			</th>
		</tr>
		<logic:iterate id="curricularCourseEquivalence" name="curricularCourseEquivalences">
			<tr>
				<td class="listClasses">
					<table align="center" width="100%">
						<logic:iterate id="oldCourse" name="curricularCourseEquivalence" property="oldCurricularCourses">
							<tr>
								<td class="listClasses">
									<bean:write name="oldCourse" property="degreeCurricularPlan.name"/>
								</td>								
								<td class="listClasses">
									<bean:write name="oldCourse" property="code"/>
								</td>								
								<td class="listClasses">
									<bean:write name="oldCourse" property="name"/>
								</td>								
							</tr>
						</logic:iterate>
					</table>
				</td>
				<td class="listClasses">
					<bean:write name="curricularCourseEquivalence" property="equivalentCurricularCourse.code"/>
				</td>
				<td class="listClasses">
					<bean:write name="curricularCourseEquivalence" property="equivalentCurricularCourse.name"/>
				</td>
				<td class="listClasses">
					<html:form action="/curricularCourseEquivalencies">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="delete"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
						<bean:define id="degreeID" name="curricularCourseEquivalenciesForm" property="degreeID" type="java.lang.String"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeID" property="degreeID" value="<%= degreeID %>"/>
						<bean:define id="degreeCurricularPlanID" name="curricularCourseEquivalenciesForm" property="degreeCurricularPlanID" type="java.lang.String"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID %>"/>
						<bean:define id="curricularCourseEquivalencyID" name="curricularCourseEquivalence" property="idInternal" type="java.lang.Integer"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseEquivalencyID" property="curricularCourseEquivalencyID" value="<%= curricularCourseEquivalencyID.toString() %>"/>

						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message bundle="DEGREE_ADM_OFFICE" key="button.delete"/>
						</html:submit>
					</html:form>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>