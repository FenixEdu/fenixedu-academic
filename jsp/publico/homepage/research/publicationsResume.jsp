<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>



		<bean:define id="currentSchema" name="currentSchema" scope="request" type="java.lang.String"/>
		<ul style="width: 600px;">
		<logic:iterate id="result" name="results" scope="request">
 			<bean:define id="resultId" name="result" property="idInternal"/>
				<li>
		 			<fr:view name="result" layout="nonNullValues" schema="<%=currentSchema%>">
		 				<fr:layout>
		 					<fr:property name="htmlSeparator" value=", "/>
		 					<fr:property name="indentation" value="false"/>
		 				</fr:layout>
		 				<fr:destination name="view.publication" path="<%= "/showResearchResult.do?method=showPublication&amp;resultId=" + resultId + "&amp;homepageID=" + request.getParameter("homepageID")%>"/>
		 			</fr:view> (<html:link target="_blank" page="<%="/bibtexExport.do?method=exportPublicationToBibtex&publicationId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" /></html:link>)
					<p class="mtop025">
					<logic:iterate id="file" name="result" property="resultDocumentFiles">
									<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />  <fr:view name="file" property="displayName"></fr:view> (<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>)
					</logic:iterate>
			 		</p>
	 			</li>
		</logic:iterate>
		</ul>
