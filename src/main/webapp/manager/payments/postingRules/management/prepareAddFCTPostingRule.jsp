<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="${phd ? 'label.create.pr.external.entity.scholarship.phd' : 'label.create.pr.external.entity.scholarship'}" bundle="MANAGER_RESOURCES"/></h2>
<br />

<fr:edit id="bean" name="bean" action="${phd ? '/phdPostingRules.do?method=addFCTScolarshipPostingRule' : '/postingRules.do?method=addExternalScholarshipPostingRule'}">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:schema bundle="MANAGER_RESOURCES"
		type="org.fenixedu.academic.ui.struts.action.manager.payments.PostingRulesManagementDA$FctScolarshipPostingRuleBean">
		<fr:slot name="startDate" key="label.phd.gratuity.startDate"
			required="true" />
		<fr:slot name="endDate" key="label.phd.gratuity.endDate" />
	</fr:schema>
</fr:edit>
