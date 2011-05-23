<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />


<h2><bean:message bundle="COORDINATOR_RESOURCES" key="title.analysisByExecutionYears"/></h2>

<fr:form id="searchForm" action="<%= "/xYear.do?method=showYearInformation&degreeCurricularPlanID=" + degreeCurricularPlanID.toString() + "&gwt.codesvr=127.0.0.1:9997" %>"> <!-- + "&gwt.codesvr=127.0.0.1:9997" -->
	<fr:edit id="searchFormBean" name="searchFormBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.xviews.YearViewBean" bundle="COORDINATOR_RESOURCES">
			<fr:slot name="executionYear" layout="menu-select" key="label.executionYear" required="true">
				<fr:property name="format" value="${qualifiedName}"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsForDegreeCurricularPlanProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="COORDINATOR_RESOURCES" key="button.show"/>
	</html:submit>
</fr:form>

