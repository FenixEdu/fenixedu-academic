<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.rectorate" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.rectorateIncoming" /></h2>

<logic:notEmpty name="batches">
    <fr:view name="batches" schema="rectorateSubmission.batchIndex.RECEIVEDFROMACADEMICOFFICE">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 tdcenter thlight mtop05" />
            <fr:property name="sortBy" value="creation=desc" />
            <fr:property name="link(view)" value="/rectorateIncomingBatches.do?method=viewBatch" />
            <fr:property name="key(view)" value="link.rectorateSubmission.viewBatch" />
            <fr:property name="param(view)" value="externalId/batchOid" />
            <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
        </fr:layout>
    </fr:view>
</logic:notEmpty>