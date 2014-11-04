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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message key="title.department.credits"/></h2>

<logic:present name="success"> 
	<logic:equal name="success" value="true"> 
		<span class="success0 mbottom05" >
			<bean:message key="label.department.credits.success" />
		</span> 
	</logic:equal>
	<logic:equal name="success" value="false"> 
		<span class="error0 mbottom05">
			<bean:message key="label.department.credits.error" />
		</span>
	</logic:equal>
</logic:present>

<fr:form action="/departmentCredits.do?method=addRoleDepartmentCredits" >
	<fr:edit id="departmentCreditsBean" name="departmentCreditsBean"
				schema="scientificCouncil.credits.departmentCreditsBean" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5"/>
	    	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="postback" path="/departmentCredits.do?method=showEmployeesByDepartment"/>
		<fr:destination name="invalid" path="/departmentCredits.do?method=showEmployeesByDepartment"/>
	</fr:edit>
	<html:submit><bean:message key="button.submit" /></html:submit>
</fr:form>



<logic:present name="employeesOfDepartment">
<bean:define id="department" name="departmentCreditsBean" property="department" type="org.fenixedu.academic.domain.Department"/>
<h3><bean:message key="title.department.credits.list" name="departmentCreditsBean" arg0="<%= department.getNameI18n().toString() %>"/></h3> 
	<fr:view name="employeesOfDepartment"  schema="scientificCouncil.credits.employee">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value="acenter,,"/>
			
			<fr:property name="link(delete)" value="<%=String.format("/departmentCredits.do?method=removeRoleDepartmentCredits&departmentId=%s",department.getExternalId())%>" />
			<fr:property name="key(delete)" value="label.department.credits.removeRole" />
			<fr:property name="param(delete)" value="externalId/employeeId"/>
		</fr:layout>
	</fr:view>
</logic:present>	

