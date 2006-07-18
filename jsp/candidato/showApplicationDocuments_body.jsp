<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<%@ page import="java.util.Date"%>
<%@ page import="net.sourceforge.fenixedu.util.Data"%>
<%@ page import="java.util.Date"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate.ShowApplicationDocumentDispatchAction"%>
<%@ page
	import="net.sourceforge.fenixedu.domain.ApplicationDocumentType"%>

<span><html:errors /></span>

<h2><bean:message key="label.masterDegree.showApplicationDocuments" /></h2>
<bean:define id="candidateID" name="candidateID" />

<table>

	<tr>
		<td><html:link
			action='<%= "/showApplicationDocuments.do?method=showApplicationDocuments&documentType=CURRICULUM_VITAE&candidateID=" + candidateID %>'>
			<bean:message key="label.masterDegree.showCurriculumVitae" />
		</html:link></td>
	</tr>

	<tr>
		<td><html:link
			action='<%= "/showApplicationDocuments.do?method=showApplicationDocuments&documentType=HABILITATION_CERTIFICATE&candidateID=" + candidateID %>'>
			<bean:message key="label.masterDegree.showHabilitationLetter" />
		</html:link></td>
	</tr>

	<tr>
		<td><html:link
			action='<%= "/showApplicationDocuments.do?method=showApplicationDocuments&documentType=SECOND_HABILITATION_CERTIFICATE&candidateID=" + candidateID %>'>
			<bean:message key="label.masterDegree.showSecondHabilitationLetter" />
		</html:link></td>
	</tr>
	<tr>
		<td><html:link
			action='<%= "/showApplicationDocuments.do?method=showApplicationDocuments&documentType=INTEREST_LETTER&candidateID=" + candidateID %>'>
			<bean:message key="label.masterDegree.showManifestationLetter" />
		</html:link></td>
	</tr>
</table>
