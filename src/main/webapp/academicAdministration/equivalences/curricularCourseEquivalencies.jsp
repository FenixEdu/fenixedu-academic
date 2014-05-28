<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

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
					<html:options collection="infoDegrees" labelProperty="presentationName" property="externalId"/>
				</html:select>
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
						<html:options collection="infoDegreeCurricularPlans" labelProperty="name" property="externalId"/>
					</html:select>
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
						<bean:define id="curricularCourseEquivalencyID" name="curricularCourseEquivalence" property="externalId" type="java.lang.String"/>
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