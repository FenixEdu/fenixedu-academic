<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.event.transfer.debt" /></h2>


<logic:messagesPresent message="true">
    <ul class="nobullet list6">
        <html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
            <li><span class="error0"><bean:write name="messages" /></span></li>
        </html:messages>
    </ul>
</logic:messagesPresent>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="transferDebtBean" property="event.person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright mtop05" />
        <fr:property name="rowClasses" value="tdhl1,," />
    </fr:layout>
</fr:view>

<p class="mtop15 mbottom05"><strong><bean:message key="label.payments.details" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
<fr:view name="transferDebtBean" property="event">
    <fr:schema type="org.fenixedu.academic.domain.accounting.Event" bundle="ACADEMIC_OFFICE_RESOURCES">
        <fr:slot name="whenOccured" key="label.IndividualCandidacy.whenCreated" />
        <fr:slot name="createdBy" key="label.responsible" layout="null-as-label"/>
    </fr:schema>
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright mtop05" />
    </fr:layout>
</fr:view>

<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.payments.currentEvents" /></strong></p>
<logic:notEmpty name="entryDTOs">
    <fr:view name="entryDTOs" schema="entryDTO.view">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thcenter thlight tdleft mtop025" />
            <fr:property name="columnClasses" value=",," />
        </fr:layout>
    </fr:view>
</logic:notEmpty>
<logic:empty name="entryDTOs">
    <bean:message key="label.payments.events.noEvents" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:empty>

<bean:define id="personId" name="transferDebtBean" property="event.person.externalId" />
<bean:define id="eventId" name="transferDebtBean" property="event.externalId" />

<fr:edit id="transferInfo" name="transferDebtBean" action="<%="/paymentsManagement.do?method=transferDebt&eventId="+eventId%>">
    <fr:schema type="org.fenixedu.academic.dto.TransferDebtBean" bundle="ACADEMIC_OFFICE_RESOURCES">
        <fr:slot name="creditorSocialSecurityNumber" layout="autoComplete"
                 validator="org.fenixedu.academic.ui.renderers.validators.RequiredAutoCompleteSelectionValidator"
                 key="label.payments.event.creditor.party">
            <fr:property name="size" value="70"/>
            <fr:property name="format" value="${party.socialSecurityNumber} - ${party.name}"/>
            <fr:property name="indicatorShown" value="true"/>
            <fr:property name="provider"
                         value="org.fenixedu.academic.service.services.commons.searchers.SearchExternalScholarshipPartySocialSecurityNumber"/>
            <fr:property name="args" value="slot=socialSecurityNumber"/>
            <fr:property name="minChars" value="1"/>
            <fr:property name="maxCount" value="20"/>
        </fr:slot>
        <fr:slot name="file" key="label.payments.event.creditor.document" bundle="ACADEMIC_OFFICE_RESOURCES">
            <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
                <fr:property name="required" value="true"/>
                <fr:property name="typeMessage" value="label.payments.event.creditor.document.type"/>
                <fr:property name="bundle" value="ACADEMIC_OFFICE_RESOURCES"/>
                <fr:property name="acceptedTypes" value="application/pdf"/>
            </fr:validator>
            <fr:property name="size" value="70"/>
            <fr:property name="fileNameSlot" value="fileName"/>
            <fr:property name="fileSizeSlot" value="fileSize"/>
        </fr:slot>
        <fr:slot name="reason" key="label.payments.event.transfer.reason"/>
    </fr:schema>
    <fr:layout name="tabular" >
        <fr:property name="requiredMarkShown" value="true" />
    </fr:layout>
    <fr:destination name="invalid" path="<%="/paymentsManagement.do?method=prepareTransferDebt&eventId=" + eventId%>"/>
    <fr:destination name="cancel" path="<%="/paymentsManagement.do?method=showEvents&personId=" + personId%>"/>
</fr:edit>
