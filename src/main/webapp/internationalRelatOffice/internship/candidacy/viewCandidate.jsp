<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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