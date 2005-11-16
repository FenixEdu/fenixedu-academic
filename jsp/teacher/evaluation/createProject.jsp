<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
.alignright {
text-align: right;
}
.valigntop {
vertical-align: top;
}
</style>
<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>
	
		<h:form>
			<h:inputHidden binding="#{projectManagementBackingBean.executionCourseIdHidden}" />
	
			<h:outputFormat value="<h2>#{bundle['link.create.project']}</h2/><hr>" escape="false"/>
			
			<%-- ERROR MESSAGE --%>
			<h:outputText styleClass="error" rendered="#{!empty projectManagementBackingBean.errorMessage}"
				value="#{bundle[projectManagementBackingBean.errorMessage]}<br/>" escape="false" />
			
			<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>
					<h:outputText value="#{bundle['label.name']}: " />
				</h:panelGroup>	
				<h:panelGroup>
					<h:inputText id="name" required="true" maxlength="100" size="20" value="#{projectManagementBackingBean.name}" />			
					<h:message for="name" styleClass="error"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>				
					<h:outputText value="#{bundle['label.beginDate']}: " />
				</h:panelGroup>
				<h:panelGroup>
					<h:inputText id="beginDate" required="true" maxlength="10" size="10" value="#{projectManagementBackingBean.beginProjectDate}" />
					<h:outputText value=" #{bundle['label.date.instructions.small']} &nbsp&nbsp" escape="false"/>
					<h:message for="beginDate" styleClass="error"/>
					<h:inputText id="beginHour" required="true" maxlength="5" size="5" value="#{projectManagementBackingBean.beginProjectHour}" />
					<h:outputText value=" #{bundle['label.hour.instructions']}" />
					<h:message for="beginHour" styleClass="error"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>					
					<h:outputText value="#{bundle['label.endDate']}: " />
				</h:panelGroup>
				<h:panelGroup>
					<h:inputText id="endDate" required="true" maxlength="10" size="10" value="#{projectManagementBackingBean.endProjectDate}" />
					<h:outputText value=" #{bundle['label.date.instructions.small']} &nbsp&nbsp" escape="false" />
					<h:message for="endDate" styleClass="error"/>
					<h:inputText id="endHour" required="true" maxlength="5" size="5" value="#{projectManagementBackingBean.endProjectHour}" />
					<h:outputText value=" #{bundle['label.hour.instructions']}" />
					<h:message for="endHour" styleClass="error"/>
				</h:panelGroup>				
				<h:outputText value="#{bundle['label.description']}: " />
				<h:inputTextarea rows="4" cols="40" value="#{projectManagementBackingBean.description}" />
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>
					<h:outputText value="#{bundle['label.neededFields']}"/>
				</h:panelGroup>
			</h:panelGrid>			
			<h:outputText value="<br/>" escape="false" />
			<h:commandButton action="#{projectManagementBackingBean.createProject}"
				styleClass="inputbutton" value="#{bundle['button.create']}"/>
			<h:commandButton immediate="true" action="projectsIndex"
				styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
		</h:form>
</ft:tilesView>