<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml />

<em><bean:message key="label.manager.executionCourses" /></em>
<h2><bean:message key="label.execution.course.groupings" bundle="SOP_RESOURCES" /></h2>

<fr:form>
	<fr:edit name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>"
		schema="academicInterval.chooseWithPostBack">
		<fr:destination name="intervalPostBack"
			path="/listExecutionCourseGroupings.do?method=selectExecutionPeriod" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
</fr:form>

<logic:present name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>">
	<p><html:link page="/listExecutionCourseGroupings.do?method=downloadExecutionCourseGroupings"
		paramId="academicInterval" paramName="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>"
		paramProperty="academicInterval.resumedRepresentationInStringFormat">
		<bean:message key="link.downloadExcelSpreadSheet" />
		<html:img border="0" src="<%=request.getContextPath() + "/images/excel.gif"%>" altKey="excel"
			bundle="IMAGE_RESOURCES" />
	</html:link></p>
</logic:present>