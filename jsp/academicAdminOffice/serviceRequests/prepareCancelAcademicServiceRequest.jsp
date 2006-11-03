<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="cancel.request" /></h2>

<hr style="margin-bottom: 2em;"/>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<bean:write name="message" />
		</span>
	</p>
</html:messages>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.information"/></strong>
<bean:define id="academicServiceRequest" name="academicServiceRequest" scope="request" type="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest"/>
<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
<fr:view name="academicServiceRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<br/><br/>

<logic:present name="academicServiceRequest" property="activeSituation">
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.situation"/></strong>
	<fr:view name="academicServiceRequest" property="activeSituation" schema="AcademicServiceRequestSituation.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value="listClasses,," />
		</fr:layout>
	</fr:view>
</logic:present>

<p>
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="confirm.cancel"/></strong>
	<table>
		<tr>
			<th class="listClasses">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="justification"/>:
			</th>
			<td>
				<html:form action="<%="/academicServiceRequestsManagement.do?method=cancelAcademicServiceRequest&academicServiceRequestId=" + academicServiceRequest.getIdInternal().toString()%>">
				<logic:present name="failingCondition" scope="request">
					<bean:define id="failingCondition" name="failingCondition" scope="request"/>
					<bean:define id="justification">
						<bean:message key="<%=failingCondition.toString()%>" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</bean:define>
					<html:textarea property="justification" value="<%=justification%>" cols="65" rows="5"/>
				</logic:present>
				<logic:notPresent name="failingCondition" scope="request">
					<html:textarea property="justification" cols="65" rows="5"/>
				</logic:notPresent>
			</td>
		</tr>
		<tr>
			<td>
				<html:submit styleClass="inputbutton"><bean:message key="button.confirm" bundle="APPLICATION_RESOURCES"/></html:submit>		
				</html:form>
			</td>
			<td>
				<html:form action="<%="/student.do?method=visualizeRegistration&registrationID=" + academicServiceRequest.getRegistration().getIdInternal().toString()%>">
					<html:submit styleClass="inputbutton"><bean:message key="cancel" bundle="APPLICATION_RESOURCES"/></html:submit>
				</html:form>
			</td>
		</tr>
	</table>
</p>
