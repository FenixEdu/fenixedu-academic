<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present role="MANAGER,OPERATOR">

	<h2><bean:message key="label.manage.department.degrees" bundle="MANAGER_RESOURCES"/></h2>

	<hr />
	<br />

	<fr:form action="/manageDepartmentDegrees.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="associate"/>

		<fr:edit id="departmentDegreeBean" name="departmentDegreeBean"
				schema="net.sourceforge.fenixedu.domain.Department.DepartmentDegreeBean" >
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>
		</fr:edit>

		<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
	</fr:form>

	<logic:present name="departmentDegreeBean" property="department">
		<br/>
		<br/>
		<bean:define id="url" type="java.lang.String">/manageDepartmentDegrees.do?method=remove&departmentID=<bean:write name="departmentDegreeBean" property="department.externalId"/></bean:define>
		<fr:view name="departmentDegreeBean" property="department.degrees"
				schema="net.sourceforge.fenixedu.domain.Degree.List.For.Department.Association">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,,"/>
		        <fr:property name="sortBy" value="tipoCurso=asc,name=asc"/>

				<fr:property name="linkFormat(remove)" value="<%= url + "&degreeID=${externalId}" %>"/>
				<fr:property name="key(remove)" value="label.remove"/>
			</fr:layout>
		</fr:view>		
	</logic:present>

</logic:present>
