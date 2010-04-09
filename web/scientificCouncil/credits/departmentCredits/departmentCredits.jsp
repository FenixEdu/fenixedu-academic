<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


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
<bean:define id="department" name="departmentCreditsBean" property="department" type="net.sourceforge.fenixedu.domain.Department"/>
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

