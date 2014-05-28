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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="label.payments.postingRules.editPostingRule" bundle="MANAGER_RESOURCES" /></h2>

<br/>

<bean:define id="className" name="postingRule" property="class.simpleName" />
<bean:define id="postingRuleId" name="postingRule" property="externalId" />

<bean:define id="phdProgramId" name="phdProgram" property="externalId" />

<fr:edit id="postingRule" name="postingRule" schema="<%=className + ".edit"%>">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thmiddle thright thlight mtop05" />
	</fr:layout>
	<fr:destination name="success"
		path="<%= "/phdPostingRules.do?method=showPhdProgramPostingRules&phdProgramId=" + phdProgramId %>" />
	<fr:destination name="invalid"
		path="<%= String.format("/phdPostingRules.do?method=editPhdProgramPostingRuleInvalid&postingRuleId=%s&amp;phdProgramId=%s", postingRuleId, phdProgramId) %>" />
	<fr:destination name="cancel"
		path="<%= "/phdPostingRules.do?method=showPhdProgramPostingRules&phdProgramId=" + phdProgramId %>" />
</fr:edit>
