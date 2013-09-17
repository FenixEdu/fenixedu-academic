<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    <bean:define id="structureUrl">
        <bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    
    <html:link href="<%= institutionUrl %>">
        <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link href="<%= institutionUrl + structureUrl %>">
        <bean:message key="structure" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link page="/department/showDepartments.faces">
        <bean:message key="academic.units" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:define id="unitId" name="unit" property="externalId"/>
    <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>">
        <fr:view name="department" property="nameI18n"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:message key="label.employees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
</div>

<h1>
    <bean:message key="label.employees" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <bean:message key="of.masculine" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <fr:view name="department" property="nameI18n"/>
</h1>

<logic:iterate id="area" name="areas" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit">
	<h2 class="greytxt mtop2">
		<fr:view name="area" property="nameI18n"/>
	</h2>

	<logic:iterate id="_e" name="employees" property="<%= area.getExternalId().toString() %>" type="net.sourceforge.fenixedu.domain.Employee">
		<bean:define id="employee" name="_e" toScope="request"/>
		
		<jsp:include page="department-employee-card.jsp"/>
	</logic:iterate>
</logic:iterate>

<logic:notEmpty name="employeesNoArea">
	<logic:notPresent name="ignoreAreas">
		<h2 class="greytxt mtop2">
			<bean:message key="link.teacher.area.noArea" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>
		</h2>
	</logic:notPresent>

	<logic:present name="ignoreAreas">
		<div class="mtop2"></div>
	</logic:present>
	
	<logic:iterate id="_e" name="employeesNoArea" type="net.sourceforge.fenixedu.domain.Employee">
		<bean:define id="employee" name="_e" toScope="request"/>
		<jsp:include page="department-employee-card.jsp"/>
	</logic:iterate>
</logic:notEmpty>
