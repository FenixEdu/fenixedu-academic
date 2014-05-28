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

<h2><bean:message key="label.payments.postingRules.degreeCurricularPlan.rulesFor"
	bundle="MANAGER_RESOURCES" /> Bolsas da FCT</h2>
<br />

<html:link
	action="/phdPostingRules.do?method=prepareAddFCTPostingRule">
	<bean:message key="label.create.fct.scolarship.pr" bundle="MANAGER_RESOURCES"/>
</html:link>
<br/>
<fr:view name="list" >
	<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionPR">
		<fr:slot name="startDate"></fr:slot>
		<fr:slot name="endDate"></fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:property name="linkFormat(edit)" value="/postingRules.do?method=prepareEditFCTScolarshipPostingRule&amp;postingRule=${externalId}" />
		<fr:property name="key(edit)" value="label.edit" />
		<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
		
		<fr:property name="linkFormat(delete)" value="/postingRules.do?method=deleteFCTScolarshipPostingRule&amp;postingRule=${externalId}" />
		<fr:property name="key(delete)" value="label.delete" />
		<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
	</fr:layout>
</fr:view>
