<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml />

<html:xhtml />

<fr:form action="/chooseContext.do">
	<fr:edit name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>" schema="degreeContext.choose">
		<fr:destination name="degreePostBack" path="/chooseContext.do?method=choosePostBackToContext" />
		<fr:destination name="yearPostBack" path="/chooseContext.do?method=choosePostBackToContext" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05 mbottom0 thmiddle" />
			<fr:property name="columnClasses" value="width12em,width800px,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
</fr:form>

<fr:form action="/chooseExecutionPeriod.do">
	<fr:edit schema="academicIntervalSelectionBean.choosePostBack"
		name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>">
		<fr:destination name="intervalPostBack" path="/chooseExecutionPeriod.do?method=choose" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop0 thmiddle" />
			<fr:property name="columnClasses" value="width12em,width800px,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
</fr:form>