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

<f:view>

	<h:outputText value="#{evaluationManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<h:form>	
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
		<fc:viewState binding="#{evaluationManagementBackingBean.viewState}"/>
		
		<h:outputText value="<h2>#{bundle['label.distribute.information']}</h2>" escape="false" />

		<h:outputText value="<div class='infoop2'>#{bundle['message.distribution.alreadyExists']}</div>" 
			rendered="#{evaluationManagementBackingBean.existsADistribution}" escape="false"/>			

		<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.evaluationRooms}">
			<h:outputText value="#{bundle['label.distribute.students']}" styleClass="bold" />		
			<h:selectOneRadio id="distributeEnroledStudentsOption"
					value="#{evaluationManagementBackingBean.distributeEnroledStudentsOption}" layout="pageDirection" >
				<f:selectItem itemValue="true" itemLabel="#{bundle['label.distribute.enrolled']} (#{evaluationManagementBackingBean.evaluation.writtenEvaluationEnrolmentsCount})" />
				<f:selectItem itemValue="false" itemLabel="#{bundle['label.distribute.allAttend']} (#{evaluationManagementBackingBean.numberOfAttendingStudents})" />
			</h:selectOneRadio>
			
			<h:outputText value="<br/>#{bundle['label.distribute.rooms']}<br/>" styleClass="bold" escape="false" />
	
	
			
			<h:dataTable value="#{evaluationManagementBackingBean.evaluationRooms}" var="room">
				<h:column>
					<h:outputText value="#{evaluationManagementBackingBean.evaluationRoomsPositions[room.externalId]}. " styleClass="bold" />
				</h:column>
				<h:column>
					<h:outputText value="#{bundle['label.lesson.room']} " />
					<h:outputText value="#{room.name} " />
					<h:outputText value="(#{room.capacidadeExame} " />
					<h:outputText value="#{bundle['label.distribute.places']})" />
				</h:column>
			</h:dataTable>
			
			<h:outputText value="<br/>" escape="false"/>
			<h:panelGroup styleClass="infoop">				
				<h:outputText value="#{bundle['label.distribute.changeRoom']} " />
				<h:selectOneMenu value="#{evaluationManagementBackingBean.roomToChangeID}" onchange="this.form.submit();"
						valueChangeListener="#{evaluationManagementBackingBean.changeRoom}" >
					<f:selectItems value="#{evaluationManagementBackingBean.names}" />
				</h:selectOneMenu>
				<h:outputText value=" #{bundle['label.distribute.position']} " />			
				<h:selectOneMenu value="#{evaluationManagementBackingBean.newRoomPosition}" onchange="this.form.submit();"
						valueChangeListener="#{evaluationManagementBackingBean.changePosition}">
					<f:selectItems value="#{evaluationManagementBackingBean.positions}"/>
				</h:selectOneMenu>
			</h:panelGroup>			
			
			<h:outputText value="<br/><br/>" escape="false"/>
			
			<h:outputText value="<em class='highlight1'>#{bundle['message.distribution.notReversible']}</em><br/><br/>" 
				rendered="#{!evaluationManagementBackingBean.existsADistribution}" escape="false"/>						
			
			<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
					value="#{bundle[evaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />		
						
			<h:commandButton alt="#{htmlAltBundle['commandButton.distribute']}" action="#{evaluationManagementBackingBean.distributeStudentsByRooms}" value="#{bundle['label.distribute']}" styleClass="inputButton" />	
		</h:panelGroup>

		<h:panelGroup rendered="#{empty evaluationManagementBackingBean.evaluationRooms}">
			<h:outputText value="<i>#{bundle['label.no.room.associated']}</i><br/><br/>" escape="false"/>
		</h:panelGroup>

		<h:commandButton alt="#{htmlAltBundle['commandButton.goBack']}" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['link.goBack']}" styleClass="inputButton" />		
	</h:form>
</f:view>		

</div>
</div>
