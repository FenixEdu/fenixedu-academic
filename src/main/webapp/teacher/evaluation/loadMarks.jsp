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

<f:view>

	<h:outputText value="#{evaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.marksListWithFile']}</h2>" escape="false"/>
	
	<h:outputFormat value="<h3>#{bundle['title.evaluation.manage.marksListWithFile.course']}</h3>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:form enctype="multipart/form-data">
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.onlineTests.OnlineTest'}">
				<h:outputText value="#{bundle['lable.test']}: "/>
				<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.distributedTest.evaluationTitle}</b> " escape="false"/>
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
				<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.description}</b> " escape="false"/>
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.at']}" />
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
				</h:outputFormat>
			</h:panelGroup>

			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
				<h:outputText value="#{bundle['label.exam']}: "/>
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
				<h:outputText value="#{bundle['label.fileUpload.information']}" escape="false"/>
				
				<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.gradeScaleDescription}">
					<h:outputText value="<p>" escape="false"/>
						<h:outputText value="#{bundle['label.marksOnline.currentGradeScale']}" escape=""/>
						<h:outputText value="#{evaluationManagementBackingBean.gradeScaleDescription}" escape="false"/>
					<h:outputText value="</p>" escape="false"/>
				</h:panelGroup>
			<h:outputText value="</div>" escape="false"/>
			

			<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
				value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
				
			<h:panelGroup rendered="#{evaluationManagementBackingBean.messagesEmpty}">
				<h:outputText styleClass="error" value="#{bundle['error.load.mark.file']}" />
				<h:messages layout="table" errorClass="error"/>
			</h:panelGroup>				

			<h:outputText value="<p class='mtop15'>" escape="false"/>
				<h:outputText value="#{bundle['label.file']}: " escape="false"/>
				<h:outputText value="<input alt=\"input.input\" size=\"30\" type=\"file\" name=\"theFile\"/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p class='mtop15'>" escape="false"/>
				<h:commandButton alt="#{htmlAltBundle['commandButton.send']}" action="#{evaluationManagementBackingBean.loadMarks}" value="#{bundle['button.send']}"/>
				<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="success" value="#{bundle['button.cancel']}"/>
			<h:outputText value="</p>" escape="false"/>

	</h:form>

</f:view>
</div>
</div>
