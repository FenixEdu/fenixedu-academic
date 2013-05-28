<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
	<span class="error">
		<bean:write name="message" filter="false"/>
	</span>
</html:messages>


<logic:present name="departmentCreditsBean">
	<fr:edit id="departmentCreditsBean" name="departmentCreditsBean" action="/creditsPool.do?method=viewDepartmentExecutionCourses">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean">
			<fr:slot name="department" key="label.department" layout="menu-select">
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
	</fr:edit>
	<logic:present name="departmentCreditsPoolBean">
		<h3><bean:message key="label.departmentCreditsPool" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
		<logic:present name="departmentCreditsPoolBean" property="departmentCreditsPool">
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
		
			<h3>Disciplinas partilhadas</h3>
			<bean:define id="canEditSharedUnitCredits" name="departmentCreditsPoolBean" property="canEditSharedUnitCredits" type="java.lang.Boolean"/>
			<fr:form action="/creditsPool.do?method=editUnitCredits">
				<fr:edit id="departmentCreditsPoolBean" name="departmentCreditsPoolBean" visible="false"/>
				<fr:edit name="departmentCreditsPoolBean" property="departmentSharedExecutionCourses" >
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsPoolBean$DepartmentExecutionCourse">
						<fr:slot name="executionCourse.externalId" key="label.course" readOnly="true"/>
						<fr:slot name="executionCourse.name" key="label.course" readOnly="true"/>
						<fr:slot name="executionCourse.degreePresentationString" key="label.degrees" readOnly="true"/>
						<fr:slot name="executionCourse.effortRate" key="label.effortRate" readOnly="true"/>
						<fr:slot name="departmentEffectiveLoad" key="label.departmentEffectiveLoad" readOnly="true"/>
						<fr:slot name="totalEffectiveLoad" key="label.totalEffectiveLoad" readOnly="true"/>
						<fr:slot name="unitCreditValue" key="label.unitCredit" readOnly="<%=!canEditSharedUnitCredits%>">
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
			<h3>Restantes Disciplinas</h3>
			<bean:define id="canEditUnitCredits" name="departmentCreditsPoolBean" property="canEditUnitCredits" type="java.lang.Boolean"/>
			<fr:form action="/creditsPool.do?method=editUnitCredits">
				<fr:edit id="departmentCreditsPoolBean" name="departmentCreditsPoolBean" visible="false"/>
				<fr:edit name="departmentCreditsPoolBean" property="departmentExecutionCourses">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsPoolBean$DepartmentExecutionCourse">
						<fr:slot name="executionCourse.externalId" key="label.course" readOnly="true"/>
						<fr:slot name="executionCourse.name" key="label.course" readOnly="true"/>
						<fr:slot name="executionCourse.degreePresentationString" key="label.degrees" readOnly="true"/>
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
	</logic:present>
</logic:present>