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
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.curricularYearIdHidden}"/>
		
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.yearHidden}"/>
		
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.evaluationTypeHidden}" />
		
		<h:outputText value="<h2>#{bundle['title.choose.discipline']}</h2><br/>" escape="false" />
		<h:selectOneMenu value="#{coordinatorEvaluationManagementBackingBean.executionCourseID}" >
			<f:selectItems value="#{coordinatorEvaluationManagementBackingBean.executionCoursesLabels}" />
		</h:selectOneMenu>
		<h:outputText value="<br/><br/>" escape="false" />		
		<h:commandButton alt="#{htmlAltBundle['commandButton.choose']}" action="#{coordinatorEvaluationManagementBackingBean.selectExecutionCourse}"
		  value="#{bundle['label.choose']}" styleClass="inputbutton"/>
		
	</h:form>
</f:view>