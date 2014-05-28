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

<logic:present role="role(MANAGER)">

<h2><bean:message key="title.personal.ingression.data.viewer.student.raides.data.view" bundle="GEP_RESOURCES" /></h2>

<p><strong><bean:message key="message.personal.ingression.data.viewer.search.student.with.criteria" bundle="GEP_RESOURCES" /></strong></p>

<fr:form action="/personalIngressionDataViewer.do?method=findStudents">
	<fr:edit id="chooseStudentBean" name="chooseStudentBean" visible="false" />
	
	<fr:edit id="chooseStudentBean-choose" name="chooseStudentBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.gep.student.candidacy.personal.ingression.data.ChooseStudentBean" bundle="GEP_RESOURCES">
			<fr:slot name="number" key="label.personal.ingression.data.viewer.search.student.number" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.NumberValidator" />
			</fr:slot>
			<fr:slot name="username" key="label.personal.ingression.data.viewer.search.student.username" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" />
			<fr:slot name="documentId" key="label.personal.ingression.data.viewer.search.document.id" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdclear error1" />
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="link.search" bundle="APPLICATION_RESOURCES" /></html:submit>
</fr:form>

<logic:present name="students">

	<logic:empty name="students">
		<p><em><bean:message key="message.personal.ingression.data.viewer.student.not.found" bundle="GEP_RESOURCES" /></em></p>
	</logic:empty>
	
	<logic:notEmpty name="students">
	
		<fr:view name="students">
			<fr:schema type="net.sourceforge.fenixedu.domain.student.Student" bundle="GEP_RESOURCES">
				<fr:slot name="number" key="label.personal.ingression.data.viewer.student.number" />
				<fr:slot name="person.name" key="label.personal.ingression.data.viewer.student.name" />
			</fr:schema>
		</fr:view>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			
			<fr:link name="view" 
				link="/personalIngressionDataViewer.do?method=viewStudent&studentId=${externalId}" 
				label="link.view,APPLICATION_RESOURCES" />
					
		</fr:layout>
	</logic:notEmpty>

</logic:present>

</logic:present>
