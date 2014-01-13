<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>

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

<div class="infoop2">
    <bean:message key="label.search.description" bundle="RESEARCHER_RESOURCES"/> 
</div>

<bean:define id="sotisURL">
    <%= net.sourceforge.fenixedu.util.FenixConfigurationManager.getConfiguration().sotisURL() %>
</bean:define>

<bean:define id="lang">
    <%= org.fenixedu.commons.i18n.I18N.getLocale().toLanguageTag() %>
</bean:define>

<script src="<%= sotisURL %>/js/sotis-embedded.js" data-sotis-use="search" data-sotis-links="yes" data-sotis-lang="<%= lang %>"></script>

<%-- 


<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>

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

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.SearchDSpacePublicationBean" />

<fr:form id="searchForm" action="<%= searchContextPath + searchAction + searchArguments + "&method=searchPublication" %>">
	<fr:hasMessages for="search" type="validation">
		<p><span class="error0"><bean:message key="label.requiredFieldsNotPresent" /></span></p>
	</fr:hasMessages>

	<p class="mbottom025 color888"><bean:message key="label.choosen.keywords" bundle="RESEARCHER_RESOURCES"/>:</p>

	<fr:edit id="search" name="bean" visible="false" />

	<table class="tstyle5 thlight thright thmiddle">
		<logic:iterate id="searchElement" indexId="index" name="bean" property="searchElements">
		<tr>
			<th>
				<logic:equal name="index" value="0">
					<bean:message key="label.searchField"/>:
				</logic:equal>
				
				<logic:notEqual name="index" value="0">
				<fr:edit id="<%="conjunctionType" + index%>" name="searchElement" slot="conjunction">
				<fr:layout>
					<fr:property name="defaultText" value=""/>
				</fr:layout>
				</fr:edit>
				</logic:notEqual>
				
			</th>
			<td>
				<fr:edit id="<%="valueField" + index%>" name="searchElement" slot="queryValue" >
					<fr:layout>
						<fr:property name="size" value="40"/>
					</fr:layout>
				</fr:edit>

				<bean:message key="label.in" bundle="APPLICATION_RESOURCES"/>
				<fr:edit id="<%= "searchTypeField" + index%>" name="searchElement" slot="searchField">
				<fr:layout>
					<fr:property name="excludedValues" value="TYPE, DATE, COURSE, INFORMATIONS"/>
					<fr:property name="sort" value="true"/>
				</fr:layout>
				</fr:edit>
				
				<logic:equal name="index" value="0">
					<div class="switchNone">
					<html:link page="<%= searchContextPath + searchAction + searchArguments + "&amp;method=addNewSearchCriteria" + bean.getSearchElementsAsParameters()  + "&amp;addIndex=" + (index+1) %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:link>			
					<logic:greaterThan name="bean" property="searchElementsSize" value="1">
					 , 
					<html:link page="<%= searchContextPath + searchAction + searchArguments + "&amp;method=removeSearchCriteria" + bean.getSearchElementsAsParameters()  + "&amp;removeIndex=" + index %>"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES"/></html:link>								
					</logic:greaterThan>
					</div>
					<div class="switchInline">
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='" + searchAction + searchArguments + "&amp;method=addNewSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;addIndex=" + (index+1) + "'; getElementById('searchForm').submit();" %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></a>
					<logic:greaterThan name="bean" property="searchElementsSize" value="1">
					 , 
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='" + searchAction + searchArguments + "&amp;method=removeSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index + "'; getElementById('searchForm').submit();" %>"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES"/></a>
					</logic:greaterThan>
					</div>
				</logic:equal>
				<logic:notEqual name="index" value="0">
					<div class="switchNone">
					<html:link page="<%= searchContextPath + searchAction + searchArguments + "&amp;method=addNewSearchCriteria" + bean.getSearchElementsAsParameters() %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:link> , 			
					<html:link page="<%= searchContextPath + searchAction + searchArguments + "&amp;method=removeSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index%>"><bean:message key="link.remove" bundle="APPLICATION_RESOURCES"/></html:link>
					</div>
					<div class="switchInline">
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='" + searchAction + searchArguments + "&amp;method=addNewSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;addIndex=" + (index+1) +  "'; getElementById('searchForm').submit();" %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></a> , 
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='" + searchAction + searchArguments + "&amp;method=removeSearchCriteria" + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index + "'; getElementById('searchForm').submit();"%>"><bean:message key="link.remove" bundle="APPLICATION_RESOURCES"/></a>
					</div>
				</logic:notEqual>
				

			</td>
		</tr>
		</logic:iterate>


		</table>
			<html:submit><bean:message key="label.search" /></html:submit>
	</fr:form>

<logic:present name="bean" property="searchElementsAsParameters">
<logic:notEmpty name="bean" property="searchElementsAsParameters">

<p><bean:message key="label.hitCount" />: <strong><fr:view name="bean" property="totalItems"/></strong></p>
<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 
<cp:collectionPages module="publico" url="<%= searchContextPath + searchAction + searchArguments + "&amp;method=moveIndex&amp;" + bean.getSearchElementsAsParameters() %>" 
	pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="11"/>
</p>
</logic:notEqual>


<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 

<cp:collectionPages module="publico" url="<%= searchContextPath + searchAction + searchArguments + "&amp;method=moveIndex&amp;" + bean.getSearchElementsAsParameters() %>" 
	pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="11"/>
</p>
</logic:notEqual>

</logic:notEmpty>

<logic:empty name="bean" property="searchElementsAsParameters">
	<bean:message key="label.search.noResultsFound" /> 
</logic:empty>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>

--%>