<%@ page import="pt.ist.fenixframework.DomainObject" %>
<%@ page import="org.fenixedu.academic.domain.Department" %>
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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib prefix="if" uri="http://jakarta.apache.org/struts/tags-logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html:xhtml/>

<h2>Associated Objects</h2>

<h3>Degree Types</h3>
<ul>
    <logic:iterate id="type" name="degreeTypes">
        <li><c:out value="${type.name.content}"/> - <html:link page="/manageAssociatedObjects.do?method=prepareCreateDegreeType&degreeTypeId=${type.externalId}">Edit</html:link><br/>
</li>
    </logic:iterate>
</ul>
<html:link page="/manageAssociatedObjects.do?method=prepareCreateDegreeType">Create</html:link><br/>


<h3>Departments</h3>
<html:link page="/manageAssociatedObjects.do?method=prepareCreateDepartment">Create</html:link><br/>
<logic:present name="departments">
    <ul>
        <logic:iterate id="department" name="departments">
            <li><bean:write name="department" property="name"/> - <html:link
                    page="<%= "/manageAssociatedObjects.do?method=prepareEditDepartment&oid=" + ((Department) department).getExternalId() %>">Edit</html:link><br/>
            </li>
        </logic:iterate>
    </ul>
</logic:present>
<logic:notPresent name="departments">
    There are no departments
</logic:notPresent>


<h3>Administrative Offices</h3>
<html:link page="/manageAssociatedObjects.do?method=prepareAcademicOffice">Create</html:link><br/>
<logic:present name="offices">
    <ul>
        <logic:iterate id="office" name="offices" type="org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice">
            <li><bean:write name="office" property="name.content"/></li>
        </logic:iterate>
    </ul>
</logic:present>
<logic:notPresent name="offices">
    There are no administrative offices
</logic:notPresent>
<h3>Associate Person to Unit</h3>
<html:link page="/manageAssociatedObjects.do?method=prepareAssociatePersonUnit">Create</html:link><br/>

<h3>Create Scientific Area</h3>
<html:link page="/manageAssociatedObjects.do?method=prepareCreateScientificArea">Create Scientific Area</html:link><br/>
<logic:present name="departments">
    <ul>
        <logic:iterate id="department" name="departments">
            <li><bean:write name="department" property="name"/></li>

            <logic:present name="department" property="departmentUnit">
                <bean:define id="depunit" name="department" property="departmentUnit"/>
                <ul>
                    <logic:iterate id="unit" name="depunit" property="subUnits">
                    
                    	<logic:equal name="unit" property="scientificAreaUnit" value="true">
	                        <li><bean:write name="unit" property="name"></bean:write> - <html:link
	                                page="<%= "/manageAssociatedObjects.do?method=prepareCreateCompetenceCourseGroup&oid=" + ((DomainObject) unit)
	                .getExternalId()%>">Create Competence Course Group</html:link></li>
	
	                        <logic:present name="unit" property="subUnits">
	                            <ul>
	                                <logic:iterate id="ccg" name="unit" property="subUnits">
	                                	<logic:equal name="ccg" property="competenceCourseGroupUnit" value="true">
	                                    	<li><bean:write name="ccg" property="name"/></li>
	                                	</logic:equal>
	                                </logic:iterate>
	                            </ul>
	                        </logic:present>
                    	</logic:equal>

                    </logic:iterate>
                </ul>
            </logic:present>

        </logic:iterate>
    </ul>
</logic:present>
<logic:notPresent name="departments">
    There are no departments
</logic:notPresent>


