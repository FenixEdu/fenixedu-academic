<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page
    import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml />

<html:xhtml />

<em><bean:message key="title.resourceAllocationManager.management" /></em>
<h2><bean:message key="title.manage.schedule" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<fr:form action="/chooseExecutionPeriod.do?method=choose">
	<fr:edit schema="academicIntervalSelectionBean.choosePostBack"
		name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.choose" />
	</html:submit>
</fr:form>