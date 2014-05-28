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

<h2><bean:message key="label.internationalrelations.internship.candidacy.title"
	bundle="INTERNATIONAL_RELATIONS_OFFICE" /></h2>

<html:link action="/internship/internshipCandidacy.do?method=prepareCandidates">
	<bean:message key="link.back" bundle="COMMON_RESOURCES" />
</html:link>

<logic:present name="candidacy">

	<p class="mtop2 mbottom025"><strong> <bean:message
		key="label.internationalrelations.internship.candidacy.section.studentinfo"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
	<fr:view name="candidacy" schema="internship.candidacy.studentinfo.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

	<p class="mtop2 mbottom025"><strong> <bean:message
		key="label.internationalrelations.internship.candidacy.section.personalinfo"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
	<fr:view name="candidacy" schema="internship.candidacy.personalinfo">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

	<p class="mtop2 mbottom025"><strong> <bean:message
		key="label.internationalrelations.internship.candidacy.section.iddocument"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
	<fr:view name="candidacy" schema="internship.candidacy.personaldocuments.bi">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

	<p class="mtop2 mbottom025"><strong> <bean:message
		key="label.internationalrelations.internship.candidacy.section.passport"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
	<fr:view name="candidacy" schema="internship.candidacy.personaldocuments.passport">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

	<p class="mtop2 mbottom025"><strong> <bean:message
		key="label.internationalrelations.internship.candidacy.section.address"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
	<fr:view name="candidacy" schema="internship.candidacy.address">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

	<p class="mtop2 mbottom025"><strong> <bean:message
		key="label.internationalrelations.internship.candidacy.section.contacts"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
	<fr:view name="candidacy" schema="internship.candidacy.contacts">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

	<p class="mtop2 mbottom025"><strong> <bean:message
		key="label.internationalrelations.internship.candidacy.section.destinations"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
	<fr:view name="candidacy" schema="internship.candidacy.destination.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

	<p class="mtop2 mbottom025"><strong> <bean:message
		key="label.internationalrelations.internship.candidacy.section.languages"
		bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
	<fr:view name="candidacy" schema="internship.candidacy.languages">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

	<fr:view name="candidacy" schema="internship.candidacy.previous">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright" />
		</fr:layout>
	</fr:view>

</logic:present>