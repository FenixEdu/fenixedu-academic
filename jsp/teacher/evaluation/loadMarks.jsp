<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
	
	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.marksListWithFile']}</h2>" escape="false"/>
	
	<h:outputFormat value="<h3>#{bundle['title.evaluation.manage.marksListWithFile.course']}</h3>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:form enctype="multipart/form-data">
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />



			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.onlineTests.OnlineTest'}">
				<h:outputText value="#{bundle['lable.test']}: "/>
				<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.distributedTest.title}</b> " escape="false"/>
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

</ft:tilesView>
