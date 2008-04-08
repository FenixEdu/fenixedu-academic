<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
	<h:outputFormat value="<h2>#{bundle['title.evaluation.choose.room']}</h2>" escape="false"/>
	
	
	<h:outputText value="<p>" escape="false" />
		<h:outputText value="#{bundle['label.exam']}: " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
		<h:outputText value="#{bundle['label.written.test']}: " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
		<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.season}</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
		<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.description}</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>

		<h:outputFormat value="{0, date, dd/MM/yyyy}">
			<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
		</h:outputFormat>
		<h:outputText value=" #{bundle['label.at']} " escape="false"/>
		<h:outputFormat value="{0, date, HH:mm}">
			<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
		</h:outputFormat>
		<h:outputText value=" #{bundle['label.to']} " escape="false"/>
		<h:outputFormat value="{0, date, HH:mm}">
			<f:param value="#{evaluationManagementBackingBean.evaluation.endDate}"/>
		</h:outputFormat>
		<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.evaluation.associatedRooms}">
			<h:outputText value="#{evaluationManagementBackingBean.evaluation.associatedRoomsAsStringList}" escape="false"/>
		</h:panelGroup>
	<h:outputText value="</p>" escape="false" />

	<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
		value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
	<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:outputText value="<p>" escape="false" />
		<h:outputText value="#{bundle['message.writtenTests.associate.rooms']}" />
		<h:outputText value="</p>" escape="false" />
		<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.availableRooms}">
			
			<h:selectManyCheckbox styleClass="tstyle5 thlight thright" id="associateRooms" value="#{evaluationManagementBackingBean.roomsToAssociate}" layout="pageDirection">
				<f:selectItems value="#{evaluationManagementBackingBean.availableRooms}"/>
			</h:selectManyCheckbox>

			<h:outputText value="<p>" escape="false" />
				<h:commandButton alt="#{htmlAltBundle['commandButton.update']}" action="#{evaluationManagementBackingBean.editEvaluationRooms}" value="#{bundle['button.update']}" styleClass="inputButton"/>
				<h:outputText value="&nbsp;" escape="false"/>
				<h:commandButton alt="#{htmlAltBundle['commandButton.goBack']}" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['link.goBack']}" styleClass="inputButton" />
			<h:outputText value="</p>" escape="false" />			
		</h:panelGroup>	
		<h:panelGroup rendered="#{empty evaluationManagementBackingBean.availableRooms}">
			<h:outputText value="<p>" escape="false" />
			<h:outputText value="#{bundle['message.writtenTests.empty.associate.rooms']}" />
			<h:outputText value="</p>" escape="false" />
		</h:panelGroup>
	</h:form>	
</ft:tilesView>
