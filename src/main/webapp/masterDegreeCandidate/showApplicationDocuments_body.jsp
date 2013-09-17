<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
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
