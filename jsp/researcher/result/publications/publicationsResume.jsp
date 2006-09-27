<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

		<table style="text-align:left" width="100%">
			<bean:define id="currentSchema" name="currentSchema" scope="request" type="java.lang.String"/>
			<logic:iterate id="result" name="results" scope="request">
	 			<bean:define id="resultId" name="result" property="idInternal"/>
				<tr>
			 		<td width="75%" class="listClasses" style="text-align:left">
			 			<fr:view name="result" layout="tabular" schema="<%=currentSchema%>" />
		 			</td>
					<td class="listClasses" style="text-align:center">
				 		<html:link page="<%="/resultPublications/prepareEdit.do?resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.details.link" /></html:link>
			 		</td>
			 		<td class="listClasses" style="text-align:center">
			 			<html:link page="<%="/resultPublications/prepareDelete.do?&resultId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.remove" /></html:link>
			 		</td>
			 		<td class="listClasses" style="text-align:center">
				 		<html:link target="_blank" page="<%="/publications/bibtexManagement.do?method=exportPublicationToBibtex&publicationId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" /></html:link>
			 		</td>
		 		</tr>
			</logic:iterate>
		</table>
