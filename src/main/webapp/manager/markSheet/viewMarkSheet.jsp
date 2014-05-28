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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>

<%@ page import="net.sourceforge.fenixedu.util.FenixDigestUtils"%>
<%@ page import="net.sourceforge.fenixedu.util.BundleUtil"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.remove.grades"/></h2>
<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<html:form action="/markSheetManagement.do" styleId="submitForm">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeGrades"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" property="mst" />
	
	<fr:view name="markSheet" schema="degreeAdministrativeOffice.markSheet.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>

	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.students.capitalized"/>:
	<table class="tstyle4 thlight tdcenter">
		<bean:define id="url" name="url" />
		
		<fr:view name="markSheet" property="enrolmentEvaluationsSortedByStudentNumber" schema="markSheet.manager.evaluations">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="evaluationsToRemove" />
				<fr:property name="checkboxValue" value="externalId" />
			</fr:layout>
		</fr:view>
		
	</table>

	<p class="mtop15 mbottom1">
		<bean:define id="mark" name="markSheet" type="net.sourceforge.fenixedu.domain.MarkSheet"/>
		<bean:define id="checksum" value="<%= FenixDigestUtils.getPrettyCheckSum(mark.getCheckSum())%>"/>
		<span class="highlight1">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.checksum"/>: <bean:write name="checksum"/>
		</span>
	</p>
	
	<p class="mtop15">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back"/>
		</html:cancel>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"
			onclick="<%= "askForConfirmation('" +
				BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", "label.remove.grades.confirmationMessage")
				+ "', '" +
				BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", "label.remove.grades.confirmationTitle")
				+ "'); return false;" %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.remove.grades"/>
		</html:submit>
	</p>
	
</html:form>
<script src="../javaScript/alertHandlers.js"></script>
<script src="../javaScript/jquery.alerts.js"></script>

<script type="text/javascript">
function askForConfirmation(messageKey, titleKey)  {
	requestConfirmation("submitForm", messageKey, titleKey);
}
</script>
