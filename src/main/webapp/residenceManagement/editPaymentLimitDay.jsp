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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="title.paymentLimitDay" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2>

<bean:define id="monthOID" name="residenceMonth" property="OID"/>

<div class="dinline forminline">
	<fr:form action="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID=" + monthOID%>">
		
		<fr:edit name="priceTable"  schema="configuration.paymentLimitDay" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thmiddle mbottom1 thleft"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		
		<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>

	<fr:form action="<%= "/residenceEventManagement.do?method=manageResidenceEvents&monthOID=" + monthOID%>">
		<html:submit><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
</div>