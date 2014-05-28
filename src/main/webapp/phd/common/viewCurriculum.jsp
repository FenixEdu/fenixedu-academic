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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.PhdCurriculumFilterOptions"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.ViewType"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.EnrolmentStateFilterType"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType"%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<bean:define id="url">/phdIndividualProgramProcess.do?method=changeViewCurriculumFilterOptions&processId=<bean:write name="process" property="externalId" /></bean:define>

<fr:form action="<%= url %>">

<strong><bean:message key="label.visualize" bundle="STUDENT_RESOURCES" /></strong>
<fr:edit id="curriculumFilter" name="curriculumFilter">

	<fr:schema bundle="PHD_RESOURCES" type="<%= PhdCurriculumFilterOptions.class.getName() %>">

		<fr:slot name="selectedStudentCurricularPlan" layout="menu-select-postback">
			<fr:property name="providerClass" value="<%= PhdCurriculumFilterOptions.PhdStudentCurricularPlansFilterProvider.class.getName()  %>" />
			<fr:property name="format" value="${presentationName}" />
			
			<fr:property name="defaultText" value="Todos os planos curriculares" />
<!--			<fr:property name="bundle" value="PHD_RESOURCES" />-->
			<fr:property name="key" value="false" />
		</fr:slot>

		<fr:slot name="viewType" layout="menu-select-postback">
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.ViewTypeProvider" />
			<fr:property name="nullOptionHidden" value="true" />
		</fr:slot>

		<fr:slot name="enrolmentStateType" layout="menu-select-postback">
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.EnrolmentStateFilterTypeProvider" />
			<fr:property name="nullOptionHidden" value="true" />
		</fr:slot>

		<fr:slot name="organizationType" layout="menu-select-postback">
			<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.OrganizationTypeProvider" />
			<fr:property name="nullOptionHidden" value="true" />
		</fr:slot>

		<fr:slot name="detailed" layout="radio-postback">
			<fr:property name="classes" value="liinline nobullet"/>
		</fr:slot>

	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
	</fr:layout>
	
	<fr:destination name="postback" path="<%= url %>" />
</fr:edit>

</fr:form>

<bean:define id="organizedBy"><bean:write name="curriculumFilter" property="organizationType" /></bean:define>
<bean:define id="enrolmentStateType"><bean:write name="curriculumFilter" property="enrolmentStateType" /></bean:define>
<bean:define id="viewType"><bean:write name="curriculumFilter" property="viewType" /></bean:define>
<bean:define id="detailed"><bean:write name="curriculumFilter" property="detailed" /></bean:define>

<logic:iterate id="studentCurricularPlan" name="curriculumFilter" property="studentCurricularPlans">

	<logic:greaterThan name="index" value="0">
		<div class="mvert3"></div>
	</logic:greaterThan>

	<div class="mvert2 mtop0">
	<p class="mvert05">
		<strong><bean:message key="label.curricularplan" bundle="STUDENT_RESOURCES" />: </strong> 
		<bean:write name="studentCurricularPlan" property="presentationName"/>
	</p>
	<p class="mvert05">
		<strong><bean:message key="label.beginDate" bundle="STUDENT_RESOURCES" />: </strong> 
		<fr:view name="studentCurricularPlan" property="startDate" />
	</p>
	</div>

	<fr:edit id="curriculum" name="studentCurricularPlan">
		<fr:layout>
			<fr:property name="organizedBy" value="<%= organizedBy.toString() %>" />
			<fr:property name="enrolmentStateFilter" value="<%= enrolmentStateType.toString() %>" />
			<fr:property name="viewType" value="<%= viewType.toString()%>" />
			<fr:property name="detailed" value="<%= detailed.toString() %>" />
		</fr:layout>
	</fr:edit>

</logic:iterate>

<jsp:include page="/phd/academicAdminOffice/viewCurriculumFooter.jsp" />
