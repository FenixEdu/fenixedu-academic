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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<jsp:include page="../teacherCreditsStyles.jsp"/>


<em><bean:message key="label.teacherService.credits"/></em>
<h3><bean:message key="label.credits.creditsReduction.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>

<html:messages id="message" message="true" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
	<span class="error">
		<bean:write name="message" filter="false" />
	</span>
</html:messages>

<logic:present name="reductionServiceBean">
	<logic:empty name="reductionServiceBean" property="teacher">
		<fr:edit id="reductionServiceBean" name="reductionServiceBean" action="/creditsReductions.do?method=aproveReductionService">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.ReductionServiceBean">
				<fr:slot name="teacher" layout="autoComplete">
					<fr:property name="size" value="80"/>
					<fr:property name="format" value="${person.name} (${person.username})"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchTeachers"/>
					<fr:property name="args" value="slot=person.name"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="errorStyleClass" value="error0"/>
					<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator" />
				</fr:slot>
				<fr:destination name="cancel" path="/creditsReductions.do?method=showReductionServices"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight mtop15" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
	
	</logic:empty>
	<logic:notEmpty name="reductionServiceBean" property="teacher">
		<fr:view name="reductionServiceBean">
			<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="org.fenixedu.academic.domain.credits.util.ReductionServiceBean">
				<fr:slot name="teacher.person" key="label.empty" layout="view-as-image">
					<fr:property name="classes" value="column3" />
					<fr:property name="moduleRelative" value="false" />
					<fr:property name="contextRelative" value="true" />
					<fr:property name="imageFormat" value="/user/photo/${teacher.person.username}" />
				</fr:slot>
				<fr:slot name="teacher.person.presentationName" key="label.name"/>
				<fr:slot name="teacherCategory" key="label.category" layout="null-as-label"/>
				<fr:slot name="facultyEvaluationProcessYear.year" key="label.evaluation.year" layout="null-as-label"/>
				<fr:slot name="teacherEvaluationMarkString" key="label.evaluation.mark"  layout="null-as-label"/>
				<fr:slot name="teacher.person.dateOfBirthYearMonthDay" key="label.dateOfBirth"  layout="null-as-label"/>
				<fr:slot name="maxCreditsFromEvaluationAndAge" key="label.maxCreditsFromEvaluationAndAgeSugested" layout="null-as-label"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
				<fr:property name="columnClasses" value="headerTable,,tdclear tderror1"/>
			</fr:layout>
		</fr:view>
		<logic:empty name="reductionServiceBean" property="reductionService">
			<bean:define id="teacherService" name="reductionServiceBean" property="teacherService"/>
			<bean:define id="teacherServiceOID" name="teacherService" property="externalId"/>
			<div class="forminline dinline">
				<fr:form action="<%="/creditsReductions.do?method=showReductionServices&teacherServiceOID="+ teacherServiceOID%>">
					<fr:edit id="reductionServiceBean" name="reductionServiceBean" visible="false"/>
					<fr:create id="reductionService" schema="create.reductionServiceAttributed" type="org.fenixedu.academic.domain.teacher.ReductionService">
						<fr:hidden slot="teacherService" name="teacherService"/>
						<fr:layout>
							<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
							<fr:property name="columnClasses" value="headerTable,,tdclear tderror1"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%="/creditsReductions.do?method=aproveReductionService&invalidated=true&teacherServiceOID="+ teacherServiceOID%>"/>
					</fr:create>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message key="button.submit" /></html:submit>
				</fr:form>
				<fr:form action="<%="/creditsReductions.do?method=showReductionServices&teacherServiceOID="+ teacherServiceOID%>">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message key="button.cancel" /></html:submit>
				</fr:form>
			</div>
		</logic:empty>
		<logic:notEmpty name="reductionServiceBean" property="reductionService">
			<bean:define id="reductionServiceOID" name="reductionServiceBean" property="reductionService.externalId"/>
			<fr:edit id="reductionService" name="reductionServiceBean" property="reductionService" action="<%="/creditsReductions.do?method=showReductionServices&reductionServiceOID="+ reductionServiceOID%>">
				<fr:schema type="org.fenixedu.academic.domain.teacher.ReductionService" bundle="TEACHER_CREDITS_SHEET_RESOURCES">
					<fr:slot name="requestCreditsReduction" key="label.requestedReductionCredits" readOnly="true" layout="radio"/>
					<fr:slot name="creditsReductionAttributed" key="label.attributedReductionCredits" required="true" validator="pt.ist.fenixWebFramework.renderers.validators.NumberValidator"/>
				</fr:schema>
				<fr:layout>
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
					<fr:property name="columnClasses" value="headerTable,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="cancel" path="<%="/creditsReductions.do?method=showReductionServices&reductionServiceOID="+ reductionServiceOID%>"/>
				<fr:destination name="invalid" path="<%="/creditsReductions.do?method=aproveReductionService&invalidated=true&reductionServiceOID="+ reductionServiceOID%>"/>
			</fr:edit>
		</logic:notEmpty>
	</logic:notEmpty>
</logic:present>