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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams.MainExamsDA" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundleSOP"/>

	<h:outputText value="<h2>#{bundleSOP['property.exam.associate']}</h2>" escape="false" />
	
	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.evaluationIdHidden}"/>

		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.academicIntervalHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.calendarPeriodHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginMinuteHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endMinuteHidden}"/>
		
		<h:outputText escape="false" value="<input alt='input.academicInterval' id='academicInterval' name='academicInterval' type='hidden' value='#{SOPEvaluationManagementBackingBean.academicInterval}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:outputText escape="false" value="<input alt='input.year' id='year' name='year' type='hidden' value='#{SOPEvaluationManagementBackingBean.year}'/>"/>
		<h:outputText escape="false" value="<input alt='input.month' id='month' name='month' type='hidden' value='#{SOPEvaluationManagementBackingBean.month}'/>"/>
		<h:outputText escape="false" value="<input alt='input.day' id='day' name='day' type='hidden' value='#{SOPEvaluationManagementBackingBean.day}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginHour' id='beginHour' name='beginHour' type='hidden' value='#{SOPEvaluationManagementBackingBean.beginHour}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginMinute' id='beginMinute' name='beginMinute' type='hidden' value='#{SOPEvaluationManagementBackingBean.beginMinute}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endHour' id='endHour' name='endHour' type='hidden' value='#{SOPEvaluationManagementBackingBean.endHour}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endMinute' id='endMinute' name='endMinute' type='hidden' value='#{SOPEvaluationManagementBackingBean.endMinute}'/>"/>

		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
<%-- 		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>
--%>
		<h:outputText value="<table class='tstyle5 thlight thright thmiddle mtop05 mbottom2'>" escape="false"/>
		<h:outputText value="<tr><th>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.degree']}: " />
		<h:outputText value="</th><td>" escape="false"/>
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedExecutionDegreeID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onExecutionDegreeChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</fc:selectOneMenu>
		<h:outputText value="</td></tr>" escape="false"/>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionDegreeID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueExecutionDegreeID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
--%>
		<h:outputText value="<tr><th>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: " />
		<h:outputText value="</th><td>" escape="false"/>
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedCurricularYearID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onCurricularYearChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}"/>
			</fc:selectOneMenu>
		<h:outputText value="</td></tr>" escape="false"/>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.curricularYearID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueCurricularYearID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}" />
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID4' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
--%>
		<h:outputText value="<tr><th>" escape="false"/>
			<h:outputText value="#{bundleSOP['lable.execution.course']}: " />
		<h:outputText value="</th><td>" escape="false"/>
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.selectedExecutionCourseID}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.onExecutionCourseChanged}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionCoursesItems}"/>
			</fc:selectOneMenu>
		<h:outputText value="</td></tr>" escape="false"/>
<%--
			<fc:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionCourseID}" 
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValueExecutionCourseID}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionCoursesLabels}" />
			</fc:selectOneMenu> 
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID6' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
--%>			
		<h:outputText value="</table>" escape="false"/>	
		

		<h:commandButton alt="#{htmlAltBundle['commandButton.associate']}" action="#{SOPEvaluationManagementBackingBean.associateExecutionCourse}" value="#{bundleSOP['button.associate']}" styleClass="inputbutton"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="#{SOPEvaluationManagementBackingBean.returnToCreateOrEdit}" value="#{bundleSOP['button.cancel']}" styleClass="inputbutton"/>
	</h:form>

</f:view>
