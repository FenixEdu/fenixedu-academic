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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.studentStatutes.manage" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>


<h3 class="mtop15 mbottom025"><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<table class="mtop025">
	<tr>
		<td>
			<fr:view name="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		      		<fr:property name="rowClasses" value="tdhl1,,,,"/>
				</fr:layout>
			</fr:view>
		</td>
		<td style="vertical-align: top; padding-left: 10px;">
			<bean:define id="personID" name="student" property="person.externalId"/>
			<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>
</table>


<h3 class="mbottom025"><bean:message key="label.studentStatutes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:empty name="student" property="allStatutes">
	<p><em><bean:message key="label.studentStatutes.unavailable" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="student" property="allStatutes">
	<fr:view name="student" property="allStatutes" schema="student.statutes" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight"/>
			<fr:property name="linkFormat(delete)" value="/studentStatutes.do?method=deleteStatute&statuteId=${studentStatute.externalId}" />
			<fr:property name="key(delete)" value="link.student.statute.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIf(delete)" value="statuteType.explicitCreation"/>
			<fr:property name="contextRelative(delete)" value="true"/> 		
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<bean:define id="studentID" name="student" property="externalId" />
<bean:define id="studentOID" name="student" property="externalId" />
<bean:define id="schemaID" name="schemaName" />
<h3 class="mtop15 mbottom025"><bean:message key="label.addNewStatute" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:edit name="manageStatuteBean" schema="<%= schemaID.toString() %>" action="/studentStatutes.do?method=addNewStatute">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:hidden slot="student" name="student" />
	<fr:destination name="seniorStatutePostBack" path="/studentStatutes.do?method=seniorStatutePostBack"/>
	<fr:destination name="invalid" path="<%="/studentStatutes.do?method=invalid&studentOID=" + studentOID + "&schemaName=" + schemaID.toString()%>"/>
	<fr:destination name="cancel" path="<%="/student.do?method=visualizeStudent&studentID=" + studentID%>" />
</fr:edit>

