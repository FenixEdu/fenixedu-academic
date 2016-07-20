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
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>
	<bean:message key="label.payments.postingRules.external.entity.scholarship.rules" bundle="MANAGER_RESOURCES" />
</h2>
<br/>
<h3>
	<bean:message key="label.payments.postingRules.external.entity.scholarship.rules.phd" bundle="MANAGER_RESOURCES" />
</h3>
<br/>

<html:link action="/phdPostingRules.do?method=prepareAddFCTPostingRule">
	<bean:message key="label.create.pr" bundle="MANAGER_RESOURCES"/>
</html:link>
<br/>
<fr:view name="phdList" >
	<fr:schema bundle="MANAGER_RESOURCES"
			   type="org.fenixedu.academic.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionPR">
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
<br/>


<h3>
	<bean:message key="label.payments.postingRules.external.entity.scholarship.rules.other" bundle="MANAGER_RESOURCES" />
</h3>
<br/>

<html:link action="/postingRules.do?method=prepareAddExternalScholarshipPostingRule">
	<bean:message key="label.create.pr" bundle="MANAGER_RESOURCES"/>
</html:link>
<br/>
<fr:view name="othersList" >
	<fr:schema bundle="MANAGER_RESOURCES" type="org.fenixedu.academic.domain.accounting.events.gratuity.ExternalScholarshipGratuityContributionPR">
		<fr:slot name="startDate"></fr:slot>
		<fr:slot name="endDate"></fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:property name="linkFormat(edit)"
					 value="/postingRules.do?method=prepareEditExternalScholarshipPostingRule&amp;postingRule=${externalId}" />
		<fr:property name="key(edit)" value="label.edit" />
		<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />

		<fr:property name="linkFormat(delete)"
					 value="/postingRules.do?method=deleteExternalScholarshipPostingRule&amp;postingRule=${externalId}" />
		<fr:property name="key(delete)" value="label.delete" />
		<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
	</fr:layout>
</fr:view>
