<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
   	    <h:inputHidden binding="#{functionsManagementBackingBean.linkHidden}"/>
	
		<h:outputText value="#{bundle['label.site.orientation2']}" escape="false"/>	
		<h:outputText value="<br/><br/>" escape="false" />
			
		<h:outputText value="<H2>#{bundle['label.chooseUnit']}</H2>" escape="false"/>		

		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGroup styleClass="infoop">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{functionsManagementBackingBean.person.nome}" escape="false"/>		
		</h:panelGroup>
		<h:outputText value="<br/><br/><br/>" escape="false" />
		
		<h:outputText value="<p><strong>#{bundle['label.units']}</strong>:</p>" escape="false"/>		
					
		<h:outputText value="#{functionsManagementBackingBean.units}" escape="false"/>
						    				
	</h:form>

</ft:tilesView>