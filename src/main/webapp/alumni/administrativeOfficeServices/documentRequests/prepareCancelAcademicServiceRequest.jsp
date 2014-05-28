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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2 class="mbottom1"><bean:message bundle="STUDENT_RESOURCES" key="documents.requirement" /></h2>

<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<bean:write name="message" />
		</span>
	</p>
</html:messages>

<bean:define id="academicServiceRequest" name="academicServiceRequest" scope="request" type="net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest"/>
<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
<p class="mbottom025"><strong><bean:message bundle="STUDENT_RESOURCES"  key="request.information"/></strong></p>
<fr:view name="academicServiceRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>	</fr:layout>
  		<fr:property name="rowClasses" value="tdhl1,,,,"/>
</fr:view>


<logic:present name="academicServiceRequest" property="activeSituation">
	<p class="mbottom025"><strong><bean:message bundle="STUDENT_RESOURCES"  key="request.situation"/></strong></p>
	<fr:view name="academicServiceRequest" property="activeSituation" schema="AcademicServiceRequestSituation.view">
		<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
		</fr:layout>
	</fr:view>
</logic:present>



	<p class="mtop2"><strong><bean:message bundle="STUDENT_RESOURCES"  key="confirm.cancel"/></strong></p>
		<p class="mbottom025"><bean:message bundle="STUDENT_RESOURCES"  key="justification"/>:</p>
		<html:form action="<%="/viewDocumentRequests.do?method=cancelAcademicServiceRequest&academicServiceRequestId=" + academicServiceRequest.getExternalId().toString()%>" style="display: inline;">
			<p class="mtop025">
				<logic:present name="failingCondition" scope="request">
					<bean:define id="failingCondition" name="failingCondition" scope="request"/>
					<bean:define id="justification">
						<bean:message key="<%=failingCondition.toString()%>" bundle="STUDENT_RESOURCES"/>
					</bean:define>
					<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.justification" property="justification" value="<%=justification%>" cols="65" rows="5"/>
				</logic:present>
				<logic:notPresent name="failingCondition" scope="request">
					<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.justification" property="justification" cols="65" rows="5"/>
				</logic:notPresent>
			</p>
			<span>
				<html:submit><bean:message key="button.confirm" bundle="STUDENT_RESOURCES"/></html:submit>
			</span>
		</html:form>

		<html:form action="/viewDocumentRequests.do?method=viewDocumentRequests" style="display: inline;">
			<span>
				<html:submit><bean:message key="button.cancel" bundle="STUDENT_RESOURCES"/></html:submit>
			</span>
		</html:form>
