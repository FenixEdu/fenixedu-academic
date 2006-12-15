<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['title.showTests']}</h2>" escape="false" />

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />
	
		<h:panelGrid rendered="#{empty evaluationManagementBackingBean.onlineTestList}" >
			<h:outputText value="#{bundle['message.onlineTests.not.scheduled']}" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!empty evaluationManagementBackingBean.onlineTestList}" >
			<h:dataTable value="#{evaluationManagementBackingBean.onlineTestList}" var="onlineTest">
				<h:column>
					<h:outputText value="<b>#{bundle['lable.test']}:</b> " escape="false"/>
					<h:outputText value="#{onlineTest.distributedTest.title}, "/>
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
						<f:param name="evaluationID" value="#{onlineTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}" />
					</h:commandLink>

					<h:outputText value="<b> | </b>" escape="false"/>
					<h:commandLink action="enterPublishMarks">
						<f:param name="evaluationID" value="#{onlineTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.publishMarks']}" />
					</h:commandLink>

					<h:outputText value="</li><ul>" escape="false"/>
					<h:outputText value="<br/>" escape="false"/>
				</h:column>
			</h:dataTable>
		</h:panelGrid>
	</h:form>

</ft:tilesView>
