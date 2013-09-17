
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgram"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.degreeStructure.DegreeCurricularPlanRendererConfig"%>

<%-- postback is invoking action form value --%>

<fr:edit id="rendererConfig" name="rendererConfig" nested="true">

	<fr:schema bundle="APPLICATION_RESOURCES" type="<%= DegreeCurricularPlanRendererConfig.class.getName() %>">

		<%-- execution interval --%>
		<fr:slot name="executionInterval" layout="menu-select-postback" required="true">
			<fr:property name="providerClass" value="<%= DegreeCurricularPlanRendererConfig.ExecutionIntervalProvider.class.getName()  %>" />
			<fr:property name="format" value="${qualifiedName}" />
			<fr:property name="nullOptionHidden" value="true" />
		</fr:slot>

		<%-- organized by --%>
		<fr:slot name="organizeBy" layout="radio-postback">
			<fr:property name="classes" value="liinline nobullet" />
		</fr:slot>

		<%-- show courses --%>
		<fr:slot name="showCourses" layout="radio-postback">
			<fr:property name="classes" value="liinline nobullet" />
		</fr:slot>

		<%-- show rules --%>
		<fr:slot name="showRules" layout="radio-postback">
			<fr:property name="classes" value="liinline nobullet" />
		</fr:slot>

	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>

</fr:edit>
