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

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>

	<h:outputText value="#{evaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>


	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.publishMarks']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
		value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>


		<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.onlineTests.OnlineTest'}">
			<h:outputText value="#{bundle['lable.test']}: " escape="false"/>
			<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.distributedTest.evaluationTitle}</b>, " escape="false"/>
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.distributedTest.beginDateDate}"/>
			</h:outputFormat>
			<h:outputText value=" #{bundle['label.at']}" />
			<h:outputFormat value="{0, date, HH:mm}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.distributedTest.beginHourDate}"/>
			</h:outputFormat>
		</h:panelGroup>

		<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
			<h:outputText value="#{bundle['label.written.test']}: " escape="false"/>
			<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.description}</b>, " escape="false"/>
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
			</h:outputFormat>
			<h:outputText value=" #{bundle['label.at']}" />
			<h:outputFormat value="{0, date, HH:mm}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
			</h:outputFormat>
		</h:panelGroup>

		<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
			<h:outputText value="#{bundle['label.exam']}: " escape="false"/>
			<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.season}</b>, " escape="false"/>
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
			</h:outputFormat>
			<h:outputText value=" #{bundle['label.at']}" />
			<h:outputFormat value="{0, date, HH:mm}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
			</h:outputFormat>
		</h:panelGroup>

		<h:outputText value="<div class='infoop2 mvert15'>" escape="false"/>
			<h:outputText value="#{bundle['label.publish.information']}" escape="false"/>
		<h:outputText value="</div>" escape="false"/>



	<h:form>
		<h:outputText value="<input type=hidden name='executionCourseID' value='#{evaluationManagementBackingBean.executionCourse.externalId}'/>" escape="false"/>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:outputText value="#{bundle['message.publishmentMessage']}" escape="false"/>
		<h:outputText value="(" /><h:outputText value="#{bundle['message.optional']}"/><h:outputText value="): <br/>" escape="false"/>
		<h:inputTextarea cols="45" value="#{evaluationManagementBackingBean.publishMarksMessage}"/>
		<h:outputText value="<br/><br/>#{bundle['message.sendSMS']}: <br/>" escape="false"/>
		<h:selectBooleanCheckbox disabled="true" value="#{evaluationManagementBackingBean.sendSMS}"/>
		<h:outputText styleClass="warning0" value="#{bundle['message.sms.unavailable']}"/>
		
		<h:outputText value="<p class='mtop15'>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.insert']}" styleClass="inputbutton" action="#{evaluationManagementBackingBean.publishMarks}" value="#{bundle['button.post']}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</f:view>
</div>
</div>
