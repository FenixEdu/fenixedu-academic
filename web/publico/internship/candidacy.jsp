<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="label.internationalrelations.internship.candidacy.title" bundle="INTERNATIONAL_RELATIONS_OFFICE" /></h2>

<logic:present name="candidacy">

<style>
th { width: 150px; }
</style>

<div class="infoop2">Consulte o Centro de Inscrição da sua instituição de ensino para mais informações.</div>
<fr:form action="/internship.do?method=submitCandidacy">

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internationalrelations.internship.candidacy.section.studentinfo" bundle="INTERNATIONAL_RELATIONS_OFFICE" />
    </strong></p>
    <fr:edit id="studentinfo" name="candidacy" schema="internship.candidacy.studentinfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form brdnone"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internationalrelations.internship.candidacy.section.personalinfo" bundle="INTERNATIONAL_RELATIONS_OFFICE" />
    </strong></p>
    <fr:edit id="personalinfo" name="candidacy" schema="internship.candidacy.personalinfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internationalrelations.internship.candidacy.section.iddocument" bundle="INTERNATIONAL_RELATIONS_OFFICE" />
    </strong></p>
    <fr:edit id="documents.bi" name="candidacy" schema="internship.candidacy.personaldocuments.bi">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internationalrelations.internship.candidacy.section.passport" bundle="INTERNATIONAL_RELATIONS_OFFICE" />
    </strong></p>
    <fr:edit id="documents.passport" name="candidacy" schema="internship.candidacy.personaldocuments.passport">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internationalrelations.internship.candidacy.section.address" bundle="INTERNATIONAL_RELATIONS_OFFICE" />
    </strong></p>
    <fr:edit id="address" name="candidacy" schema="internship.candidacy.address">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internationalrelations.internship.candidacy.section.contacts" bundle="INTERNATIONAL_RELATIONS_OFFICE" />
    </strong></p>
    <fr:edit id="contacts" name="candidacy" schema="internship.candidacy.contacts">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
	</fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internationalrelations.internship.candidacy.section.destinations" bundle="INTERNATIONAL_RELATIONS_OFFICE" />
    </strong></p>
    <fr:edit id="destination" name="candidacy" schema="internship.candidacy.destination">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <p class="mtop2 mbottom025"><strong>
        <bean:message key="label.internationalrelations.internship.candidacy.section.languages" bundle="INTERNATIONAL_RELATIONS_OFFICE" />
    </strong></p>
    <fr:edit id="languages" name="candidacy" schema="internship.candidacy.languages">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <fr:edit id="previous" name="candidacy" schema="internship.candidacy.previous">
        <fr:layout name="tabular">
            <fr:property name="classes" value="form"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1 nowrap aright"/>
        </fr:layout>
    </fr:edit>

    <input type="hidden" name="method" />
    <p>
        <html:submit onclick="this.form.method.value='submitCandidacy';">
            <bean:message bundle="COMMON_RESOURCES" key="button.next" />
        </html:submit>
    </p>
</fr:form>

</logic:present>

<logic:notPresent name="candidacy">
	<h3>As candidaturas a estágios internacionais estão fechadas</h3>
</logic:notPresent>