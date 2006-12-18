<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.search"/></h2>

<logic:equal name="searchType" value="simple">

	<fr:form action="<%= "/publications/search.do?method=searchPublication&searchType=" + request.getAttribute("searchType") %>">
		<fr:hasMessages for="search" type="validation">
			<p>
			<span class="error0"><bean:message key="label.requiredFieldsNotPresent" bundle="RESEARCHER_RESOURCES"/></span>
			</p>
		</fr:hasMessages>

		<fr:edit id="search" name="bean" schema="publicationSearch"> 
			<fr:layout name="flow">
				<fr:property name="classes" value="tstyle5 thright thlight"/>
				<fr:property name="labelExcluded" value="true"/>
				<fr:property name="hideValidators" value="true"/>
			</fr:layout>
		</fr:edit>

		<html:submit><bean:message key="label.search" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</fr:form>

	<p class="mtop025 mbottom15"><html:link page="/publications/search.do?method=prepareSearchPublication&searchType=advanced"><bean:message key="label.search.advanced" bundle="RESEARCHER_RESOURCES"/></html:link> </p>

</logic:equal>

<logic:equal name="searchType" value="advanced">

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
		</html:messages>
	</logic:messagesPresent>
	
	<fr:hasMessages for="editf1" type="validation">
		<span class="error0"><bean:message key="required.searchField" bundle="RESEARCHER_RESOURCES"/></span>
	</fr:hasMessages>
	<fr:hasMessages for="editv1" type="validation">
		<span class="error0"><bean:message key="required.value" bundle="RESEARCHER_RESOURCES"/></span>
	</fr:hasMessages>
		
	<fr:form action="<%= "/publications/search.do?method=searchPublication&searchType=" + request.getAttribute("searchType") %>">
		<fr:edit id="search" name="bean" nested="true" visible="false" />
		<table class="tstyle5">
		<tr>
		<td></td>
		<td>
			<fr:edit id="editf1" name="bean" slot="field1" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator">
				<fr:layout>
					<fr:property name="defaultText" value=""/>
				</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editv1" name="bean" slot="value1" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator">
				<fr:layout>
					<fr:property name="size" value="40"/>
				</fr:layout>
			</fr:edit>
		</td>
		</tr>
		<tr>
		<td>
			<fr:edit id="c1" name="bean" slot="firstConjunction">
			<fr:layout>
				<fr:property name="defaultText" value=""/>
			</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editf2" name="bean" slot="field2">
			<fr:layout>
				<fr:property name="defaultText" value=""/>
			</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editv2" name="bean" slot="value2">
			<fr:layout>
				<fr:property name="size" value="40"/>
			</fr:layout>
			</fr:edit>
		</td>
		</tr>
		<tr>
		<td>
			<fr:edit id="c2" name="bean" slot="secondConjunction">
			<fr:layout>
				<fr:property name="defaultText" value=""/>
			</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editf3" name="bean" slot="field3">
			<fr:layout>
				<fr:property name="defaultText" value=""/>
			</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editv3" name="bean" slot="value3">
			<fr:layout>
				<fr:property name="size" value="40"/>
			</fr:layout>
			</fr:edit>
		</td>
		</tr>	
		</table>
			<html:submit><bean:message key="label.search" bundle="RESEARCHER_RESOURCES"/></html:submit>
		</fr:form>
		
	<p class="mtop025 mbottom15"><html:link page="/publications/search.do?method=prepareSearchPublication&searchType=simple"><bean:message key="label.search.simple" bundle="RESEARCHER_RESOURCES"/></html:link></p>

</logic:equal>

<logic:present name="searchResult">
<logic:notEmpty name="searchResult">

<p><em><bean:message key="label.hitCount" bundle="RESEARCHER_RESOURCES"/>: <strong><fr:view name="totalItems"/></strong></em></p>

<logic:notEqual name="numberOfPages" value="1">
<cp:collectionPages url="<%= 
	"/researcher/publications/search.do?method=moveIndex&searchType=" + request.getAttribute("searchType")  
	+ "&field1=" + request.getAttribute("field1") + "&value1=" + request.getAttribute("value1") 
	+ "&field2=" + request.getAttribute("field2") + "&value1=" + request.getAttribute("value2") 
	+ "&field3=" + request.getAttribute("field3") + "&value1=" + request.getAttribute("value3") 
	+ "&c1=" + request.getAttribute("c1") + "&c2=" + request.getAttribute("c2") %>" 
	pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>
</logic:notEqual>
<ul>
<logic:iterate id="result" name="searchResult" type="net.sourceforge.fenixedu.domain.research.result.ResearchResult">
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="schema" name="result" property="schema" type="java.lang.String"/>
	
	<li class="mtop1">
		<fr:view name="result" layout="nonNullValues" schema="<%= schema %>">
			<fr:layout>
				<fr:property name="classes" value="mbottom025"/>
				<fr:property name="htmlSeparator" value=", "/>
				<fr:property name="indentation" value="false"/>
			</fr:layout>
		</fr:view>
		(<html:link target="_blank" page="<%="/publications/bibtexManagement.do?method=exportPublicationToBibtex&publicationId=" + resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" /></html:link>)

			<%-- <p class="mvert0" style="color: #777;"><bean:message key="label.files" bundle="RESEARCHER_RESOURCES"/>:</p> --%>
			<ul class="nobullet mvert05" style="color: #777;">
				<logic:iterate id="file" name="result" property="resultDocumentFiles">
					<li class="mvert025"><img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="icon_file" bundle="IMAGE_RESOURCES"/>">
						<fr:view name="file" property="displayName"/> 
						(<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>),  
						<fr:view name="file" property="size" layout="fileSize"/>, 
						<bean:message key="label.fileAvailableFor" bundle="RESEARCHER_RESOURCES"/>:
						<em><fr:view name="file" property="fileResultPermittedGroupType"/></em>
					</li>
				</logic:iterate>
			</ul>
	</li>
</logic:iterate>
</ul>

<logic:notEqual name="numberOfPages" value="1">
<cp:collectionPages url="<%= 
	"/researcher/publications/search.do?method=moveIndex&searchType=" + request.getAttribute("searchType")  
	+ "&field1=" + request.getAttribute("field1") + "&value1=" + request.getAttribute("value1") 
	+ "&field2=" + request.getAttribute("field2") + "&value1=" + request.getAttribute("value2") 
	+ "&field3=" + request.getAttribute("field3") + "&value1=" + request.getAttribute("value3") 
	+ "&c1=" + request.getAttribute("c1") + "&c2=" + request.getAttribute("c2") %>" 
	pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>
</logic:notEqual>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
</logic:notEmpty>

<logic:empty name="searchResult">
	<bean:message key="label.search.noResultsFound" bundle="RESEARCHER_RESOURCES"/> 
</logic:empty>
</logic:present>