<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

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
