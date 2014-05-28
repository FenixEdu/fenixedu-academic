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

<logic:present role="role(STUDENT)">
    <h2><bean:message key="link.shift.enrollment.item2" /></h2>
    
    <html:form action="/studentTimeTable.do" target="_blank">
       	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTimeTable"/>
       
       <logic:notEmpty name="registrations">
       		<p class="mtop2">
       			<bean:message  key="label.studentCurricularPlan"/>
            	   	<html:select property="registrationId">
       				<html:options collection="registrations" property="externalId" labelProperty="degreeNameWithDegreeCurricularPlanName"/>
       			</html:select>
      	 	</p>
       
       		<p class="mtop2">
       			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
       				<bean:message key="button.continue" />
       			</html:submit>
       		</p>
       	</logic:notEmpty>
       	<logic:empty  name="registrations">
       		<p class="mvert15"><em><bean:message key="message.no.registration" bundle="STUDENT_RESOURCES"/></em></p>
       	</logic:empty>
    </html:form>
</logic:present>

