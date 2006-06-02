<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.equivalencies.curricular.course"/></h2>

<html:form action="/curricularCourseEquivalencies">
	<html:hidden property="method" value="create"/>
	<html:hidden property="page" value="0"/>
	<bean:define id="degreeID" name="curricularCourseEquivalenciesForm" property="degreeID" type="java.lang.String"/>
	<html:hidden property="degreeID" value="<%= degreeID %>"/>
	<bean:define id="degreeCurricularPlanID" name="curricularCourseEquivalenciesForm" property="degreeCurricularPlanID" type="java.lang.String"/>
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID %>"/>

	<br />
	<br />

<bean:message key="label.choose.old.curricular.course"/>
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.choose.degree.old"/>
			</td>
			<td class="listClasses">
				<html:select property="oldDegreeID" onchange="this.form.method.value='prepareCreate';this.form.submit();">
					<html:option value=""/>
					<html:options collection="infoDegrees" labelProperty="name" property="idInternal"/>
				</html:select>
			</td>
		</tr>
		<logic:present name="oldInfoDegreeCurricularPlans">
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.choose.degree.curricular.plan.old"/>
				</td>
				<td class="listClasses">
					<html:select property="oldDegreeCurricularPlanID" onchange="this.form.method.value='prepareCreate';this.form.submit();">
						<html:option value=""/>
						<html:options collection="oldInfoDegreeCurricularPlans" labelProperty="name" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
		<logic:present name="oldInfoCurricularCourses">
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.choose.curricular.course.old"/>
				</td>
				<td class="listClasses">
					<html:select property="oldCurricularCourseID">
						<html:option value=""/>
						<html:options collection="oldInfoCurricularCourses" labelProperty="nameAndCode" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
	</table>

<br />
<bean:message key="label.choose.equivalent.curricular.course"/>
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.choose.degree"/>
			</td>
			<td class="listClasses">
				<html:select property="degreeID" disabled="true">
					<html:option value=""/>
					<html:options collection="infoDegrees" labelProperty="name" property="idInternal"/>
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
						<html:options collection="infoCurricularCourses" labelProperty="nameAndCode" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
	</table>

	<br/>
	<br/>

	<html:submit styleClass="inputbutton">
		<bean:message key="link.curricular.course.equivalence.create"/>
	</html:submit>
</html:form>
