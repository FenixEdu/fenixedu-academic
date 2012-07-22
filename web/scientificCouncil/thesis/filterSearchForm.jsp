<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h3><bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.search.filter"/></h3>

<logic:present name="manageSecondCycleThesisSearchBean">
	<fr:form action="<%= "/manageSecondCycleThesis.do?method=filterSearch" %>">
		<fr:edit id="filterSearchForm" name="manageSecondCycleThesisSearchBean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ManageSecondCycleThesisSearchBean"
					bundle="SCIENTIFIC_COUNCIL_RESOURCES">
				<fr:slot name="executionYear" key="label.execution.year" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
					<fr:property name="format" value="${year}"/>
    				<fr:property name="destination" value="listThesis"/>
    			</fr:slot>
    			<fr:slot name="presentationState" key="label.thesis.state" layout="menu-postback" bundle="APPLICATION_RESOURCES" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tdtop thright"/>
				<fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
</logic:present>
