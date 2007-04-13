<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<%@ page import="net.sourceforge.fenixedu.util.FenixDigestUtils"%>
<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.viewMarkSheet"/></h2>
<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<html:form action="/markSheetManagement.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choosePrinter"/>
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
		<tr>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.number"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.name"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.evaluationDate"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.grades"/></th>
			<th>&nbsp;</th>
		</tr>
		<bean:define id="url" name="url" />
		<logic:iterate id="enrolmentEvaluation" name="markSheet" property="enrolmentEvaluationsSortedByStudentNumber" type="net.sourceforge.fenixedu.domain.EnrolmentEvaluation">
			<tr>
				<td>
					<bean:write name="enrolmentEvaluation" property="student.number"/>
				</td>
				<td>
					<bean:write name="enrolmentEvaluation" property="student.person.name"/>
				</td>
				<td>
                    <dt:format pattern="dd-MM-yyyy">
						<bean:write name="enrolmentEvaluation" property="examDate.time"/>
                    </dt:format>
				</td>
				<td>
					<bean:write name="enrolmentEvaluation" property="grade"/>
				</td>
				<td>
					<% if(enrolmentEvaluation.getEnrolmentEvaluationState() == net.sourceforge.fenixedu.util.EnrolmentEvaluationState.RECTIFIED_OBJ) { %>
						<html:link action='<%= "/markSheetManagement.do?method=prepareViewRectificationMarkSheet" + url %>' paramId="evaluationID" paramName="enrolmentEvaluation" paramProperty="idInternal">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectified"/>
						</html:link>
					<% } %>
				</td>
			</tr>
		</logic:iterate>
	</table>

	<p class="mtop15 mbottom1">
		<bean:define id="mark" name="markSheet" type="net.sourceforge.fenixedu.domain.MarkSheet"/>
		<bean:define id="checksum" value="<%= FenixDigestUtils.getPrettyCheckSum(mark.getCheckSum())%>"/>
		<span class="highlight1"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.checksum"/>: <bean:write name="checksum"/></span>
	</p>
	
	<p class="mtop15">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back"/></html:cancel>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.print"/></html:submit>
	</p>
	
</html:form>
