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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<script type='text/javascript' src='<%= request.getContextPath() + "/dwr/engine.js" %>'></script>
<script type='text/javascript' src='<%= request.getContextPath() + "/dwr/util.js" %>'></script>
<script type='text/javascript' src='<%= request.getContextPath() + "/dwr/interface/TeacherBridge.js" %>'></script>

<h3><bean:message key="link.inquiries.teachingStaff" bundle="INQUIRIES_RESOURCES" /></h3>
<table>
	<tr class="listClasses-header">
		<th><bean:message key="table.rowname.inquiries.teacher.form.teacher.name" bundle="INQUIRIES_RESOURCES" /></th>
		<th><bean:message key="table.header.inquiries.username" bundle="INQUIRIES_RESOURCES" /></th>
		<th/>
	</tr>
	
	<logic:iterate id="professorship" name="professorships" >
		<tr class="listClasses">
			<td>
				<bean:write name="professorship" property="person.name"/>
			</td>
			<td>
				<bean:write name="professorship" property="person.username"/>
			</td>		
			<td>
				<logic:equal name="professorship" property="responsibleFor" value="true">
					<bean:message key="label.inquiries.responsible" bundle="INQUIRIES_RESOURCES" />
				</logic:equal>				
			</td>
		</tr>
	</logic:iterate>
</table>	

<h3><bean:message key="title.inquiries.nonAffiliatedTeachers" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="INQUIRIES_RESOURCES" /></h3>
<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<table>	
	<tr class="listClasses-header">
		<th><bean:message key="table.rowname.inquiries.teacher.form.teacher.name" bundle="INQUIRIES_RESOURCES" /></th>
		<th><bean:message key="table.header.inquiries.institution" bundle="INQUIRIES_RESOURCES" /></th>		
	</tr>
	
	<logic:iterate id="nonAffiliatedTeacher" name="nonAffiliatedTeachers" >
		<tr class="listClasses">
			<td>
				<bean:write name="nonAffiliatedTeacher" property="name"/>
			</td>
			<td>
				<bean:write name="nonAffiliatedTeacher" property="institutionUnit.name"/>
			</td>							
			<td>
				<bean:define id="deleteLink">/teachingStaff.do?method=removeNonAffiliatedTeacher&executionCourseID=<bean:write name="teachingStaffForm" property="executionCourseID"/></bean:define>
				<html:link action="<%= deleteLink %>" paramId="nonAffiliatedTeacherID" paramName="nonAffiliatedTeacher" paramProperty="externalId">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.remove"/>
				</html:link>				
			</td>							
		</tr>
	</logic:iterate>	
</table>	
</p>
<h3><bean:message key="title.inquiries.insertNonAffiliatedTeacher" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="INQUIRIES_RESOURCES" /></h3>
<table>	
	<html:form action="/teachingStaff"  >
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createNewNonAffiliatedTeacher" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" styleId="executionCourseID" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.nonAffiliatedTeacherID" property="nonAffiliatedTeacherID" />
		
		<tr>
			<td><strong><bean:message key="table.rowname.inquiries.teacher.form.teacher.name" bundle="INQUIRIES_RESOURCES" />:</strong></td>
			<td><input alt="input.nonAffiliatedTeacherName" type="text" name="nonAffiliatedTeacherName" value="" onkeyup="TeacherBridge.readNonAffiliatedTeachersByName(fillTeachers,teacherName.value);" id="teacherName" autocomplete="off" /></td>			
		</tr>
		
		<tr>
			<td colspan="2">
				<table>
					<thead id="teachersResultHeader" class="listClasses-header" />
					<tbody id="teachersResult" class="listClasses" />
				</table>
			
			</td>
		</tr>
		
		<tr>
			<td><strong><bean:message key="table.header.inquiries.institution" bundle="INQUIRIES_RESOURCES" />:</strong></td>			
		</tr>
		<tr>
			<td><strong><bean:message key="label.inquiries.existentInstitution" bundle="INQUIRIES_RESOURCES" />:</strong></td>			
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.nonAffiliatedTeacherInstitutionID" property="nonAffiliatedTeacherInstitutionID">
					<html:option value="0" >[<bean:message key="label.inquiries.choose" bundle="INQUIRIES_RESOURCES" />]</html:option>
					<html:options collection="institutions" property="externalId" labelProperty="name" />
				</html:select>
			</td>
		</tr>		
		<tr>
			<td><strong><bean:message key="label.inquiries.or" bundle="INQUIRIES_RESOURCES" />&nbsp;<bean:message key="label.inquiries.newInstitution" bundle="INQUIRIES_RESOURCES" />:</strong></td>	
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.nonAffiliatedTeacherInstitutionName" property="nonAffiliatedTeacherInstitutionName" /></td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>
				<html:submit><bean:message key="button.inquiries.insertNewTeacher" bundle="INQUIRIES_RESOURCES" /></html:submit>
			</td>
		</tr>
	</html:form>

</table>