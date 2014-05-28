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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="edit.person.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="(role(MANAGER) | role(OPERATOR))">
		
	<fr:form action="/editPerson.do?method=searchPersonToEdit">		
		<fr:edit name="personBean" id="personBeanID" schema="SearchPersonAttributes">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>	
		</fr:edit>			
		<html:submit><bean:message key="label.search" bundle="MANAGER_RESOURCES" /></html:submit>	
	</fr:form>
	
	<p>
		<logic:notEmpty name="resultPersons">			
			<fr:view name="resultPersons" schema="ShowExistentPersonsDetailsBeforeCreateInvitedPersons" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4"/>
					<fr:property name="rowClasses" value="listClasses"/>					
																
		   			<fr:property name="link(edit)" value="/editPerson.do?method=prepareEditPerson"/>
		            <fr:property name="param(edit)" value="externalId/personID"/>
			        <fr:property name="key(edit)" value="link.edit"/>
		            <fr:property name="bundle(edit)" value="MANAGER_RESOURCES"/>
		            <fr:property name="order(edit)" value="0"/>		         					
				</fr:layout>			
			</fr:view>				
		</logic:notEmpty>			
	</p>

</logic:present>