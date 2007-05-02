<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.notNeedToEnrol.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<br/>
<br/>
<fr:form action="/notNeedToEnrolEnrolments.do?method=readNotNeedToEnrol">
	<table>
		<tr>
			<td>
				<fr:edit id="bean" name="bean" 
					type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol.NotNeedToEnrolEnrolmentsBean" 
					schema="notNeedToEnroll.choose.student.number">
					<fr:destination name="invalid" path="/notNeedToEnrolEnrolments.do?method=prepare"/>
				</fr:edit>
			</td>
			<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
				<td>
					<span class="error0"><bean:write name="errMsg" /></span>
				</td>
			</html:messages>
			<td>
				<html:submit><bean:message key="button.submit"/></html:submit>
			</td>
		</tr>
	</table>
</fr:form>
