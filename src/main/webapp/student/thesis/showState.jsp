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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisPresentationState"%>


<html:xhtml/>

<h2><bean:message key="title.student.thesis.submission"/></h2>
<bean:define id="thesisId" name="thesis" property="externalId"/>

<ul>
    <li>
        <html:link page="/thesisSubmission.do?method=downloadIdentificationSheet">
            <bean:message key="link.student.thesis.identification.download"/>
        </html:link>
    </li>
 	 <li>
     	<html:link page="<%= String.format("/thesisSubmission.do?method=viewOperationsThesis&thesisID=%s",thesisId)%>">
            <bean:message key="link.thesis.operation" bundle="STUDENT_RESOURCES"/>
      	</html:link>
     </li>
</ul>

<div class="infoop2">
    <p>
        <bean:message key="label.student.thesis.confirmed"/>
    </p>
</div>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.details"/></h3>

<fr:view name="thesis" schema="student.thesis.details">

    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="title.student.thesis.submit.abstract"/></h3>

<fr:view name="thesis" schema="student.thesis.details.abstract">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="title.student.thesis.submit.keywords"/></h3>

<fr:view name="thesis" schema="student.thesis.details.keywords">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.declaration"/></h3>

<fr:view name="thesis" schema="student.thesis.details.declaration">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        <fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<html:link page="/thesisSubmission.do?method=viewDeclaration">
    <bean:message key="label.student.thesis.declaration.view"/>
</html:link>

<h3><bean:message key="title.student.thesis.submit.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <bean:message key="label.student.thesis.submit.noDissertation"/>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" schema="student.thesis.file.dissertation">
        <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<h3><bean:message key="title.student.thesis.submit.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <p><em><bean:message key="label.student.thesis.submit.noExtendedAbstract"/></em></p>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" schema="student.thesis.file">
        <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>
