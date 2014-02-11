<%@page import="net.sourceforge.fenixedu.domain.person.MaritalStatus"%>
<%@page import="net.sourceforge.fenixedu.domain.person.Gender"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.personnelSection.PersonFieldImporter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.personnelSection.PersonImportersContainer"%>
<%@page import="com.google.common.base.Joiner"%>
<%@page import="net.sourceforge.fenixedu.domain.person.IDDocumentType"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.Role"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="create.person.title" bundle="MANAGER_RESOURCES"/></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

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
		<br />

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
			
<logic:present name="personsUploadBean">
	<div class="infoop2">
		<p><bean:message key="personsBatchImport.rules" bundle="MANAGER_RESOURCES"/><p>
	
		<ul>
			<li><bean:message key="personsBatchImport.rules.mandatoryColumns" arg0="<%= Joiner.on(", ").join(PersonImportersContainer.getMandatoryColumns()) %>" bundle="MANAGER_RESOURCES"/></li>
			<li><bean:message key="personsBatchImport.rules.dates" arg0="<%= PersonFieldImporter.DATE_FORMAT %>" bundle="MANAGER_RESOURCES"/></li>
			<li><bean:message key="personsBatchImport.rules.gender" arg0="<%= Joiner.on(", ").join(Gender.getLocalizedNames()) %>" bundle="MANAGER_RESOURCES"/></li>
			<li><bean:message key="personsBatchImport.rules.maritialStatus" arg0="<%= Joiner.on(", ").join(MaritalStatus.getLocalizedNames()) %>" bundle="MANAGER_RESOURCES"/></li>
			<li><bean:message key="personsBatchImport.rules.documentType" arg0="<%= Joiner.on(", ").join(IDDocumentType.getLocalizedNames()) %>" bundle="MANAGER_RESOURCES"/></li>
			<li><bean:message key="personsBatchImport.rules.nationality" bundle="MANAGER_RESOURCES"/></li>
		</ul>
	</div>
	<fr:form action="/personnelManagePeople.do" encoding="multipart/form-data">
		<html:hidden property="method" value="importPersons"/>
		
		<fr:edit id="personsUploadBean" name="personsUploadBean">
			<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES"
				type="net.sourceforge.fenixedu.presentationTier.Action.personnelSection.PersonsUploadBean">
				
				<fr:slot name="inputStream" key="label.file" required="true" bundle="APPLICATION_RESOURCES">
					<fr:property name="fileNameSlot" value="filename"/>
				</fr:slot>
				
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			
		</fr:edit>
		
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
			</html:submit>
		
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" onclick="this.form.method.value='list';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/>	
			</html:cancel>
		</p>
	</fr:form>
</logic:present>
<logic:notPresent name="personsUploadBean">
	<html:link action="/personnelManagePeople.do?method=prepareImportPersons"><bean:message key="personsBatchImport.label.importPersons" bundle="MANAGER_RESOURCES"/></html:link>
</logic:notPresent>
