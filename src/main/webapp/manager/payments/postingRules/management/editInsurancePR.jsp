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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message
	key="label.payments.postingRules.editPostingRule"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<fr:hasMessages type="conversion" for="postingRuleEditor">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message show="label" />:<fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="postingRuleEditorClassName" name="postingRule"
	property="class.simpleName" />

<bean:define id="postingRuleId" name="postingRule"
	property="externalId" />

<fr:edit id="postingRuleEditor" name="postingRule"
	schema="<%=postingRuleEditorClassName + ".edit"%>">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thmiddle thright thlight mtop05" />
	</fr:layout>
	<fr:destination name="invalid"
		path="<%="/postingRules.do?method=prepareEditInsuracePRInvalid&postingRuleId=" + postingRuleId%>" />
	<fr:destination name="success"
		path="<%="/postingRules.do?method=showInsurancePostingRules"%>" />
	<fr:destination name="cancel"
		path="<%="/postingRules.do?method=showInsurancePostingRules"%>" />
</fr:edit>
