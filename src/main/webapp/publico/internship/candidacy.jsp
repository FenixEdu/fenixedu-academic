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