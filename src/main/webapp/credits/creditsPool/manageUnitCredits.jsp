<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
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
-->
</style>

<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>


<logic:present name="departmentCreditsBean">
	<fr:form action="/creditsPool.do?method=viewDepartmentExecutionCourses">
		<fr:edit id="departmentCreditsBean" name="departmentCreditsBean">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean">
				<fr:slot name="department" key="label.department" layout="menu-select" required="true">
					<fr:property name="from" value="availableDepartments"/>
					<fr:property name="format" value="${name}"/>
				</fr:slot>
				<fr:slot name="executionYear" key="label.executionYear" layout="menu-select" required="true">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
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
						<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.departmentCreditsPool"/>:
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
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsPoolBean">
					<fr:slot name="departmentCreditsPool.creditsPool" key="label.departmentCreditsPool"/>
					<fr:slot name="assignedCredits" key="label.assignedCredits"/>
					<fr:slot name="availableCredits" key="label.availableCredits"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
				</fr:layout>
			</fr:view>
		
			<h3><bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.courses.shared"/></h3>
			<bean:define id="canEditSharedUnitCredits" name="departmentCreditsPoolBean" property="canEditSharedUnitCredits" type="java.lang.Boolean"/>
			<fr:form id="banana" action="/creditsPool.do?method=editUnitCredits">
				<fr:edit id="departmentCreditsPoolBean" name="departmentCreditsPoolBean" visible="false"/>
				<fr:edit name="departmentCreditsPoolBean" property="departmentSharedExecutionCourses" >
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsPoolBean$DepartmentExecutionCourse">
						<fr:slot name="executionCourse.name" key="label.course" readOnly="true"/>
						<fr:slot name="executionCourse.degreePresentationString" key="label.degrees" readOnly="true"/>
						<fr:slot name="executionCourse.executionPeriod.semester" key="label.execution-period" readOnly="true"/>
						<fr:slot name="executionCourse.effortRate" key="label.effortRate" readOnly="true"/>
						<fr:slot name="departmentEffectiveLoad" key="label.departmentEffectiveLoad" readOnly="true"/>
						<fr:slot name="totalEffectiveLoad" key="label.totalEffectiveLoad" readOnly="true"/>
						<fr:slot name="unitCreditValue" key="label.unitCredit" readOnly="<%=!canEditSharedUnitCredits%>" >
							<fr:property name="maxLength" value="4"/>
							<fr:property name="size" value="2"/>
						</fr:slot>
					</fr:schema>
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
					</fr:layout>
				</fr:edit>
				<logic:equal name="canEditSharedUnitCredits" value="true">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit>
				</logic:equal>
			</fr:form>
			<h3><bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.courses.not.shared"/></h3>
			<bean:define id="canEditUnitCredits" name="departmentCreditsPoolBean" property="canEditUnitCredits" type="java.lang.Boolean"/>
			<fr:form action="/creditsPool.do?method=editUnitCredits">
				<fr:edit id="departmentCreditsPoolBean" name="departmentCreditsPoolBean" visible="false"/>
				<fr:edit name="departmentCreditsPoolBean" property="departmentExecutionCourses">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsPoolBean$DepartmentExecutionCourse">
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
					</fr:schema>
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle2 thleft thlight mtop05"/>
					</fr:layout>
				</fr:edit>
				<logic:equal name="canEditUnitCredits" value="true">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit"/></html:submit>
				</logic:equal>
			</fr:form>
		</logic:present>
		<logic:notPresent name="departmentCreditsPoolBean" property="departmentCreditsPool">
			<bean:message bundle="TEACHER_CREDITS_SHEET_RESOURCES" key="label.department.credits.pool.not.defined"/>
		</logic:notPresent>
	</logic:present>
</logic:present>

<script type="text/javascript" language="javascript">
	$(document).ready(function(){
		$(':input').change(function(){
			$("#notification-bar").toggle(true);
			var i = $(this).attr("name");
			var assignedCredits = 0;
			$( 'input[name$="unitCreditValue"]' ).each(function() {
				var cleTotal = parseFloat($(this).parent().parent().parent().children().eq(4).html());
				assignedCredits += cleTotal * parseFloat($(this).val());
			});
			$("#assignedCredits").text(assignedCredits);

			var creditsPool = parseFloat($("#creditsPool").html());
			var newAvailable = creditsPool - assignedCredits;
			$("#availableCredits").text(newAvailable);

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

			var bk = parseFloat($(this).parent().parent().parent().children().eq(4).html());

			if (parseFloat($(this).val()) > 1) {
				$('<span class="badValue">O valor tem que ser <= 1 !</span>').insertAfter($(this));
			} else if (parseFloat($(this).val()) <= 0) {
				$('<span class="badValue">O valor tem que ser > 0 !</span>').insertAfter($(this));
			} else if (parseFloat($(this).val()) <= bk) {
				$('<span class="badValue">O valor tem que ser justificado uma vez que é < Bk !</span>').insertAfter($(this));
			} else {
				$(this).nextAll().remove();
			}
		});
	});
</script>
