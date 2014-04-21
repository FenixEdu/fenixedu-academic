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
<h3>Associate Person to Unit</h3>
<html:link page="/manageAssociatedObjects.do?method=prepareAssociatePersonUnit">Create</html:link><br/>
<h3>Create empty degree</h3>
<logic:notEmpty name="emptyDegree">
<bean:define id="emptyDegree" type="net.sourceforge.fenixedu.domain.EmptyDegree" name="emptyDegree"></bean:define>
	Empty degree associated with : <%= emptyDegree.getAdministrativeOffice().getAdministrativeOfficeType().getDescription() %></br>
	<html:link page="/manageAssociatedObjects.do?method=prepareEmptyDegree">Change</html:link><br/>
</logic:notEmpty>
<logic:empty name="emptyDegree">
	There is no empty degree.</br>
<html:link page="/manageAssociatedObjects.do?method=prepareEmptyDegree">Create</html:link><br/>
</logic:empty>