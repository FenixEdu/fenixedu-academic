<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate.ShowApplicationDocumentDispatchAction" %>
<%@ page import="net.sourceforge.fenixedu.domain.ApplicationDocumentType"%>
<span><html:errors/></span>

<h2><bean:message key="label.masterDegree.showApplicationDocuments"/></h2>
	<table>
	<%
	java.util.HashMap showDocument = new java.util.HashMap();
	showDocument.put(
		ShowApplicationDocumentDispatchAction.REQUEST_DOCUMENT_TYPE
		,ApplicationDocumentType.CURRICULUM_VITAE);
	pageContext.setAttribute("showDocument",showDocument);
	%>
		<tr>
			<td>
				<html:link action="/showApplicationDocuments.do?method=showApplicationDocuments" name="showDocument"><bean:message key="label.masterDegree.showCurriculumVitae"/></html:link>
			</td>
		</tr>
			<%
			showDocument.clear();
			showDocument.put(
				ShowApplicationDocumentDispatchAction.REQUEST_DOCUMENT_TYPE
				,ApplicationDocumentType.HABILITATION_CERTIFICATE);
			pageContext.setAttribute("showDocument",showDocument);
			%>
				<tr>
					<td>
						<html:link action="/showApplicationDocuments.do?method=showApplicationDocuments" name="showDocument"><bean:message key="label.masterDegree.showHabilitationLetter"/></html:link>
					</td>
				</tr>
			<%
			showDocument.clear();
			showDocument.put(
				ShowApplicationDocumentDispatchAction.REQUEST_DOCUMENT_TYPE
				,ApplicationDocumentType.SECOND_HABILITATION_CERTIFICATE);
			pageContext.setAttribute("showDocument",showDocument);
			%>
				<tr>
					<td>
						<html:link action="/showApplicationDocuments.do?method=showApplicationDocuments" name="showDocument"><bean:message key="label.masterDegree.showSecondHabilitationLetter"/></html:link>
					</td>
				</tr>
	<%
	showDocument.clear();
	showDocument.put(
		ShowApplicationDocumentDispatchAction.REQUEST_DOCUMENT_TYPE
		,ApplicationDocumentType.INTEREST_LETTER);
	pageContext.setAttribute("showDocument",showDocument);
	
%>
		<tr>
			<td>
				<html:link action="/showApplicationDocuments.do?method=showApplicationDocuments" name="showDocument"><bean:message key="label.masterDegree.showManifestationLetter"/></html:link>
			</td>
		</tr>
	</table>