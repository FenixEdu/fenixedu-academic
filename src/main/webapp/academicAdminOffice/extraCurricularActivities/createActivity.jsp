<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />
<h2><bean:message key="label.manage.extraCurricularActivityTypes" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:create id="createActivityType" action="/manageExtraCurricularActivities.do?method=list"
    type="org.fenixedu.academic.domain.student.curriculum.ExtraCurricularActivityType">
    <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
        type="org.fenixedu.academic.domain.student.curriculum.ExtraCurricularActivityType">
        <fr:slot name="namePt" key="label.extraCurricularActivityTypes.namePt" required="true" />
        <fr:slot name="nameEn" key="label.extraCurricularActivityTypes.nameEn" required="true" />
    </fr:schema>
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle4 thlight tdcenter mtop05" />
    </fr:layout>
</fr:create>