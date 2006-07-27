<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

<style>
.boldFontClass { 
	font-weight: bold
}
</style>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<h:form>	
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
		<fc:viewState binding="#{evaluationManagementBackingBean.viewState}"/>
		
		<h:outputText value="<h2>#{bundle['label.distribute.information']}</h2>" escape="false" />

		<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.evaluationRooms}">
			<h:outputText value="#{bundle['label.distribute.students']}" styleClass="boldFontClass" />		
			<h:selectOneRadio id="distributeEnroledStudentsOption"
					value="#{evaluationManagementBackingBean.distributeEnroledStudentsOption}" layout="pageDirection" >
				<f:selectItem itemValue="true" itemLabel="#{bundle['label.distribute.enrolled']} (#{evaluationManagementBackingBean.evaluation.writtenEvaluationEnrolmentsCount})" />
				<f:selectItem itemValue="false" itemLabel="#{bundle['label.distribute.attend']} (#{evaluationManagementBackingBean.numberOfAttendingStudents})" />
			</h:selectOneRadio>
			
			<h:outputText value="<br/>#{bundle['label.distribute.rooms']}<br/>" styleClass="boldFontClass" escape="false" />
	
	
			
			<h:dataTable value="#{evaluationManagementBackingBean.evaluationRooms}" var="room">
				<h:column>
					<h:outputText value="#{evaluationManagementBackingBean.evaluationRoomsPositions[room.idInternal]}. " styleClass="boldFontClass" />
				</h:column>
				<h:column>
					<h:outputText value="#{bundle['label.lesson.room']} " />
					<h:outputText value="#{room.nome} " />
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
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
				<h:outputText value=" #{bundle['label.distribute.position']} " />			
				<h:selectOneMenu value="#{evaluationManagementBackingBean.newRoomPosition}" onchange="this.form.submit();"
						valueChangeListener="#{evaluationManagementBackingBean.changePosition}">
					<f:selectItems value="#{evaluationManagementBackingBean.positions}"/>
				</h:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			</h:panelGroup>
			
			<h:outputText value="<br/><br/>" escape="false"/>
			<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
					value="#{bundle[evaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />		
			
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" action="#{evaluationManagementBackingBean.distributeStudentsByRooms}" value="#{bundle['label.submit']}" styleClass="inputButton" />	
		</h:panelGroup>

		<h:panelGroup rendered="#{empty evaluationManagementBackingBean.evaluationRooms}">
			<h:outputText value="<i>#{bundle['label.no.room.associated']}</i><br/><br/>" escape="false"/>
		</h:panelGroup>

		<h:commandButton alt="#{htmlAltBundle['commandButton.goBack']}" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['link.goBack']}" styleClass="inputButton" />		
	</h:form>
</ft:tilesView>		