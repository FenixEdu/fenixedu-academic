<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<%@ page language="java" %>
<%@page import="org.fenixedu.academic.domain.degree.DegreeType"%>
<%@page import="org.fenixedu.academic.domain.EvaluationSeason"%>
<%@page import="org.fenixedu.academic.domain.EvaluationConfiguration"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<html:xhtml/>

<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />

<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

<bean:define id="registration" name="registration" type="org.fenixedu.academic.domain.student.Registration"/>

<academic:allowed operation="VIEW_FULL_STUDENT_CURRICULUM" permission="ACADEMIC_OFFICE_REGISTRATION_ACCESS" program="<%= registration.getDegree() %>">
	<p class="printhidden">
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="externalId">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>
</academic:allowed>

<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<%-- Foto --%>
<div style="float: right;">
	<img class="img-thumbnail" src="${pageContext.request.contextPath}/user/photo/${registration.student.person.username}?s=100" width="100" height="100" />
</div>

<%-- Person and Student short info --%>
<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<div class="clearfix">
<%-- Registration Details --%>
<logic:notPresent name="registration" property="ingressionType">
	<h3 class="separator2 mbottom1 fwnormal"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<div class="col-sm-6">
	<fr:view name="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thnowrap_print thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",,,,,,"/>
		</fr:layout>
	</fr:view>
	</div>
</logic:notPresent>
<logic:present name="registration" property="ingressionType">
	<h3 class="separator2 mbottom1 fwnormal"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<div class="col-sm-6">
		<fr:view name="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thnowrap_print thright thlight mtop0"/>
				<fr:property name="rowClasses" value=",,,,,,"/>
			</fr:layout>
		</fr:view>
	</div>
</logic:present>

<%-- Registration Average and Curricular Year calculations --%>

	<p class="mtop1 mbottom1 hidden-print">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<bean:define id="url" value="<%="/registration.do?method=prepareViewRegistrationCurriculum&amp;registrationID=" + registration.getExternalId()%>"/>
		<logic:present name="degreeCurricularPlanID">
				<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>
				<bean:define id="url" value="<%= url + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID%>"/>
		</logic:present>

		<html:link target="_blank" page="<%=url%>">
			<bean:message key="link.registration.viewCurriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
		<br />
	</p>
</div>

<%-- Choose Student Curricular Plan form --%>
<html:form action="<%="/viewStudentCurriculum.do?method=prepare&registrationOID=" + registration.getExternalId()%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
	</logic:present>
	
	<h3 class="separator2 mbottom1 mtop2 printhidden fwnormal"><bean:message key="label.visualize" bundle="STUDENT_RESOURCES" /></h3>
	<table class="tstyle5 thnowrap_print thright thlight mtop025">
		<tr>
			<th><bean:message key="label.studentCurricularPlan.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="studentCPID" onchange='this.form.submit();'> 
					<html:options collection="scpsLabelValueBeanList" property="value" labelProperty="label" />
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.view" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<e:labelValues id="viewTypes" enumeration="org.fenixedu.academic.ui.renderers.student.curriculum.StudentCurricularPlanRenderer$ViewType" bundle="ENUMERATION_RESOURCES" />
				<html:select property="viewType" altKey="select.viewType" bundle="HTMLALT_RESOURCES" onchange="this.form.submit();">
					<html:options collection="viewTypes" labelProperty="label" property="value"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.enrollmentsFilter.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<e:labelValues id="enrolmentStateTypes" enumeration="org.fenixedu.academic.ui.renderers.student.curriculum.StudentCurricularPlanRenderer$EnrolmentStateFilterType" bundle="ENUMERATION_RESOURCES" />
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.enrolmentStateType" property="select" onchange='this.form.submit();' >
					<html:options collection="enrolmentStateTypes" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message key="organize.by" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<e:labelValues id="organizationTypes" enumeration="org.fenixedu.academic.ui.renderers.student.curriculum.StudentCurricularPlanRenderer$OrganizationType" bundle="ENUMERATION_RESOURCES" />
				<logic:iterate id="organizationType" name="organizationTypes">
					<bean:define id="label" name="organizationType" property="label" />
					<bean:define id="value" name="organizationType" property="value" />
					<html:radio property="organizedBy" altKey="radio.organizedBy" bundle="HTMLALT_RESOURCES" onclick="this.form.submit();" value="<%=value.toString()%>"/><bean:write name="label"/>
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.detailed" bundle="STUDENT_RESOURCES" />:</th>
			<td>
				<html:radio property="detailed" altKey="radio.detailed" bundle="HTMLALT_RESOURCES" onclick="this.form.submit();" value="<%=Boolean.TRUE.toString()%>"/><bean:message  key="label.yes" bundle="STUDENT_RESOURCES"/>
				<html:radio property="detailed" altKey="radio.detailed" bundle="HTMLALT_RESOURCES" onclick="this.form.submit();" value="<%=Boolean.FALSE.toString()%>"/><bean:message  key="label.no" bundle="STUDENT_RESOURCES"/>
			</td>
		</tr>
	</table>


<%-- Show Student Curricular Plans --%>
<logic:empty name="selectedStudentCurricularPlans">
	<p>
		<span class="warning0">
			<bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/>
		</span>
	</p>
</logic:empty>

<logic:notEmpty name="selectedStudentCurricularPlans">
		<bean:define id="organizedBy" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="organizedBy" type="java.lang.String" />
		<bean:define id="enrolmentStateFilterType" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="select" type="java.lang.String" />
		<bean:define id="detailed" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="detailed" type="java.lang.Boolean" />
		<bean:define id="viewType" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="viewType" type="java.lang.String" />
			
	<logic:iterate id="studentCurricularPlan" name="selectedStudentCurricularPlans" indexId="index">
		
		<logic:greaterThan name="index" value="0">
			<div class="mvert3"></div>
		</logic:greaterThan>

		<bean:define id="dateFormated">
			<dt:format pattern="dd.MM.yyyy">
				<bean:write name="studentCurricularPlan" property="startDate.time"/>
			</dt:format>
		</bean:define>

		<div class="mvert2 mtop0">
			<p class="mvert05">
				<strong>
					<bean:message key="label.curricularplan" bundle="STUDENT_RESOURCES" />: 
				</strong> 
				<bean:write name="studentCurricularPlan" property="presentationName"/>
			</p>
			<logic:present name="studentCurricularPlan" property="branch">
				<p class="mvert05">
					<strong>
						Grupo: 
					</strong> 
					<bean:write name="studentCurricularPlan" property="branch.name"/>
				</p>
			</logic:present>
			<p class="mvert05">
				<strong>
					<bean:message key="label.beginDate" bundle="STUDENT_RESOURCES" />: 
				</strong> 
				<bean:write name="dateFormated"/>
			</p>
		</div>

		<fr:edit name="studentCurricularPlan" nested="true">
			<fr:layout>
					<fr:property name="organizedBy" value="<%=organizedBy.toString()%>" />
					<fr:property name="enrolmentStateFilter" value="<%=enrolmentStateFilterType.toString()%>" />
					<fr:property name="viewType" value="<%=viewType.toString()%>" />
					<fr:property name="detailed" value="<%=detailed.toString()%>" />
			</fr:layout>
		</fr:edit>

	</logic:iterate>

<div class="print_fsize08">
	<p class="mtop2 mbottom0"><strong><bean:message key="label.legend" bundle="STUDENT_RESOURCES"/></strong></p>
	<div style="width: 350px; float: left;">
		<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.minCredits" bundle="APPLICATION_RESOURCES"/></em></p>
		<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.creditsConcluded" bundle="APPLICATION_RESOURCES"/></em></p>			
		<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.approvedCredits" bundle="APPLICATION_RESOURCES"/></em></p>
		<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.maxCredits" bundle="APPLICATION_RESOURCES"/></em></p>
		<% for (EvaluationSeason evaluationSeason : EvaluationConfiguration.getInstance().getEvaluationSeasonSet()) { %>
		    <p class="mvert05"><em><%= evaluationSeason.getAcronym().getContent() %>: <%= evaluationSeason.getName().getContent() %></em></p>
		<% } %>
	</div>
</div>


<div class="cboth"></div>



<link href="../javaScript/sviz/sviz.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="../javaScript/sviz/d3.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/qtip.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/i18next.min.js"></script>
<script type="text/javascript" src="../javaScript/sviz/sviz.min.js"></script>
<div id="graph" style="margin-top: 20px; margin-bottom: 10px"></div>

<script type="text/javascript">
	var data = <bean:write name="registrationApprovalRateJSON" filter="false" />;
	SViz.init({ lang: "<%= I18N.getLocale().getLanguage() %>", localesBasePath: "../javaScript/sviz" });
	var chart = SViz.loadViz("showApprovalRate", data, "#graph", {classic:true, width:650, blockWidth:50, blockPadding:7, barWidth:0.85, margin:{left:0}, titleclass:'h4'});
</script>

</logic:notEmpty>
</html:form>
