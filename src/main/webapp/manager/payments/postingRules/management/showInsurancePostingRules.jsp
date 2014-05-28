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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message
	key="label.payments.postingRules.postingRuleDetails"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<fr:view name="postingRules" schema="FixedAmountPR.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="linkFormat(edit)"
			value="/postingRules.do?method=prepareEditInsurancePR&postingRuleId=${externalId}" />
		<fr:property name="key(edit)" value="label.edit" />
		<fr:property name="visibleIf(edit)" value="active" />
		<fr:property name="sortBy" value="endDate=desc" />
	</fr:layout>
</fr:view>

<html:link action="/postingRules.do?method=prepare">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
