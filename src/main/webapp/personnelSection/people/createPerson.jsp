<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.Role"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="create.person.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="role(PERSONNEL_SECTION)">

	<logic:notEmpty name="createdPerson">
		<b><bean:message key="label.invitedPerson.created.with.success" bundle="MANAGER_RESOURCES"/>:</b>
		<fr:view name="createdPerson" schema="ShowExistentPersonsDetailsBeforeCreateInvitedPersons">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="rowClasses" value="listClasses"/>					
			</fr:layout>
		</fr:view>	
	</logic:notEmpty>

	<logic:empty name="createdPerson">
		<b><bean:message key="label.verify.if.invitedPerson.already.exists" bundle="MANAGER_RESOURCES"/></b>
		<fr:form action="/personnelManagePeople.do">
			<fr:edit name="anyPersonSearchBean" id="anyPersonSearchBeanId">
				<input type="hidden" name="method"/>
				<fr:schema type="net.sourceforge.fenixedu.domain.Person$AnyPersonSearchBean" bundle="MANAGER_RESOURCES">
					<fr:slot name="name">
						<fr:property name="size" value="50"/>
					</fr:slot>	
					<fr:slot name="documentIdNumber" />
				</fr:schema>	
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>	
			</fr:edit>			
			<html:submit onclick="this.form.method.value='showExistentPersonsWithSameMandatoryDetails';"><bean:message key="label.search" bundle="MANAGER_RESOURCES" /></html:submit>	
		
		
		<p>
			<logic:notEmpty name="resultPersons">
				<table class="tstyle4">
					<tr>
						<th>
							<bean:message key="label.name" bundle="MANAGER_RESOURCES"/>
						</th>
						<th>
							<bean:message key="label.documentIdNumber" bundle="MANAGER_RESOURCES"/>
						</th>
						<th>
							<bean:message key="label.idDocumentType" bundle="MANAGER_RESOURCES"/>
						</th>
						<th>
							<bean:message key="label.roles" bundle="APPLICATION_RESOURCES"/>
						</th>
					</tr>
					<logic:iterate id="person" name="resultPersons" type="net.sourceforge.fenixedu.domain.Person">
						<tr>
							<td class="listClasses">
								<html:link action="/personnelManagePeople.do?method=viewPerson" paramId="personId" paramName="person" paramProperty="externalId">
									<bean:write name="person" property="name"/>
								</html:link>
								<logic:notEmpty name="person" property="username">
									(<bean:write name="person" property="username"/>)
								</logic:notEmpty>
							</td>
							<td class="listClasses">
								<bean:write name="person" property="documentIdNumber"/>
							</td>
							<td class="listClasses">
								<fr:view name="person" property="idDocumentType"/>
							</td>
							<td class="listClasses">
								<%
									int i = 0;
									for (final Role role : person.getPersonRolesSet()) {
									    final RoleType roleType = role.getRoleType();
									    if (roleType == RoleType.STUDENT || roleType == RoleType.TEACHER || roleType == RoleType.EMPLOYEE || roleType == RoleType.RESEARCHER) {
								%>
											<% if (i++ > 0) { %>
													<br/>
											<% } %>
											<%= role.getRoleType().getLocalizedName() %>
								<%
									    }
									}
								%>
							</td>
						</tr>
					</logic:iterate>
				</table>				
			</logic:notEmpty>
			</p>
			<logic:present name="resultPersons">
				<html:submit onclick="this.form.method.value='prepareCreatePersonFillInformation';"><bean:message key="link.create.person.because.does.not.exist" bundle="MANAGER_RESOURCES" /></html:submit>	
			</logic:present>
			</fr:form>
		
	</logic:empty>	
					
</logic:present>

