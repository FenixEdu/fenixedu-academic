<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.notNeedToEnrol.chooseStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<fr:form action="/notNeedToEnrolEnrolments.do?method=readNotNeedToEnrol">
	<table class="thlight thmiddle mbottom0">
		<tr>
			<td>
				<fr:edit id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.notNeedToEnrol.NotNeedToEnrolEnrolmentsBean" 
					schema="notNeedToEnroll.choose.student.number">
					<fr:destination name="invalid" path="/notNeedToEnrolEnrolments.do?method=prepare"/>
					<fr:layout>
						<fr:property name="classes" value="tstyle5 mvert0"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
			</td>
			<td>
				<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
					<span class="error0"><bean:write name="errMsg" /></span>
				</html:messages>
			</td>
		</tr>
	</table>


<p><html:submit><bean:message key="button.submit"/></html:submit></p>


</fr:form>
