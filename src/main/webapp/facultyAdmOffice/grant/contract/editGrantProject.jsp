<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.project.edition"/></h2>


<html:messages id="errorMessage" message="true">
	<p class="mtop2"><span class="error0 mtop0">
		<bean:write name="errorMessage" filter="false"/>
	</span></p>
</html:messages>
			
<logic:present name="grantProject">
	<fr:edit name="grantProject" schema="edit.grantProject" action="/manageGrantProject.do?method=prepareManageGrantProject">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
		</fr:layout>
	</fr:edit>
</logic:present>

<logic:notPresent name="grantProject">
	<fr:create schema="edit.grantProject" action="/manageGrantProject.do?method=prepareManageGrantProject"
	type="net.sourceforge.fenixedu.domain.grant.contract.GrantProject">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
		</fr:layout>
	</fr:create>
</logic:notPresent>