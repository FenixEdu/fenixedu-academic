<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml/>

<jsp:include page="../teacherCreditsStyles.jsp"/>


<em><bean:message key="label.teacherService.credits"/></em>
<h3><bean:message key="label.managementFunctionNote" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<logic:present name="personFunctionBean">
	<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="personFunctionBean" property="teacher.person.username"/></bean:define>
	<table class="headerTable"><tr>
	<td><img src="<%= request.getContextPath() + url %>"/></td>
	<td ><fr:view name="personFunctionBean">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean">
			<fr:slot name="teacher.person.presentationName" key="label.name"/>
			<fr:slot name="teacher.currentWorkingDepartment.name" key="label.department" layout="null-as-label"/>
			<fr:slot name="executionSemester" key="label.period" layout="format">
				<fr:property name="format" value="${name}  ${executionYear.year}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="creditsStyle"/>
		</fr:layout>
	</fr:view></td>
	</tr></table>

<br/>
<bean:define id="executionYearOid" name="personFunctionBean" property="executionSemester.executionYear.externalId"/>
<bean:define id="teacherOid" name="personFunctionBean" property="teacher.externalId"/>

<p><html:link page="<%="/credits.do?method=viewAnnualTeachingCredits&amp;executionYearOid="+executionYearOid+"&teacherOid="+teacherOid%>"><bean:message key="link.return"/></html:link></p>
<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
	<span class="error"><bean:write name="message" filter="false" /></span>
</html:messages>

	<logic:empty name="personFunctionBean" property="unit">
		<fr:edit id="personFunctionBean1" name="personFunctionBean" action="/managePersonFunctionsShared.do?method=prepareToAddPersonFunction">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean">
				<fr:slot name="unit" key="label.departmentOrDegreeOrUnit" layout="autoCompleteWithPostBack">
					<fr:property name="size" value="80"/>
					<fr:property name="labelField" value="name"/>
					<fr:property name="format" value="${presentationName}"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchAllActiveInternalUnits"/>	
					<fr:property name="args" value="slot=name"/>		
					<fr:property name="minChars" value="3"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"/>	
					<fr:property name="errorStyleClass" value="error0"/>
					<fr:property name="destination" value="/managePersonFunctionsShared.do?method=prepareToAddPersonFunction"/>
					<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"/>
		</fr:edit>
	</logic:empty>
	
	<logic:notEmpty name="personFunctionBean" property="unit">
		<logic:empty name="personFunctionBean" property="function">
			<fr:edit id="personFunctionBean2" name="personFunctionBean" action="/managePersonFunctionsShared.do?method=prepareToAddPersonFunction">
				<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean">
					<fr:slot name="unit.presentationName" key="label.unit" readOnly="true"/>
					<fr:slot name="function" key="label.function" layout="menu-select-postback" required="true">
						<fr:property name="from" value="availableFunctions"/>
						<fr:property name="destination" value="/managePersonFunctionsShared.do?method=prepareToAddPersonFunction"/>
						<fr:property name="format" value="${typeName}"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="cancel" path="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"/>
			</fr:edit>
		</logic:empty>	
		<logic:notEmpty name="personFunctionBean" property="function">
			<bean:define id="function" name="personFunctionBean" property="function" type="net.sourceforge.fenixedu.domain.organizationalStructure.Function"/>
			<% if( function.isSharedFunction()){ %>
				<fr:edit id="personFunctionBean3" name="personFunctionBean" action="/managePersonFunctionsShared.do?method=editPersonFunctionShared">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean">
						<fr:slot name="unit.presentationName" key="label.unit" readOnly="true"/>
						<fr:slot name="function.typeName" key="label.function" readOnly="true"/>
						<fr:slot name="percentage" key="label.teacher-dfp-student.percentage" required="true"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="cancel" path="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"/>
				</fr:edit>
				<br/>
				<logic:notEmpty name="personFunctionBean" property="personFunctionsShared">
					<h3><bean:message key="label.percentageDistribuition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
					<fr:view name="personFunctionBean" property="personFunctionsShared">
						<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean">
							<fr:slot name="childParty" key="label.empty" layout="view-as-image">
								<fr:property name="classes" value="column3" />
								<fr:property name="useParent" value="true" />
								<fr:property name="moduleRelative" value="false" />
								<fr:property name="contextRelative" value="true" />
								<fr:property name="imageFormat" value="/person/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=${person.istUsername}" />
							</fr:slot>
							<fr:slot name="childParty.presentationName" key="label.name"/>
							<fr:slot name="percentage" key="label.teacher-dfp-student.percentage"/>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
							<fr:property name="columnClasses" value="headerTable,,"/>
						</fr:layout>
					</fr:view>
				</logic:notEmpty>
			<%} else{ %>
				<fr:edit id="personFunctionBean3" name="personFunctionBean" action="/managePersonFunctionsShared.do?method=editPersonFunction">
					<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.credits.util.PersonFunctionBean">
						<fr:slot name="unit.presentationName" key="label.unit" readOnly="true"/>
						<fr:slot name="function.typeName" key="label.function" readOnly="true"/>
						<fr:slot name="credits" key="label.credits" required="true"/>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="cancel" path="<%="/credits.do?method=viewAnnualTeachingCredits&executionYearOid="+executionYearOid+"&teacherOid="+teacherOid %>"/>
				</fr:edit>
			<% } %>
		</logic:notEmpty>
	</logic:notEmpty>
	<br/>
</logic:present>