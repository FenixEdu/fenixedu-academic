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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="org.apache.struts.action.ActionMessages" %>
<html:xhtml />

	<h2><bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses" /></h2>


	<bean:define id="periodSemester" name="bolonhaStudentEnrollmentBean" property="executionPeriod.semester" />
	<bean:define id="executionYearName" name="bolonhaStudentEnrollmentBean" property="executionPeriod.executionYear.year" />


	<p class="mtop15 mbottom025">
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.executionPeriod"/>:</strong> <bean:message bundle="STUDENT_RESOURCES"  key="label.periodDescription" arg0="<%=periodSemester.toString()%>" arg1="<%=executionYearName.toString()%>" />
	</p>
	<p class="mtop0 mbottom15">
		<strong><bean:message bundle="STUDENT_RESOURCES"  key="label.registration.basic"/>:</strong> <bean:write name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan.degreeCurricularPlan.presentationName"/> 
	</p>

	
	
	<ul class="mbottom15">
		<li>
			<html:link action="/bolonhaStudentEnrollment.do?method=showEnrollmentInstructions" styleClass="externallink" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.viewInstructions"/></html:link>
		</li>
		<bean:define id="studentCurricularPlan" name="bolonhaStudentEnrollmentBean" property="studentCurricularPlan" type="org.fenixedu.academic.domain.StudentCurricularPlan" />
		<logic:present name="studentCurricularPlan" property="degree.siteUrl">
			<bean:define id="siteUrl" name="studentCurricularPlan" property="degree.siteUrl" type="java.lang.String" />
			<li>
				<html:link href="<%= siteUrl %>" styleClass="externallink" target="_blank">
					<bean:message bundle="STUDENT_RESOURCES"  key="label.viewDegreeCurricularPlan"/>
				</html:link>
			</li>
		</logic:present>
		<li>
			<html:link action="/viewStudentCurriculum.do?method=prepare" paramId="registrationOID" paramName="studentCurricularPlan" paramProperty="registration.externalId" styleClass="externallink" target="_blank"><bean:message bundle="STUDENT_RESOURCES"  key="label.viewStudentCurricularPlan"/></html:link>
		</li>
		<li>
			<% request.setAttribute("academicSupportAddress", org.fenixedu.academic.domain.Installation.getInstance().getAcademicEmailAddress()); %>
			<html:link href="mailto:${academicSupportAddress}" styleClass="externallink">
				<bean:message key="link.academicSupport" bundle="GLOBAL_RESOURCES"/>
			</html:link>
		</li>
	</ul>

<div class="infoop2 mtop05 mbottom15">
	<p><span><bean:message bundle="APPLICATION_RESOURCES" key="label.warning.coursesAndGroupsSimultaneousEnrolment"/></span></p>
</div>

<div class="infoop2 mtop05 mbottom15">
	<p><strong><bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.selected" />:</strong></p>
	<ul class="mbottom15 selected-modules">
	</ul>
</div>

	<logic:messagesPresent message="true" property="success">
		<p>
		<span class="success0" style="padding: 0.25em;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="success">
				<span><bean:write name="messages" /></span>
			</html:messages>
		</span>
		</p>
	</logic:messagesPresent>
	
	<logic:messagesPresent message="true" property="warning" >
		<div class="warning0" style="padding: 0.5em;">
		<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.warnings.in.enrolment" />:</strong></p>
		<ul class="mvert05">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="warning">
				<li><span><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		</div>
	</logic:messagesPresent>

	<logic:messagesPresent message="true" property="error">
		<div class="error0 mvert1" style="padding: 0.5em;">
			<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.errors.in.enrolment" />:</strong></p>
			<ul class="mvert05">
				<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
					<li><span><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
		</div>
	</logic:messagesPresent>
	
	<fr:form action="/bolonhaStudentEnrollment.do">
		<input type="hidden" name="method" />
		
		<p class="mtop15 mbottom025">
			<bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:
		</p>
		<p class="mtop025 mbottom1">
			<button type="submit" class="btn btn-primary" onclick="submitForm(this);"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></button>
		</p>
		
		<logic:present name="openedEnrolmentPeriodsSemesters">		
			<ul class="nav nav-tabs">
				<logic:iterate id="period" name="openedEnrolmentPeriodsSemesters">				
					<logic:equal name="bolonhaStudentEnrollmentBean" property="executionPeriod.externalId" value="${period.externalId}">
						<li role="presentation" class="active"><a href="#">${period.qualifiedName}</a></li>
					</logic:equal>
					<logic:notEqual name="bolonhaStudentEnrollmentBean" property="executionPeriod.externalId" value="${period.externalId}">
						<li role="presentation">							
							<html:link onclick="return checkState()" action="/bolonhaStudentEnrollment.do?method=prepare&registrationOid=${bolonhaStudentEnrollmentBean.registration.externalId}&executionSemesterID=${period.externalId}">
								${period.qualifiedName}
							</html:link>
						</li>
					</logic:notEqual>
				</logic:iterate>	
			</ul>			
		</logic:present>

		<fr:edit id="bolonhaStudentEnrolments" name="bolonhaStudentEnrollmentBean">
			<fr:layout name="bolonha-student-enrolment">
				<fr:property name="enrolmentClasses" value="se_enrolled smalltxt,se_enrolled smalltxt aright,se_enrolled smalltxt aright,se_enrolled smalltxt aright,se_enrolled aright" />
				<fr:property name="temporaryEnrolmentClasses" value="se_temporary smalltxt,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary smalltxt aright,se_temporary aright" />
				<fr:property name="impossibleEnrolmentClasses" value="se_impossible smalltxt,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible smalltxt aright,se_impossible aright" />
				<fr:property name="curricularCourseToEnrolClasses" value="smalltxt, smalltxt aright, smalltxt aright, aright" />				
				<fr:property name="groupRowClasses" value="se_groups" />

				<fr:property name="encodeGroupRules" value="true" />
				<fr:property name="encodeCurricularRules" value="true" />
			</fr:layout>
		</fr:edit>
		
		
		<p class="mtop15 mbottom05"><bean:message bundle="APPLICATION_RESOURCES"  key="label.saveChanges.message"/>:</p>
		<p class="mtop05 mbottom1">
			<button type="submit" class="btn btn-primary" onclick="submitForm(this);"><bean:message bundle="APPLICATION_RESOURCES"  key="label.save"/></button>
		</p>
	
	</fr:form>



<p class="mtop15">
<em><bean:message bundle="STUDENT_RESOURCES"  key="message.enrollment.terminated"/> <html:link action="/viewStudentCurriculum.do?method=prepare" paramId="registrationOID" paramName="studentCurricularPlan" paramProperty="registration.externalId"><bean:message bundle="STUDENT_RESOURCES"  key="message.student.curriculum"/></html:link>.</em> <br/>
<em><bean:message bundle="STUDENT_RESOURCES"  key="message.enrollment.terminated.shifts"/> <html:link page="/studentShiftEnrollmentManager.do?method=prepare" titleKey="link.title.shift.enrolment"><bean:message key="link.shift.enrolment"/></html:link>.</em>
</p>


<p class="mtop2 mbottom0"><em><bean:message bundle="APPLICATION_RESOURCES"  key="label.legend"/>:</em></p>

<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.minCredits" bundle="APPLICATION_RESOURCES"/></em></p>
<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.creditsConcluded" bundle="APPLICATION_RESOURCES"/></em></p>
<p class="mvert05"><em><bean:message  key="label.curriculum.credits.legend.maxCredits" bundle="APPLICATION_RESOURCES"/></em></p>

<table class="mtop0">
<tr>
	<td><div style="width: 10px; height: 10px; border: 1px solid #84b181; background: #eff9ee; float:left;"></div></td>
	<td><bean:message bundle="APPLICATION_RESOURCES"  key="label.confirmedEnrollments"/><span class="color888"> (<bean:message bundle="APPLICATION_RESOURCES"  key="label.greenLines"/>)</span></td>
</tr>
<tr>
	<td><div style="width: 10px; height: 10px; border: 1px solid #b9b983; background: #fafce6; float:left;"></div></td>
	<td><bean:message bundle="APPLICATION_RESOURCES"  key="label.temporaryEnrollments"/><span class="color888"> (<bean:message bundle="APPLICATION_RESOURCES"  key="label.yellowLines"/>)</span></td>
</tr>
<tr>
	<td><div style="width: 10px; height: 10px; border: 1px solid #be5a39; background: #ffe9e2; float:left;"></div></td>
	<td><bean:message bundle="APPLICATION_RESOURCES"  key="label.impossibleEnrollments"/><span class="color888"> (<bean:message bundle="APPLICATION_RESOURCES"  key="label.redLines"/>)</span></td>
</tr>
</table>

<script>
function submitForm(btn) {
	btn.form.method.value = 'enrolInDegreeModules';
	$(btn).addClass('disabled');
	$(btn).html('${portal.message('resources.ApplicationResources', 'label.saving')}');
}

function getSelectedModules() {
    return $.makeArray($('.module-enrol-checkbox:checked, .enrolment-checkbox:checked').map(function(i, obj) {

        return '<li>' + $(obj).data('fullpath') + '</li>';
    })).join("") || '${portal.message('resources.StudentResources', 'label.enrollment.courses.selected.empty')}';
}

(function () {
	$('table').removeClass('table');

    $('.module-enrol-checkbox:checked').each(function(i, obj) {
        var cells = $(obj).parent().siblings().andSelf();
		cells.addClass('se_temporary');
    });

    $('.selected-modules').html(getSelectedModules());

    $('.module-enrol-checkbox, .enrolment-checkbox').on('click', function(){
        $('.success0').remove();

        var cells = $(this).parent().siblings().andSelf();
        if ($(this).is(":checked")) {
            cells.addClass('se_temporary');
		} else {
            cells.removeClass('se_enrolled');
            cells.removeClass('se_temporary');
		}

        $('.selected-modules').html(getSelectedModules());
    });

})();

function checkState(){
	if($.grep($("input[type=checkbox]"), function (item) { return $(item).is("[checked]") != item.checked; }).length > 0){
		result = window.confirm("<bean:message bundle="STUDENT_RESOURCES"  key="label.changeSemesterWithoutSave"/>");
		if(!result){
			return false;
		}
	}
	return true;
}


</script>