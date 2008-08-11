<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page language="java" %>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer"%>
<html:xhtml/>

<!-- showStudentCurriculum.jsp -->

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.tutorship.student.curriculum" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<fr:form action="/studentTutorship.do?method=showStudentCurriculum">
	<fr:edit id="filterForm" name="tutorateBean" schema="tutorship.student.number">
		<fr:edit id="tutorateBean" name="tutorateBean" visible="false" />
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/studentTutorship.do?method=prepareStudentCurriculum" />
	</fr:edit>
	<html:submit>
		<bean:message key="label.submit" bundle="PEDAGOGICAL_COUNCIL" />
	</html:submit>
</fr:form>


<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
<br/><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>


<logic:present name="registration">

	<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>
	
	<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>
	
	<!-- Foto -->
	<div style="float: right;" class="printhidden">
		<bean:define id="personID" name="registration" property="student.person.idInternal"/>
		<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<!-- Person and Student short info -->
	<p class="mvert2">
		<span class="showpersonid">
			<fr:view name="registration" property="student.person" schema="tutorship.tutorate.student">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
					<fr:property name="rowClasses" value=",,,,,,"/>
			    </fr:layout>
			</fr:view>
		</span>
	</p>
	
	
	<!-- Registration Details -->
	<logic:notPresent name="registration" property="ingression">
		<h3 class="separator2 mbottom1 fwnormal"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
				<fr:property name="rowClasses" value=",,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>
	<logic:present name="registration" property="ingression">
		<h3 class="separator2 mbottom1 fwnormal"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
				<fr:property name="rowClasses" value=",,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	
	<!-- Choose Student Curricular Plan form -->

	<logic:present name="degreeCurricularPlanID">
		<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>
		<fr:edit id="degreeCurricularPlanID" name="degreeCurricularPlanID" visible="false" />
	</logic:present>

	<logic:present name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber" >
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
	</logic:present>
	

	<h3 class="separator2 mbottom1 mtop2 printhidden fwnormal"><bean:message key="label.visualize" bundle="STUDENT_RESOURCES" /></h3>


	<!-- Show Student Curricular Plans -->
	<logic:empty name="registration" property="studentCurricularPlans">
		<p>
			<span class="warning0">
				<bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/>
			</span>
		</p>
	</logic:empty>


	<logic:iterate id="studentCurricularPlan" name="registration" property="studentCurricularPlans" indexId="index" >
		
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
				<bean:message bundle="ENUMERATION_RESOURCES" name="studentCurricularPlan" property="degreeType.name"/>
				<bean:message bundle="APPLICATION_RESOURCES" key="label.in"/> 
				<bean:write name="studentCurricularPlan" property="degree.name"/>,
				<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.name"/>
				<logic:present name="studentCurricularPlan" property="specialization">
					- <bean:message name="studentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
				</logic:present>
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
					<fr:property name="organizedBy" value="<%= StudentCurricularPlanRenderer.OrganizationType.GROUPS.name() %>" />
					<fr:property name="enrolmentStateFilter" value="<%= StudentCurricularPlanRenderer.EnrolmentStateFilterType.ALL.name() %>" />
					<fr:property name="viewType" value="<%= StudentCurricularPlanRenderer.ViewType.ALL.name() %>" />
					<fr:property name="detailed" value="<%= Boolean.TRUE.toString() %>" />
			</fr:layout>
		</fr:edit>

	</logic:iterate>

	<div class="print_fsize08">
		<p class="mtop2 mbottom0"><strong><bean:message key="label.legend" bundle="STUDENT_RESOURCES"/></strong></p>
		<div style="width: 350px; float: left;">
			<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.minCredits" bundle="APPLICATION_RESOURCES"/></em></p>
			<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.creditsConcluded" bundle="APPLICATION_RESOURCES"/></em></p>	
			<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
				<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.approvedCredits" bundle="APPLICATION_RESOURCES"/></em></p>
			</logic:present>
			<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.maxCredits" bundle="APPLICATION_RESOURCES"/></em></p>
		    <e:labelValues id="enrolmentEvaluationTypes" enumeration="net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType" />
			<logic:iterate id="enrolmentEvaluationType" name="enrolmentEvaluationTypes" type="LabelValueBean">
				<p class="mvert05"><em><bean:message key="<%="EnrolmentEvaluationType." + enrolmentEvaluationType.getValue() + ".acronym"%>" bundle="ENUMERATION_RESOURCES"/>: <bean:message key="<%="EnrolmentEvaluationType." + enrolmentEvaluationType.getValue()%>" bundle="ENUMERATION_RESOURCES"/></em></p>
			</logic:iterate>
		</div>
	</div>

	<div class="cboth"></div>
	
	<div class="mtop15">
		<bean:define id="graph" type="java.lang.String">
			<%= request.getContextPath() %>/student/viewCurriculumGraph.do?method=createAreaXYChart&registrationOID=<%= registration.getIdInternal() %>
		</bean:define>
		<html:img align="middle" src="<%= graph %>"/>
	</div>

</logic:present>