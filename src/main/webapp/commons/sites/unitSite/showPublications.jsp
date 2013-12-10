<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<bean:define id="showAction" name="showAction" scope="request" type="java.lang.String" />
<bean:define id="searchAction" name="searchAction" scope="request" type="java.lang.String" />
<bean:define id="showContextPath" name="showContextPath" scope="request" type="java.lang.String" />
<bean:define id="searchContextPath" name="searchContextPath" scope="request" type="java.lang.String" />

<bean:define id="showMethod" value="&method=showPublications" toScope="request" />
<bean:define id="searchMethod" value="&method=prepareSearchPublication" toScope="request" />
<bean:define id="siteID" name="<%= FilterFunctionalityContext.CONTEXT_KEY%>" property="selectedContainer.externalId"/>
<bean:define id="showArguments" value="<%= "siteID=" + siteID %>" toScope="request" />
<bean:define id="searchArguments" value="<%=  "siteID=" + siteID %>" toScope="request" />

<bean:define id="searchPublicationLabelKey" name="searchPublicationLabelKey" scope="request" type="java.lang.String" />

<p>
	<bean:message key="link.Search" bundle="RESEARCHER_RESOURCES" />: 
	<html:link page="<%= showContextPath + showAction + showArguments + showMethod %>">
		<bean:message key="<%=searchPublicationLabelKey%>" bundle="RESEARCHER_RESOURCES" />
	</html:link> | 
	<html:link	page="<%= searchContextPath + searchAction + searchArguments + searchMethod %>">
		<bean:message key="label.search.publications.ist" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="RESEARCHER_RESOURCES" />
	</html:link>
</p>

<fr:form action="<%= showContextPath  + showAction + showArguments + showMethod %>">

	<fr:edit id="executionYearIntervalBean" name="executionYearIntervalBean" visible="false"/>
	
	<p class="mbottom025 color888"><bean:message key="label.choosen.interval" bundle="RESEARCHER_RESOURCES"/>:</p>
	
	<table class="tstyle5 mtop025 thmiddle">
		<tr>
			<td>
				<bean:message key="label.date.from" bundle="RESEARCHER_RESOURCES"/>:	  
				<fr:edit id="firstYear" name="executionYearIntervalBean" slot="firstExecutionYear">
					<fr:layout name="menu-select">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
						<fr:property name="format" value="${year}"/>
						<fr:property name="defaultText" value="label.undefined"/>
						<fr:property name="key" value="true"/>
						<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
					</fr:layout>
				</fr:edit>
			</td>
			<td>
				<bean:message key="label.date.to" bundle="RESEARCHER_RESOURCES"/>:
				<fr:edit id="finalYear" name="executionYearIntervalBean" slot="finalExecutionYear">
					<fr:layout name="menu-select">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
						<fr:property name="format" value="${year}"/>
						<fr:property name="defaultText" value="label.undefined"/>
						<fr:property name="key" value="true"/>
						<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
					</fr:layout>
				</fr:edit>
			</td>
			<td>
				<bean:message key="label.projectType" bundle="RESEARCHER_RESOURCES"/>:
				<fr:edit id="finalYear" name="executionYearIntervalBean" slot="publicationType">
					<fr:layout name="menu-select">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ResultPublicationTypeProvider"/>
						<fr:property name="defaultText" value="label.all"/>
						<fr:property name="key" value="true"/>
						<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
						<fr:property name="eachLayout" value="default"/>
					</fr:layout>
				</fr:edit>
	
				<html:submit><bean:message key="label.filter" bundle="RESEARCHER_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>

</fr:form>


<jsp:include page="publicationsList.jsp" />