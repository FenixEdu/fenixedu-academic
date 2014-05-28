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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />
<h2><bean:message key="label.manage.extraCurricularActivityTypes" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<p><span class="error0"><html:errors bundle="ACADEMIC_OFFICE_RESOURCES" /></span></p>

<fr:view name="activityTypes">
    <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
        type="net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType">
        <fr:slot name="namePt" key="label.extraCurricularActivityTypes.namePt" />
        <fr:slot name="nameEn" key="label.extraCurricularActivityTypes.nameEn" />
    </fr:schema>
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle4 thlight tdcenter mtop05" />

        <fr:property name="link(edit)" value="/manageExtraCurricularActivities.do?method=edit" />
        <fr:property name="param(edit)" value="externalId/activityTypeId" />
        <fr:property name="key(edit)" value="label.edit" />
        <fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES" />
        <fr:property name="order(edit)" value="0" />

        <fr:property name="link(delete)" value="/manageExtraCurricularActivities.do?method=delete" />
        <fr:property name="param(delete)" value="externalId/activityTypeId" />
        <fr:property name="key(delete)" value="label.delete" />
        <fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
        <fr:property name="confirmationKey(delete)" value="label.extraCurricularActivityTypes.confirmDelete" />
        <fr:property name="confirmationBundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
        <fr:property name="order(delete)" value="1" />
    </fr:layout>
</fr:view>

<p><html:link action="/manageExtraCurricularActivities.do?method=create">
    <bean:message key="label.extraCurricularActivityTypes.create" bundle="ACADEMIC_OFFICE_RESOURCES" />
</html:link></p>
