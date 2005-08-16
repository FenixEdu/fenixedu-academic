<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="title.equivalencies.curricular.course"/></h2>

<html:form action="/curricularCourseEquivalencies">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="0"/>

	<br />
	<br />

	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.choose.degree"/>
			</td>
			<td class="listClasses">
				<html:select property="degreeID" onchange="this.form.submit();">
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
					<html:select property="degreeCurricularPlanID" onchange="this.form.submit();">
						<html:option value=""/>
						<html:options collection="infoDegreeCurricularPlans" labelProperty="name" property="idInternal"/>
					</html:select>
				</td>
			</tr>
		</logic:present>
	</table>

</html:form>

<logic:present name="infoCurricularCourseEquivalences">

	<br/>
	<br/>

	<html:form action="/curricularCourseEquivalencies">
		<html:hidden property="method" value="prepareCreate"/>
		<html:hidden property="page" value="0"/>
		<bean:define id="degreeID" name="curricularCourseEquivalenciesForm" property="degreeID" type="java.lang.String"/>
		<html:hidden property="degreeID" value="<%= degreeID %>"/>
		<bean:define id="degreeCurricularPlanID" name="curricularCourseEquivalenciesForm" property="degreeCurricularPlanID" type="java.lang.String"/>
		<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID %>"/>

		<html:submit styleClass="inputbutton">
			<bean:message key="link.curricular.course.equivalence.create"/>
		</html:submit>
	</html:form>

	<br/>
	<br/>

	<table>
		<tr>
			<td colspan="3" class="listClasses-header">
				<bean:message key="label.old.curricular.course"/>
			</td>
			<td colspan="2" class="listClasses-header">
				<bean:message key="label.equivalente.curricular.course"/>
			</td>
		</tr>
		<logic:iterate id="infoCurricularCourseEquivalence" name="infoCurricularCourseEquivalences">
			<tr>
				<td class="listClasses">
					<bean:write name="infoCurricularCourseEquivalence" property="infoOldCurricularCourse.infoDegreeCurricularPlan.name"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoCurricularCourseEquivalence" property="infoOldCurricularCourse.code"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoCurricularCourseEquivalence" property="infoOldCurricularCourse.name"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoCurricularCourseEquivalence" property="infoEquivalentCurricularCourse.code"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoCurricularCourseEquivalence" property="infoEquivalentCurricularCourse.name"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>