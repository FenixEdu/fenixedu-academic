<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2>Associated Objects</h2>
<h3>Departments</h3>
<html:link page="/manageAssociatedObjects.do?method=prepareCreateDepartment">Create</html:link><br/>
<logic:present name="departments">
<ul>
<logic:iterate id="department" name="departments">
		<li><bean:write name="department" property="name"/></li>
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
<logic:iterate id="office" name="offices" type="net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice">
		<li><bean:message bundle="ENUMERATION_RESOURCES" key="<%= "AdministrativeOfficeType." + office.getAdministrativeOfficeType().toString() %>"/></li>
</logic:iterate>
</ul>
</logic:present>
<logic:notPresent name="offices">
	There are no administrative offices
</logic:notPresent>