<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="java.util.Locale"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>

<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at <bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES" /></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>


<h1><bean:write name="application.name" bundle="CANDIDATE_RESOURCES"/></h1>

<fr:form action='<%= mappingPath + ".do?method=testStorkAttrString" %>'>
	<fr:edit name="attrStringTestBean" id="attr.string.test.bean" visible="false" />

	<fr:edit	name="attrStringTestBean"
				id="attr.string.test.bean.set">
		<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.publico.candidacies.erasmus.ErasmusIndividualCandidacyProcessPublicDA$StorkAttrStringTestBean">
			<fr:slot name="attrList" key="attrList" layout="longText">
				<fr:property name="rows" value="10" />
				<fr:property name="columns" value="100" />
			</fr:slot>
		</fr:schema>
	</fr:edit>
	
	<html:submit>Submit</html:submit>	
</fr:form>
