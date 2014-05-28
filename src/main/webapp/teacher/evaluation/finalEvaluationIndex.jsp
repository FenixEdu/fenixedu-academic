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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


<f:view>

	<h:outputText value="#{evaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<logic:messagesPresent message="true">
		<html:messages bundle="APPLICATION_RESOURCES" id="messages" message="true">
			<span class="error0"><bean:write name="messages" /></span>
		</html:messages>
		<br/>
		<br/>
	</logic:messagesPresent>
	
	<h:messages layout="table" errorClass="error"/>
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
				
		<h:outputText value="<h2>#{bundle['label.finalEvaluation']}</h2>" escape="false" />
	
		<h:outputText value="<ul class='links'><li><b>#{bundle['label.students.listMarks']}:</b> " escape="false"/>
		<h:commandLink action="enterShowMarksListOptions">
			<f:param name="evaluationID" value="#{evaluationManagementBackingBean.finalEvaluation.externalId}" />
			<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}"/>
		</h:commandLink>
		
		<h:outputText value=" | " escape="false"/>
		<h:commandLink action="enterPublishMarks">
			<f:param name="evaluationID" value="#{evaluationManagementBackingBean.finalEvaluation.externalId}" />
			<h:outputFormat value="#{bundle['link.publishMarks']}" />
		</h:commandLink>

<%--		<h:outputText value=" | " escape="false"/>
		<h:commandLink action="enterSubmitMarksList">
			<f:param name="evaluationID" value="#{evaluationManagementBackingBean.finalEvaluation.externalId}" />		
			<h:outputFormat value="#{bundle['label.submit.listMarks']}" />
		</h:commandLink>
--%>
		<h:outputText value=" | " escape="false"/>
		<h:outputText value="<a href='#{evaluationManagementBackingBean.contextPath}/teacher/markSheetManagement.do?method=prepareSubmitMarks&amp;executionCourseID=#{evaluationManagementBackingBean.executionCourseID}'>#{bundle['label.submit.listMarks']}</a>" escape="false"/>
		
		<h:outputText value=" | " escape="false"/>
		<h:outputText value="<a href='#{evaluationManagementBackingBean.contextPath}/teacher/markSheetManagement.do?method=viewSubmitedMarkSheets&amp;executionCourseID=#{evaluationManagementBackingBean.executionCourseID}'>#{bundle['label.view.submited.markSheets']}</a>" escape="false"/>

		<h:outputText value="</li></ul>" escape="false"/>
	</h:form>

</f:view>

</div>
</div>