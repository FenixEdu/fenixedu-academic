<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">

	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>

	<h:form>	

		<h:outputText value="<h2>#{bundle['title.manage.advisories']}</h2><br/>" escape="false" />
	
		<h:outputText styleClass="error" rendered="#{!empty advisoriesManagementBackingBean.errorMessage}"
				value="#{bundle[advisoriesManagementBackingBean.errorMessage]}<br/>" escape="false"/>
			
		<h:outputLink value="#{advisoriesManagementBackingBean.contextPath}/manager/manageAdvisories.do?method=prepare" >
			<h:outputText value="#{bundle['link.new.advisory']}" />
		</h:outputLink>
	
		<h:outputText value="<br/><br/><h3>#{bundle['label.advisories.list']}</h3>" escape="false" />
		 
		<h:dataTable value="#{advisoriesManagementBackingBean.allAdvisories}" var="advisory"
			headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty advisoriesManagementBackingBean.allAdvisories}">
		
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.from']}" />
				</f:facet>	
				<h:outputText value="#{advisory.sender}"/>
			</h:column>			
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.subject']}" />
				</f:facet>	
				<h:outputText value="#{advisory.subject}"/>
			</h:column>			
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.beginDate']}" />
				</f:facet>					
				<h:outputFormat value="{0, date, dd/MM/yyyy HH:mm}">
					<f:param value="#{advisory.created}"/>
				</h:outputFormat>
			</h:column>				
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.expirationDate2']}" />
				</f:facet>					
				<h:outputFormat value="{0, date, dd/MM/yyyy HH:mm}">
					<f:param value="#{advisory.expires}"/>
				</h:outputFormat>
			</h:column>					
			<h:column>			
				<h:commandLink action="prepareEditAdvisory" >
					<h:outputText value="(#{bundle['label.edit']})"/>
					<f:param id="advisoryID1" name="advisoryID" value="#{advisory.idInternal}"/>
				</h:commandLink>
			</h:column>
			<h:column>			
				<h:commandLink action="" actionListener="#{advisoriesManagementBackingBean.closeAdvisory}" >
					<h:outputText value="(#{bundle['link.close.execution.period']})"/>
					<f:param id="advisoryID2" name="advisoryID" value="#{advisory.idInternal}"/>
				</h:commandLink>
			</h:column>			
		</h:dataTable>	
		
		<h:outputText value="<br/>#{bundle['label.no.activeAdvisories']}" styleClass="error" 
			rendered="#{empty advisoriesManagementBackingBean.allAdvisories}" escape="false"/>
				
	</h:form>
</ft:tilesView>