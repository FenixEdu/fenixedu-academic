<%@ page language="java" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


		<bean:define id="siteID" name="site" property="externalId"/>

		<bean:define id="currentSchema" name="currentSchema" scope="request" type="java.lang.String"/>

		<ul style="width: 600px;">
		<logic:iterate id="result" name="results" scope="request">
 			<bean:define id="resultId" name="result" property="externalId"/>
				<li>
		 			<fr:view name="result" layout="nonNullValues" schema="<%=currentSchema%>">
		 				<fr:layout>
		 					<fr:property name="htmlSeparator" value=", "/>
		 					<fr:property name="indentation" value="false"/>
		 				</fr:layout>
		 				<fr:destination name="view.publication" path="<%= "/unitSite/showResearchResult.do?method=showPublication&amp;resultId=" + resultId + "&amp;siteID=" + siteID %>"/>
		 			</fr:view> (<html:link target="_blank" page="<%="/bibtexExport.do?method=exportPublicationToBibtex&publicationId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" /></html:link>)
					<p class="mtop025">
					<logic:iterate id="file" name="result" property="resultDocumentFiles">
									<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />  <fr:view name="file" property="displayName"></fr:view> (<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>)
					</logic:iterate>
			 		</p>
	 			</li>
		</logic:iterate>
		</ul>
