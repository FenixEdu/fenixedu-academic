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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

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