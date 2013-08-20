<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2 class="mbottom1"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="cancel.request" /></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<bean:write name="message" />
		</span>
	</p>
</html:messages>

<bean:define id="academicServiceRequest" name="academicServiceRequest" scope="request" type="net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest"/>
<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.information"/></strong></p>
<fr:view name="academicServiceRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
  		<fr:property name="rowClasses" value="tdhl1,,,,"/>
  	</fr:layout>
</fr:view>


<logic:present name="academicServiceRequest" property="activeSituation">
	<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.situation"/></strong></p>
	<bean:define id="schema" name="academicServiceRequest" property="activeSituation.class.simpleName" />
	<fr:view name="academicServiceRequest" property="activeSituation" schema="<%= schema.toString() + ".view" %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
			<fr:property name="rowClasses" value="tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>



	<p class="mtop2"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="confirm.cancel"/></strong></p>
		<p class="mbottom025"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="justification"/>:</p>
		<html:form action="<%="/academicServiceRequestsManagement.do?method=cancelAcademicServiceRequest&academicServiceRequestId=" + academicServiceRequest.getExternalId().toString()%>" style="display: inline;">
			<p class="mtop025">
				<logic:present name="failingCondition" scope="request">
					<bean:define id="failingCondition" name="failingCondition" scope="request"/>
					<bean:define id="justification">
						<bean:message key="<%=failingCondition.toString()%>" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</bean:define>
					<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.justification" property="justification" value="<%=justification%>" cols="65" rows="5"/>
				</logic:present>
				<logic:notPresent name="failingCondition" scope="request">
					<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.justification" property="justification" cols="65" rows="5"/>
				</logic:notPresent>
			</p>
			<span>
				<html:submit><bean:message key="button.confirm" bundle="APPLICATION_RESOURCES"/></html:submit>
			</span>
		</html:form>

		<html:form action="<%="/student.do?method=visualizeRegistration&registrationID=" + academicServiceRequest.getRegistration().getExternalId().toString()%>" style="display: inline;">
			<span>
				<html:submit><bean:message key="label.back" bundle="APPLICATION_RESOURCES"/></html:submit>
			</span>
		</html:form>
