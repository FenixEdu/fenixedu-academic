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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="title.section.viewByYear"/></h2>

<fr:form id="searchForm" action="/viewYear.do?method=showStudents">
	<fr:edit id="sessionBean" name="sessionBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult.ExternalSupervisorViewsBean" bundle="EXTERNAL_SUPERVISION_RESOURCES">
			<fr:slot name="executionYear" layout="menu-select" key="label.selectYear.executionYear" required="true">
				<fr:property name="format" value="${qualifiedName}"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.BolonhaExecutionYearsProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
			<logic:present name="selectProtocol">
				<logic:equal name="selectProtocol" value="true">
					<fr:slot name="protocol" layout="menu-select" key="label.selectYear.agreement" required="true">
						<fr:property name="format" value="${registrationAgreement}"/>
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.RegistrationProtocolsAllowedForUserProvider"/>
						<fr:property name="saveOptions" value="true"/>
					</fr:slot>
				</logic:equal>
			</logic:present>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="button.consult"/>
	</html:submit>
</fr:form>

<logic:present name="hasChosenYear">
	<logic:equal name="hasChosenYear" value="true">
	
		<bean:define id="executionYearId" name="sessionBean" property="executionYear.externalId"/>
		<bean:define id="registrationProtocolId" name="sessionBean" property="protocol.externalId"/>
		<bean:define id="megavisor" name="sessionBean" property="megavisor"/>
		
		<fr:form id="beanForm" action="/viewYear.do?method=exportXLS">
			<fr:edit id="sessionBean" name="sessionBean" visible="false"/>
		</fr:form>
		<p class="mtop15 mbottom1">
			<a href="javascript:var form = document.getElementById('beanForm');form.method.value='exportXLS';form.submit()">
				<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
				<bean:message key="link.lists.xlsFileToDownload" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</a>
		</p>
	
		<fr:view name="sessionBean" property="students">
		
			<fr:layout name="tabular-sortable">
				<fr:property name="linkFormat(view-details)" value="/viewStudent.do?method=showStats&personId=${externalId}"/>
				<fr:property name="order(view-details)" value="1" />
				<fr:property name="key(view-details)" value="link.selectYear.viewStudentDetails" />
				<fr:property name="bundle(view-details)" value="EXTERNAL_SUPERVISION_RESOURCES" />
				
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="istUsername,name, student.number"/>
            	<fr:property name="sortUrl" value="<%= "/viewYear.do?method=showStudents&executionYearId=" + executionYearId.toString() + "&registrationProtocolId=" + registrationProtocolId.toString() + "&megavisor=" + megavisor.toString()%>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "student.number=desc" : request.getParameter("sortBy") %>"/>
            	
				<fr:property name="classes" value="tstyle1 thleft" />
				<fr:property name="headerClasses" value="acenter,acenter,,," />
				<fr:property name="columnClasses" value="acenter,acenter,,,,," />
			</fr:layout>
			
			<fr:schema type="net.sourceforge.fenixedu.domain.Person" bundle="EXTERNAL_SUPERVISION_RESOURCES">
				<fr:slot name="student.number" key="label.selectYear.studentNumber" />
				<fr:slot name="istUsername" key="label.selectYear.istUsername" />
				<fr:slot name="name" key="label.selectYear.name" />
				<fr:slot name="student.activeRegistrations" key="label.selectYear.degreeSigla">
					<fr:property name="eachSchema" value="registration.view-degree-sigla"/>
					<fr:property name="eachLayout" value="values-comma"/>
					<fr:property name="classes" value="nobullet ulindent0 mvert0"/>
				</fr:slot>
			</fr:schema>
			
		</fr:view>
		
	</logic:equal>
</logic:present>