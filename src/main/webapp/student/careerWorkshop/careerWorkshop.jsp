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
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml />

<h2><bean:message key="label.careerWorkshopApplication.title" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true" property="success">
	<div class="mvert15">
		<span class="success0"> <html:messages id="messages"
				message="true" bundle="APPLICATION_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages> </span>
	</div>
</logic:messagesPresent>

<h3 class="mbottom025"><bean:message key="label.careerWorkshop.openApplicationEvents" bundle="STUDENT_RESOURCES"/></h3>

<logic:messagesPresent message="true" property="error">
	<div class="error0" style="padding: 0.5em;">
	<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.careerWorkshop.achtung" />:</strong></p>
	<ul class="mvert05">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
			<li><span><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	</div>
	<bean:define id="noFurtherAccess" value="true"/>
</logic:messagesPresent>

<logic:empty name="openApplicationEvents" >
    <p><em><bean:message key="label.careerWorkshop.noOpenApplicationEvents" bundle="STUDENT_RESOURCES"/></em></p>
</logic:empty>

<logic:notEmpty name="openApplicationEvents">
    <fr:view name="openApplicationEvents">
        <fr:layout name="tabular">
        	<fr:property name="linkFormat(apply)" value="<%="/careerWorkshopApplication.do?method=presentApplication&eventId=${externalId}"%>"/>
			<fr:property name="order(apply)" value="1" />
			<fr:property name="key(apply)" value="label.manageCareerWorkshop.presentApplication" />
			<fr:property name="bundle(apply)" value="STUDENT_RESOURCES" />
        
        	<fr:property name="sortBy" value="beginDate=desc"/>
            <fr:property name="classes" value="tstyle1 thleft thlight mvert05" />
			<fr:property name="columnClasses" value=",,,,tdclear tderror1" />	    
        </fr:layout>
        <fr:schema type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent" bundle="STUDENT_RESOURCES">
			<fr:slot name="formattedBeginDate" key="label.manageCareerWorkshop.startDate" />
			<fr:slot name="formattedEndDate" key="label.manageCareerWorkshop.endDate"/>
			<fr:slot name="relatedInformation" key="label.manageCareerWorkshop.relatedInformation"/>
		</fr:schema>
    </fr:view>
</logic:notEmpty>


<h3 class="mbottom025"><bean:message key="label.careerWorkshop.pendingForConfirmation" bundle="STUDENT_RESOURCES"/></h3>

<logic:empty name="pendingForConfirmation">
    <p><em><bean:message key="label.careerWorkshop.noPendingForConfirmation" bundle="STUDENT_RESOURCES"/></em></p>
</logic:empty>

<logic:notEmpty name="pendingForConfirmation">
    <fr:view name="pendingForConfirmation">
        <fr:layout name="tabular">
        	<fr:property name="linkFormat(confirm)" value="<%="/careerWorkshopApplication.do?method=presentConfirmation&eventId=${externalId}"%>"/>
			<fr:property name="order(confirm)" value="1" />
			<fr:property name="key(confirm)" value="label.manageCareerWorkshop.presentConfirmation" />
			<fr:property name="bundle(confirm)" value="STUDENT_RESOURCES" />
        
        	<fr:property name="sortBy" value="careerWorkshopApplicationEvent.beginDate=desc"/>
            <fr:property name="classes" value="tstyle1 thleft thlight mvert05" />
			<fr:property name="columnClasses" value=",,,tdclear tderror1" />	    
        </fr:layout>
         <fr:schema type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopConfirmationEvent" bundle="STUDENT_RESOURCES">
			<fr:slot name="beginDate" key="label.manageCareerWorkshop.confirmationStartDate"/>
			<fr:slot name="endDate" key="label.manageCareerWorkshop.confirmationEndDate"/>
		</fr:schema>
    </fr:view>
</logic:notEmpty>