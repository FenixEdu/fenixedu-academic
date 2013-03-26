<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.rectorateSubmission" /></h2>

<logic:notEmpty name="unsent">
    <h3 class="mtop15 mbottom05"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
        key="title.rectorateSubmission.unsent" /></h3>
    <fr:view name="unsent" schema="rectorateSubmission.batchIndex.UNSENT">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 tdcenter thlight mtop05" />
            <fr:property name="sortBy" value="creation=desc" />
            <fr:property name="link(view)" value="/rectorateDocumentSubmission.do?method=viewBatch" />
            <fr:property name="key(view)" value="link.rectorateSubmission.viewBatch" />
            <fr:property name="param(view)" value="externalId/batchOid" />
            <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:notEmpty name="closed">
    <h3 class="mtop15 mbottom05"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
        key="title.rectorateSubmission.closed" /></h3>
    <fr:view name="closed" schema="rectorateSubmission.batchIndex.CLOSED">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 tdcenter thlight mtop05" />
            <fr:property name="sortBy" value="creation=desc" />
            <fr:property name="link(view)" value="/rectorateDocumentSubmission.do?method=viewBatch" />
            <fr:property name="key(view)" value="link.rectorateSubmission.viewBatch" />
            <fr:property name="param(view)" value="externalId/batchOid" />
            <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:notEmpty name="sent">
    <h3 class="mtop15 mbottom05"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
        key="title.rectorateSubmission.sent" /></h3>
    <fr:view name="sent" schema="rectorateSubmission.batchIndex.SENT">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 tdcenter thlight mtop05" />
            <fr:property name="sortBy" value="creation=desc" />
            <fr:property name="link(view)" value="/rectorateDocumentSubmission.do?method=viewBatch" />
            <fr:property name="key(view)" value="link.rectorateSubmission.viewBatch" />
            <fr:property name="param(view)" value="externalId/batchOid" />
            <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:notEmpty name="received">
    <h3 class="mtop15 mbottom05"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
        key="title.rectorateSubmission.received" /></h3>
    <fr:view name="received" schema="rectorateSubmission.batchIndex.RECEIVED">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 tdcenter thlight mtop05" />
            <fr:property name="sortBy" value="creation=desc" />
            <fr:property name="link(view)" value="/rectorateDocumentSubmission.do?method=viewBatch" />
            <fr:property name="key(view)" value="link.rectorateSubmission.viewBatch" />
            <fr:property name="param(view)" value="externalId/batchOid" />
            <fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
        </fr:layout>
    </fr:view>
</logic:notEmpty>