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

	<fr:form action="/internship/internshipCandidacy.do">
		<p class="mtop2 mbottom025"><strong> <bean:message
			key="label.internationalrelations.internship.candidacy.section.studentinfo"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
		<fr:edit id="studentinfo" name="candidacy" schema="internship.candidacy.studentinfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright brdnone" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<p class="mtop2 mbottom025"><strong> <bean:message
			key="label.internationalrelations.internship.candidacy.section.personalinfo"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
		<fr:edit id="personalinfo" name="candidacy" schema="internship.candidacy.personalinfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<p class="mtop2 mbottom025"><strong> <bean:message
			key="label.internationalrelations.internship.candidacy.section.iddocument"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
		<fr:edit id="documents.bi" name="candidacy" schema="internship.candidacy.personaldocuments.bi">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<p class="mtop2 mbottom025"><strong> <bean:message
			key="label.internationalrelations.internship.candidacy.section.passport"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
		<fr:edit id="documents.passport" name="candidacy"
			schema="internship.candidacy.personaldocuments.passport">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<p class="mtop2 mbottom025"><strong> <bean:message
			key="label.internationalrelations.internship.candidacy.section.address"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
		<fr:edit id="address" name="candidacy" schema="internship.candidacy.address">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<p class="mtop2 mbottom025"><strong> <bean:message
			key="label.internationalrelations.internship.candidacy.section.contacts"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
		<fr:edit id="contacts" name="candidacy" schema="internship.candidacy.contacts">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<p class="mtop2 mbottom025"><strong> <bean:message
			key="label.internationalrelations.internship.candidacy.section.destinations"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
		<fr:edit id="destination" name="candidacy" schema="internship.candidacy.destination">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<p class="mtop2 mbottom025"><strong> <bean:message
			key="label.internationalrelations.internship.candidacy.section.languages"
			bundle="INTERNATIONAL_RELATIONS_OFFICE" /> </strong></p>
		<fr:edit id="languages" name="candidacy" schema="internship.candidacy.languages">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<fr:edit id="previous" name="candidacy" schema="internship.candidacy.previous">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value="width12em,,tdclear tderror1 nowrap aright" />
			</fr:layout>
		</fr:edit>

		<input type="hidden" name="method" />
		<p><html:submit onclick="this.form.method.value='candidateEdit';">
			<bean:message bundle="COMMON_RESOURCES" key="button.submit" />
		</html:submit>
        <html:cancel onclick="this.form.method.value='prepareCandidates';">
            <bean:message bundle="COMMON_RESOURCES" key="button.cancel" />
        </html:cancel></p>
	</fr:form>

</logic:present>