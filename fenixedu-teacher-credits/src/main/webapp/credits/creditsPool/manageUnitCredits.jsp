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
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<style>
<!--
.yellowStuff {
	background-color: #fce94f;
}
.redStuff {
	background-color: #ff9999;
}
.badValue {
	color: #DF0101;
}
#notification-bar {
    width:35%;
    position:fixed;
    left:425px;
    border-bottom: 1px solid black;
}

.badInput { 
	background-color: #F6CECE;
    border-color: #F6CECE;
}
-->
</style>

<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES"><span class="error"><bean:write name="message" filter="false" /></span></html:messages>
<fr:hasMessages><p><span class="error0"><fr:messages><fr:message/></fr:messages></span></p></fr:hasMessages>
	
<logic:present name="departmentCreditsBean">
	<div class="thisPage">
	<fr:form id="f1" action="/creditsPool.do?method=viewDepartmentExecutionCourses">
		<fr:edit id="departmentCreditsBean" name="departmentCreditsBean">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.DepartmentCreditsBean">
				<fr:slot name="department" key="label.department" layout="menu-select" required="true">
					<fr:property name="from" value="availableDepartments"/>
					<fr:property name="format" value="${name}"/>
				</fr:slot>
				<fr:slot name="executionYear" key="label.executionYear" layout="menu-select" required="true">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionYearsProvider" />
					<fr:property name="format" value="${year}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop15" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="cancel" path="/creditsPool.do?method=exportDepartmentExecutionCourses" />
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit>
		<html:cancel><bean:message key="label.export" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</fr:form>
	<logic:present name="departmentCreditsPoolBean">
		<logic:present name="departmentCreditsPoolBean" property="departmentCreditsPool">
			<h3><bean:message key="label.departmentCreditsPool" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
			<div id="notification-bar" style="opacity: 0.75; height: 100px; padding-top: 10px; padding-left: 20px; display: none;" class="yellowStuff">
				<span id="notification-bar-warning"><bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.department.credits.pool.change.warning"/></span>
				<span id="notification-bar-error"><bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.department.credits.pool.change.error"/></span>
				<ul>
					<li>
						<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.changedDepartmentCreditsPool"/>:
						<span id="creditsPool"><bean:write name="departmentCreditsPoolBean" property="departmentCreditsPool.creditsPool"/></span>
					</li>
					<li>
						<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.assignedCredits"/>:
						<span id="assignedCredits"><bean:write name="departmentCreditsPoolBean" property="assignedCredits"/></span> 
					</li>
					<li>
						<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.availableCredits"/>:
						<span id="availableCredits"><bean:write name="departmentCreditsPoolBean" property="availableCredits"/></span> 
					</li>
				</ul>
				
			</div>
			<fr:view name="departmentCreditsPoolBean">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.DepartmentCreditsPoolBean">
					<fr:slot name="departmentCreditsPool.originalCreditsPool" key="label.departmentCreditsPool"/>
					<fr:slot name="departmentCreditsPool.creditsPool" key="label.changedDepartmentCreditsPool"/>
					<fr:slot name="assignedCredits" key="label.assignedCredits"/>
					<fr:slot name="availableCredits" key="label.availableCredits"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
				</fr:layout>
			</fr:view>
		
			<h3><bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.courses.shared"/></h3>
			<logic:notEmpty name="departmentCreditsPoolBean" property="departmentSharedExecutionCourses">
				<bean:define id="canEditSharedUnitCredits" name="departmentCreditsPoolBean" property="canEditSharedUnitCredits" type="java.lang.Boolean"/>
				<fr:form id="sharedUnitCreditsForm" action="/creditsPool.do?method=editUnitCredits">
					<fr:edit id="departmentCreditsPoolBean" name="departmentCreditsPoolBean" visible="false"/>
					<fr:edit id="departmentCreditsPoolBean2" name="departmentCreditsPoolBean" property="departmentSharedExecutionCourses" >
						<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.DepartmentCreditsPoolBean$DepartmentExecutionCourse">
							<fr:slot name="executionCourse.name" key="label.course" readOnly="true"/>
							<fr:slot name="executionCourse.degreePresentationString" key="label.degrees" readOnly="true"/>
							<fr:slot name="executionCourse.executionPeriod.semester" key="label.execution-period" readOnly="true"/>
							<fr:slot name="executionCourse.effortRate" key="label.effortRate" readOnly="true"/>
							<fr:slot name="departmentEffectiveLoad" key="label.departmentEffectiveLoad" readOnly="true"/>
							<fr:slot name="totalEffectiveLoad" key="label.totalEffectiveLoad" readOnly="true"/>
							<fr:slot name="unitCreditValue" key="label.unitCredit" readOnly="<%=!canEditSharedUnitCredits%>" >
								<fr:property name="maxLength" value="4"/>
								<fr:property name="size" value="2"/>
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator"/>
							</fr:slot>
							<fr:slot name="unitCreditJustification" key="label.justification" readOnly="<%=!canEditSharedUnitCredits%>"/>
						</fr:schema>
						<fr:layout name="tabular-editable">
							<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
							<fr:property name="columnClasses" value=",,,,,,,,tdclear tderror1"/>
							<fr:property name="requiredMarkShown" value="true" />
						</fr:layout>
						<fr:destination name="invalid" path="/creditsPool.do?method=postBackUnitCredits" />
					</fr:edit>
					<logic:equal name="canEditSharedUnitCredits" value="true">
						<html:submit styleId="sb1" bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit>
					</logic:equal>
				</fr:form>
			</logic:notEmpty>
			<logic:empty name="departmentCreditsPoolBean" property="departmentSharedExecutionCourses">
				<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="message.noRecordsFound"/>
			</logic:empty>
			<h3><bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.courses.shared.other.departments"/></h3>
			<logic:notEmpty name="departmentCreditsPoolBean" property="otherDepartmentSharedExecutionCourses">
				<fr:view name="departmentCreditsPoolBean" property="otherDepartmentSharedExecutionCourses">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.DepartmentCreditsPoolBean$DepartmentExecutionCourse">
						<fr:slot name="executionCourse.departmentNames" key="label.department" readOnly="true" />
						<fr:slot name="executionCourse.name" key="label.course" readOnly="true"/>
						<fr:slot name="executionCourse.degreePresentationString" key="label.degrees" readOnly="true"/>
						<fr:slot name="executionCourse.executionPeriod.name" key="label.execution-period" readOnly="true"/>
						<fr:slot name="executionCourse.effortRate" key="label.effortRate" readOnly="true"/>
						<fr:slot name="departmentEffectiveLoad" key="label.departmentEffectiveLoad" readOnly="true"/>
						<fr:slot name="totalEffectiveLoad" key="label.totalEffectiveLoad" readOnly="true"/>
						<fr:slot name="unitCreditValue" key="label.unitCredit" readOnly="true"/>
					</fr:schema>
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="departmentCreditsPoolBean" property="otherDepartmentSharedExecutionCourses">
				<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="message.noRecordsFound"/>
			</logic:empty>
			
			<h3><bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.courses.not.shared"/></h3>
			<logic:notEmpty name="departmentCreditsPoolBean" property="departmentExecutionCourses">
				<bean:define id="canEditUnitCredits" name="departmentCreditsPoolBean" property="canEditUnitCredits" type="java.lang.Boolean"/>
				<fr:form id="unitCreditsForm" action="/creditsPool.do?method=editUnitCredits">
					<fr:edit id="departmentCreditsPoolBean" name="departmentCreditsPoolBean" visible="false"/>
					<fr:edit id="departmentCreditsPoolBean3" name="departmentCreditsPoolBean" property="departmentExecutionCourses">
						<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.DepartmentCreditsPoolBean$DepartmentExecutionCourse">
							<fr:slot name="executionCourse.name" key="label.course" readOnly="true"/>
							<fr:slot name="executionCourse.degreePresentationString" key="label.degrees" readOnly="true"/>
							<fr:slot name="executionCourse.executionPeriod.semester" key="label.execution-period" readOnly="true"/>
							<fr:slot name="executionCourse.effortRate" key="label.effortRate" readOnly="true"/>
							<fr:slot name="departmentEffectiveLoad" key="label.departmentEffectiveLoad" readOnly="true"/>
							<fr:slot name="totalEffectiveLoad" key="label.totalEffectiveLoad" readOnly="true"/>
							<fr:slot name="unitCreditValue" key="label.unitCredit" readOnly="<%=!canEditUnitCredits%>">
								<fr:property name="maxLength" value="4"/>
								<fr:property name="size" value="2"/>
							</fr:slot>
							<fr:slot name="unitCreditJustification" key="label.justification" readOnly="<%=!canEditUnitCredits%>"/>
						</fr:schema>
						<fr:layout name="tabular-editable">
							<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
						</fr:layout>
					</fr:edit>
					<logic:equal name="canEditUnitCredits" value="true">
						<html:submit styleId="sb2" bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit>
					</logic:equal>
				</fr:form>
			</logic:notEmpty>
			<logic:empty name="departmentCreditsPoolBean" property="departmentExecutionCourses">
				<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="message.noRecordsFound"/>
			</logic:empty>
		</logic:present>
		<logic:notPresent name="departmentCreditsPoolBean" property="departmentCreditsPool">
			<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.department.credits.pool.not.defined"/>
		</logic:notPresent>
	</logic:present>
	</div>
</logic:present>

<script type="text/javascript" language="javascript">
	$(document).ready(function(){
		var creditsPool = parseFloat($("#creditsPool").html());
		var otherAssignedCredits = parseFloat($("#assignedCredits").html());

		$( '.thisPage input[name$="unitCreditValue"]' ).each(function() {
			var inputVal = $(this).val();
			if (inputVal && !isNaN(inputVal)){
				var cleTotal = parseFloat($(this).parent().parent().parent().children().eq(4).html());
				otherAssignedCredits = otherAssignedCredits - (cleTotal * parseFloat(inputVal));
			}
			var bk = parseFloat($(this).parent().parent().parent().children().eq(3).html());
			if(!bk || isNaN(bk)){
				 $(this).prop('readonly', true);
				 $(this).parent().parent().parent().children().eq(7).children().children().eq(0).prop('readonly', true);
			}
		});

		$(':input[name$="unitCreditValue"]').change(function(){
			$("#notification-bar").toggle(true);
			var assignedCredits = otherAssignedCredits;
			$( '.thisPage input[name$="unitCreditValue"]' ).each(function() {
				var inputVal = $(this).val();
				if (inputVal && !isNaN(inputVal)){
					var cleTotal = parseFloat($(this).parent().parent().parent().children().eq(4).html());
					assignedCredits += cleTotal * parseFloat(inputVal);
				}
			});

			var newAvailable = (creditsPool - assignedCredits);
			$("#availableCredits").text(newAvailable.toFixed(2));
			$("#assignedCredits").text(assignedCredits.toFixed(2));
			
			if (assignedCredits > creditsPool) {
				$("#notification-bar").removeClass("yellowStuff");
				$("#notification-bar").addClass("redStuff");
				$("#notification-bar-warning").toggle(false);
				$("#notification-bar-error").toggle(true);
			} else {
				$("#notification-bar").removeClass("redStuff");
				$("#notification-bar").addClass("yellowStuff");				
				$("#notification-bar-warning").toggle(true);
				$("#notification-bar-error").toggle(false);
			}

			var bk = Math.min(parseFloat($(this).parent().parent().parent().children().eq(3).html()),1);
			$(this).parent().parent().parent().children().eq(7).children().eq(0).nextAll().remove();
			var inputVal = $(this).val();
			$(this).removeClass("badInput");
			if(!inputVal || isNaN(inputVal)){
				$(this).addClass("badInput");
				$('<span class="badValue">O valor tem de ser um número.</span>').insertAfter($(this).parent().parent().parent().children().eq(7).children().eq(0));
			}else if (parseFloat(inputVal) > 1) {
				$(this).addClass("badInput");
				$('<span class="badValue">O valor tem que ser <= 1 !</span>').insertAfter($(this).parent().parent().parent().children().eq(7).children().eq(0));
			} else if (parseFloat(inputVal) <0) {
				$('<span class="badValue">O valor tem que ser >= 0 !</span>').insertAfter($(this).parent().parent().parent().children().eq(7).children().eq(0));
				$(this).addClass("badInput");
			} else if (parseFloat(inputVal) < bk) {
				$('<span class="badValue">O valor tem que ser justificado uma vez que é < min(Bk,1) !</span>').insertAfter($(this).parent().parent().parent().children().eq(7).children().eq(0));
			} 
			
			var badCount = 0;
			$( '.thisPage input[name$="unitCreditValue"]' ).each(function() {
				var inputVal = $(this).val();
				if ((!inputVal) || isNaN(inputVal) || parseFloat(inputVal) > 1 || parseFloat(inputVal) <0){
					badCount += 1;
				}
			});
			$('#sb1').attr("disabled", badCount > 0);
			$('#sb2').attr("disabled", badCount > 0);
		});
	});
</script>
