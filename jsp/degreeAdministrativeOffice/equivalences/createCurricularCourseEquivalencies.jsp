<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.equivalencies.curricular.course"/></h2>

<html:form action="/curricularCourseEquivalencies">
	<html:hidden property="method" value="prepareCreate"/>
	<html:hidden property="page" value="0"/>
	<bean:define id="degreeID" name="curricularCourseEquivalenciesForm" property="degreeID" type="java.lang.String"/>
	<html:hidden property="degreeID" value="<%= degreeID %>"/>
	<bean:define id="degreeCurricularPlanID" name="curricularCourseEquivalenciesForm" property="degreeCurricularPlanID" type="java.lang.String"/>
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID %>"/>

	<br />
	<br />

	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.choose.degree"/>
			</td>
			<td class="listClasses">
				<html:select property="degreeID" disabled="true">
					<html:option value=""/>
					<html:options collection="infoDegrees" labelProperty="nome" property="idInternal"/>
				</html:select>
			</td>
		</tr>
		<logic:present name="infoDegreeCurricularPlans">
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.choose.degree.curricular.plan"/>
				</td>
				<td class="listClasses">
					<html:select property="degreeCurricularPlanID" disabled="true">
						<html:option value=""/>
						<html:options collection="infoDegreeCurricularPlans" labelProperty="name" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
		<logic:present name="infoDegreeCurricularPlans">
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.choose.curricular.course"/>
				</td>
				<td class="listClasses">
					<html:select property="curricularCourseID">
						<html:option value=""/>
						<html:options collection="infoCurricularCourses" labelProperty="name" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.choose.degree"/>
			</td>
			<td class="listClasses">
				<html:select property="oldDegreeID" onchange="this.form.submit();">
					<html:option value=""/>
					<html:options collection="infoDegrees" labelProperty="nome" property="idInternal"/>
				</html:select>
			</td>
		</tr>
	</table>
</html:form>
