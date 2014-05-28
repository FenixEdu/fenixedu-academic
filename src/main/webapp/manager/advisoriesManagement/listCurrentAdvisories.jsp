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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<f:view>

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
					<f:param id="advisoryID1" name="advisoryID" value="#{advisory.externalId}"/>
				</h:commandLink>
			</h:column>
			<h:column>			
				<h:commandLink action="" actionListener="#{advisoriesManagementBackingBean.closeAdvisory}" >
					<h:outputText value="(#{bundle['link.close.execution.period']})"/>
					<f:param id="advisoryID2" name="advisoryID" value="#{advisory.externalId}"/>
				</h:commandLink>
			</h:column>			
		</h:dataTable>	
		
		<h:outputText value="<br/>#{bundle['label.no.activeAdvisories']}" styleClass="error" 
			rendered="#{empty advisoriesManagementBackingBean.allAdvisories}" escape="false"/>
				
	</h:form>
</f:view>