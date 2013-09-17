<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page
    import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml />

<em><bean:message key="title.resourceAllocationManager.management" /></em>
<h2><bean:message key="title.manage.schedule" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<p class="mtop15 mbottom05"><bean:message key="label.chooseDegreeAndYear" />:</p>

<fr:form action="/chooseContext.do?method=choose">

	<fr:edit name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>" schema="degreeContext.choose">
		<fr:destination name="degreePostBack" path="/chooseContext.do?method=choosePostBack" />
		<fr:destination name="yearPostBack" path="/chooseContext.do?method=choosePostBack" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter"
		styleClass="inputbutton">
		<bean:message key="label.next" />
	</html:submit></p>
</fr:form>