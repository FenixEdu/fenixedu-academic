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

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['title.showTests']}</h2>" escape="false" />

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />
	
		<h:panelGrid rendered="#{empty evaluationManagementBackingBean.onlineTestList}" >
			<h:outputText value="<em>#{bundle['message.onlineTests.not.scheduled']}</em>" escape="false"/>
		</h:panelGrid>
		<h:panelGrid rendered="#{!empty evaluationManagementBackingBean.onlineTestList}" >
			<h:dataTable value="#{evaluationManagementBackingBean.onlineTestList}" var="onlineTest">
				<h:column>
					<h:outputText value="<b>#{bundle['lable.test']}:</b> " escape="false"/>
					<h:outputText value="#{onlineTest.distributedTest.evaluationTitle}, "/>
					<h:outputText value="#{bundle['label.day']}" />
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{onlineTest.distributedTest.beginDateDate}"/>
					</h:outputFormat>
					<h:outputText value=" #{bundle['label.at']}" />
					<h:outputFormat value="{0, date, HH:mm}">
						<f:param value="#{onlineTest.distributedTest.beginHourDate}"/>
					</h:outputFormat>
					
					<h:outputText value="<br/><ul class=\"links\"><li><b>#{bundle['label.students.listMarks']}:</b> " escape="false"/>
					<h:commandLink action="enterShowMarksListOptions">
						<f:param name="evaluationID" value="#{onlineTest.externalId}" />
						<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}" />
					</h:commandLink>

					<h:outputText value="<b> | </b>" escape="false"/>
					<h:commandLink action="enterPublishMarks">
						<f:param name="evaluationID" value="#{onlineTest.externalId}" />
						<h:outputFormat value="#{bundle['link.publishMarks']}" />
					</h:commandLink>

					<h:outputText value="</li><ul>" escape="false"/>
					<h:outputText value="<br/>" escape="false"/>
				</h:column>
			</h:dataTable>
		</h:panelGrid>
	</h:form>

</f:view>
</div>
</div>
