<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
<style>
.temp1 {
clear: both;
}
.temp1 li span {
float: left; 
width: 100px;
padding-right: 10px;
}
</style>
	<h:outputText value="<em>#{CompetenceCourseManagement.competenceCourse.name}</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['bibliographicReference']}</h2>" escape="false"/>	
	<h:messages infoClass="infoMsg" errorClass="error0" globalOnly="true" />
	
	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.idInternal}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		<h:outputText value="<p>" escape="false"/>
 		<h:outputLink value="setCompetenceCourseBibliographicReference.faces">
				<h:outputText value="#{bolonhaBundle['createBibliographicReference']}"/>
				<f:param name="action" value="#{CompetenceCourseManagement.action}"/>
				<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.idInternal}"/>
		</h:outputLink>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.bibliographicReferenceID || (!empty CompetenceCourseManagement.bibliographicReferenceID && CompetenceCourseManagement.bibliographicReferenceID != -1)}">
			<h:outputText value="<div class='simpleblock4'>" escape="false"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['new']}</h4>" escape="false" rendered="#{empty CompetenceCourseManagement.bibliographicReferenceID}"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['edit']}</h4>" escape="false" rendered="#{(!empty CompetenceCourseManagement.bibliographicReferenceID && CompetenceCourseManagement.bibliographicReferenceID != -1)}"/>
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['title']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.title']}" id="title" size="50" required="true" value="#{CompetenceCourseManagement.title}"/>
			<h:outputText value=" " escape="false"/>
			<h:message for="title" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['author']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.author']}" id="author" size="50" required="true" value="#{CompetenceCourseManagement.author}"/>
			<h:outputText value=" " escape="false"/>
			<h:message for="author" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['year']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.year']}" id="year" size="3" required="true" value="#{CompetenceCourseManagement.year}"/>
			<h:outputText value=" " escape="false"/>
			<h:message for="year" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['reference']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.reference']}" id="reference" size="50" required="true" value="#{CompetenceCourseManagement.reference}"/>
			<h:outputText value=" " escape="false"/>
			<h:message for="reference" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['url']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.url']}" id="url" size="50" maxlength="100" value="#{CompetenceCourseManagement.url}"/>			
			<h:outputText value=" <span>(#{bolonhaBundle['optional']})</span></p>" escape="false"/>

			<h:outputText value="<p><label>#{bolonhaBundle['type']}:</label>" escape="false"/>	
			<h:selectOneMenu value="#{CompetenceCourseManagement.type}">
				<f:selectItem itemValue="MAIN" itemLabel="#{enumerationBundle['MAIN']}"/>
				<f:selectItem itemValue="SECONDARY" itemLabel="#{enumerationBundle['SECONDARY']}"/>
			</h:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false" />
			<h:panelGroup rendered="#{empty CompetenceCourseManagement.bibliographicReferenceID}">
				<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" value="#{bolonhaBundle['create']}" styleClass="inputbutton"
					action="#{CompetenceCourseManagement.createBibliographicReference}"/>					
			</h:panelGroup>
			<h:panelGroup rendered="#{(!empty CompetenceCourseManagement.bibliographicReferenceID && CompetenceCourseManagement.bibliographicReferenceID != -1)}">
				<h:outputText escape="false" value="<input alt='input.bibliographicReferenceID' id='bibliographicReferenceID' name='bibliographicReferenceID' type='hidden' value='#{CompetenceCourseManagement.bibliographicReferenceID}'/>"/>
				<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" value="#{bolonhaBundle['save']}" styleClass="inputbutton"
					action="#{CompetenceCourseManagement.editBibliographicReference}"/>
			</h:panelGroup>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" value="#{bolonhaBundle['cancel']}" styleClass="inputbutton" immediate="true"
					action="#{CompetenceCourseManagement.cancelBibliographicReference}"/>			
			<h:outputText value="</p></fieldset></div>" escape="false" />
		</h:panelGroup>	
		<h:outputText value="<p>#{CompetenceCourseManagement.bibliographicReferencesCount} #{bolonhaBundle['totalItems']}</p>" escape="false"/>
		<h:outputText value="<h3>#{enumerationBundle['MAIN']}</h3>" escape="false"/>
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
			<h:outputText value="<em>#{bolonhaBundle['noBibliographicReferences']}</em>" escape="false"/>
		</h:panelGroup>
		<h:dataTable style="line-height: 1.45em;" value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
			<h:column>
				<h:panelGroup rendered="#{bibliographicReference.order != CompetenceCourseManagement.bibliographicReferenceID}">
					<h:outputText value="<ul class='nobullet temp1 mbottom05'>" escape="false"/>					
					<h:outputText value="<li><span>#{bolonhaBundle['title']}:</span>" escape="false"/>
					<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{bibliographicReference.url != 'http://'}" escape="false"/>
					<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{bibliographicReference.url == 'http://'}" escape="false"/>
					
					<h:outputText value="<li><span>#{bolonhaBundle['author']}:</span>" escape="false"/>
					<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
					
					<h:outputText value="<li><span>#{bolonhaBundle['year']}:</span>" escape="false"/>
					<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
					
					<h:outputText value="<li><span>#{bolonhaBundle['reference']}:</span>" escape="false"/>
					<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
					
					<h:outputText value="<li><span>#{bolonhaBundle['move.to']}:</span>" escape="false"/>
					<h:commandLink value="#{bolonhaBundle['up']}" action="#{CompetenceCourseManagement.switchBibliographicReferencePosition}">	
						<f:param name="oldPosition" value="#{bibliographicReference.order}"/>
						<f:param name="newPosition" value="#{bibliographicReference.order - 1}"/>
					</h:commandLink>
					<h:outputText value=", " />
					<h:commandLink value="#{bolonhaBundle['down']}" action="#{CompetenceCourseManagement.switchBibliographicReferencePosition}">	
						<f:param name="oldPosition" value="#{bibliographicReference.order}"/>
						<f:param name="newPosition" value="#{bibliographicReference.order + 1}"/>
					</h:commandLink>
					<h:outputText value=", " />
					<h:commandLink value="#{bolonhaBundle['top']}" action="#{CompetenceCourseManagement.switchBibliographicReferencePosition}">	
						<f:param name="oldPosition" value="#{bibliographicReference.order}"/>
						<f:param name="newPosition" value="0"/>
					</h:commandLink>
					<h:outputText value=", " />
					<h:commandLink value="#{bolonhaBundle['end']}" action="#{CompetenceCourseManagement.switchBibliographicReferencePosition}">	
						<f:param name="oldPosition" value="#{bibliographicReference.order}"/>
						<f:param name="newPosition" value="#{CompetenceCourseManagement.bibliographicReferencesCount - 1}"/>
					</h:commandLink>
					<h:outputText value="</li>" escape="false"/>

					<h:outputText value="<li class='mtop05'>" escape="false"/>
					<h:outputLink value="setCompetenceCourseBibliographicReference.faces">
						<h:outputText value="#{bolonhaBundle['edit']}"/>
						<f:param name="bibliographicReferenceID" value="#{bibliographicReference.order}"/>
						<f:param name="action" value="#{CompetenceCourseManagement.action}"/>
						<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.idInternal}"/>
					</h:outputLink>
					<h:outputText value=", " />
					<h:commandLink value="#{bolonhaBundle['delete']}" action="#{CompetenceCourseManagement.deleteBibliographicReference}">	
						<f:param name="bibliographicReferenceIDToDelete" value="#{bibliographicReference.order}"/>
					</h:commandLink>
					<h:outputText value="</li></ul>" escape="false"/>
				</h:panelGroup>
			</h:column>
		</h:dataTable>
		
		<h:outputText value="<h3>#{enumerationBundle['SECONDARY']}</h3>" escape="false"/>
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
			<h:outputText value="<em>#{bolonhaBundle['noBibliographicReferences']}</em><br/>" escape="false"/>
		</h:panelGroup>
		<h:dataTable style="line-height: 1.45em;" value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
			<h:column>
				<h:panelGroup rendered="#{bibliographicReference.order != CompetenceCourseManagement.bibliographicReferenceID}">
					<h:outputText value="<ul class='nobullet temp1 mbottom05'>" escape="false"/>					
					<h:outputText value="<li><span>#{bolonhaBundle['title']}:</span>" escape="false"/>
					<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{bibliographicReference.url != 'http://'}" escape="false"/>
					<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{bibliographicReference.url == 'http://'}" escape="false"/>
					
					<h:outputText value="<li><span>#{bolonhaBundle['author']}:</span>" escape="false"/>
					<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
					
					<h:outputText value="<li><span>#{bolonhaBundle['year']}:</span>" escape="false"/>
					<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
					
					<h:outputText value="<li><span>#{bolonhaBundle['reference']}:</span>" escape="false"/>
					<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
					
					<h:outputText value="<li><span>#{bolonhaBundle['move.to']}:</span>" escape="false"/>
					<h:commandLink value="#{bolonhaBundle['up']}" action="#{CompetenceCourseManagement.switchBibliographicReferencePosition}">	
						<f:param name="oldPosition" value="#{bibliographicReference.order}"/>
						<f:param name="newPosition" value="#{bibliographicReference.order - 1}"/>
					</h:commandLink>
					<h:outputText value=", " />
					<h:commandLink value="#{bolonhaBundle['down']}" action="#{CompetenceCourseManagement.switchBibliographicReferencePosition}">	
						<f:param name="oldPosition" value="#{bibliographicReference.order}"/>
						<f:param name="newPosition" value="#{bibliographicReference.order + 1}"/>
					</h:commandLink>
					<h:outputText value=", " />
					<h:commandLink value="#{bolonhaBundle['top']}" action="#{CompetenceCourseManagement.switchBibliographicReferencePosition}">	
						<f:param name="oldPosition" value="#{bibliographicReference.order}"/>
						<f:param name="newPosition" value="0"/>
					</h:commandLink>
					<h:outputText value=", " />
					<h:commandLink value="#{bolonhaBundle['end']}" action="#{CompetenceCourseManagement.switchBibliographicReferencePosition}">	
						<f:param name="oldPosition" value="#{bibliographicReference.order}"/>
						<f:param name="newPosition" value="#{CompetenceCourseManagement.bibliographicReferencesCount - 1}"/>
					</h:commandLink>
					<h:outputText value="</li>" escape="false"/>

					<h:outputText value="<li class='mtop05'>" escape="false"/>
					<h:outputLink value="setCompetenceCourseBibliographicReference.faces">
						<h:outputText value="#{bolonhaBundle['edit']}"/>
						<f:param name="bibliographicReferenceID" value="#{bibliographicReference.order}"/>
						<f:param name="action" value="#{CompetenceCourseManagement.action}"/>
						<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.idInternal}"/>
					</h:outputLink>
					<h:outputText value=", " />
					<h:commandLink value="#{bolonhaBundle['delete']}" action="#{CompetenceCourseManagement.deleteBibliographicReference}">	
						<f:param name="bibliographicReferenceIDToDelete" value="#{bibliographicReference.order}"/>
					</h:commandLink>
					<h:outputText value="</li></ul>" escape="false"/>
				</h:panelGroup>
			</h:column>
		</h:dataTable>
		<h:outputText value="<br/>" escape="false"/>			
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'add'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}" action="competenceCoursesManagement"/>			
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}" action="editCompetenceCourseMainPage"/>			
		</h:panelGroup>
	</h:form>
</ft:tilesView>