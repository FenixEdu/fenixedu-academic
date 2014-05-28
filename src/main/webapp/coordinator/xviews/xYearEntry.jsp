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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="org.fenixedu.commons.i18n.I18N"%>

<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />


<h2><bean:message bundle="COORDINATOR_RESOURCES" key="title.analysisByExecutionYears"/></h2>

<fr:form id="searchForm" action="<%= "/xYear.do?method=showYearInformation&degreeCurricularPlanID=" + degreeCurricularPlanID.toString() + "&locale=" + I18N.getLocale().getLanguage() %>"> <!-- + "&gwt.codesvr=127.0.0.1:9997" -->
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

