<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="org.apache.struts.action.ActionMessages"%>

<jsp:include page="authorizationsScripts.jsp"/>

		<header>
			<h2><bean:message key="label.authorizations" bundle="ACADEMIC_OFFICE_RESOURCES" />
			<span><bean:write name="authorizationsBean" property="operation.localizedName"/>
			</span>
			</h2>
		</header>
		
		<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>" >
			<p>
				<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
			</p>
		</html:messages>

		<div id="all">
		<div id="main" class="col-lg-8">

			<section id="authorizations">
			
				<logic:equal value="false" name="authorizationsBean" property="hasNewObject">
					<fr:form action="/authorizations.do?method=addNewAuthorization">
						${csrf.field()}
						<fr:edit id="authorizationsBean" name="authorizationsBean" visible="false" />
						<html:submit value="Criar nova Autorização"/>
					</fr:form>
				</logic:equal>
				
				<br />
				
				<br />
				
				
				
				<div id="authorizationList">

				<logic:iterate id="group" name="authorizationsBean" property="groups">

				<bean:define id="newObject" name="group" property="newObject" type="java.lang.Boolean"/>
				
				<bean:define id="id" name="group" property="id" type="java.lang.String" />
				
				<div class="edit-authorizations">
				<fr:form action="/authorizations.do">
				${csrf.field()}
				<html:hidden property="method" value="manageOperation"/>

				<fr:edit id="authorizationsBean" name="authorizationsBean" visible="false" />

				<div id="period" class="authorization period <%= newObject ? "newObject" : "" %>">
					<header id="header">
						<h4>
						<logic:notPresent name="group" property="party">
							<bean:message key="label.new.authorization" bundle="ACADEMIC_OFFICE_RESOURCES"/>
						</logic:notPresent>
						<logic:present name="group" property="party">
							<bean:write name="group" property="party.name"/>
						</logic:present>
						</h4>
						<span class="saveButton"><i><bean:message key="label.authorizations.unsavedChanges" bundle="ACADEMIC_OFFICE_RESOURCES"/></i></span>
						<a href="javascript:void(0);" class="edit-auth"><img src="../images/iconEditOff.png" /> <bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></a>
					</header>

					<div class="authorization-edit">
						<fr:edit name="group">
							<fr:schema type="org.fenixedu.academic.ui.struts.action.academicAdministration.AuthorizationGroupBean" bundle="ACADEMIC_OFFICE_RESOURCES">
								<fr:slot name="party" layout="autoComplete" key="label.academicAdministration.authorizations.member" validator="org.fenixedu.academic.ui.renderers.validators.RequiredAutoCompleteSelectionValidator">
									<fr:property name="size" value="50" />
									<fr:property name="labelField" value="presentationName" />
									<fr:property name="indicatorShown" value="true" />
									<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchAllActiveParties" />
									<fr:property name="args" value="slot=name,size=50,internal=true" />
									<fr:property name="minChars" value="4" />
								</fr:slot>
							</fr:schema>
						</fr:edit>
						<fieldset class="botoes">
							<html:button value="<%= newObject ? "Criar" : "Confirmar" %>" styleClass="confirmar" 
							onclick="<%= newObject ? "createAuthorization($(this));" : "editAuthorization($(this), " + id + ");"%>" 
							property="create" />
							
							<html:submit value="Cancelar" styleClass="cancelar" 
								onclick="<%= newObject ? "reload(); return false;" : 
								    					 "javascript:$(this).parents().eq(1).hide(); return false;" %>" property="removeNewAuthorization"/>
						</fieldset>
						
						<logic:equal name="newObject" value="false">
						<div class="links-authorization">
							<a href="#" class="eliminar" onclick="<%= "deleteAuthorization($(this)," + id + ")"%>" style="color: red"><bean:message key="label.delete" bundle="APPLICATION_RESOURCES"/></a>
						</div>
						</logic:equal>
					</div>

					<table width="100%" class="small">
					<tr>
					<td width="50%" align="left" valign="top" id="programs">
						<div align="center"><b style="color: #848484"><bean:message key="label.degrees" bundle="APPLICATION_RESOURCES"/></b></div>
						<ul class="courses-list">
							<logic:iterate id="program" name="group" property="programs">
								<li>									
								<div id="oid" style="display:none"><bean:write name="program" property="oid"/></div>
								<bean:write name="program" property="presentationName"/> 
								<img src="../images/iconRemoveOff.png" alt="remove"/>
								</li>
							</logic:iterate>
						</ul>
					</td>
					<td width="50%" align="left" class="separator" valign="top" id="offices">
						<div align="center"><b style="color: #848484">
											<bean:message key="label.offices" bundle="ACADEMIC_OFFICE_RESOURCES" /></b></div>
						<ul class="offices-list">
							<logic:iterate id="office" name="group" property="offices">
								<li>									
								<div id="oid" style="display:none"><bean:write name="office" property="oid"/></div>
								<bean:write name="office" property="unit.name"/> 
								<img src="../images/iconRemoveOff.png" alt="remove"/>
								</li>
							</logic:iterate>
						</ul>
					</td>
					</tr>
					</table>
					<ul class="placeholder-tip">
						<li>
						<html:submit value="Descartar Alterações" styleClass="btn btn-danger saveButton" onclick="reload(); return false;"/>
						<html:button value="Guardar Alterações" styleClass="btn btn-success saveButton" onclick="<%= "editAuthorizationPrograms($(this).parents().eq(3), " + id + ");" %>" 
								     property="saveChanges" style="margin-right: 8px;"/>
						</li>
					</ul>
				</div>
				
				</fr:form>
				</div>
				
				</logic:iterate>				
				
				<div class="edit-authorizations">
					<div id="period" class="unit period">
						<header id="header" align="center">
							<h4>Pessoas com acesso</h4>
						</header>
						<ul style="display: none" class="small">
							<logic:iterate id="user" name="authorizationsBean" property="members">
								<li><bean:write name="user" property="profile.displayName"/>
									<span style="float: right"><bean:write name="user" property="username"/></span>
								</li>
							</logic:iterate>
						</ul>
					</div>
				</div>

				</div>
			
			</section>
		</div><!--main-->
		
		
		<div class="col-lg-4">
			<div class="panel-group" id="cursos_acc" data-spy="affix" data-offset-top="200">
			
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a data-toggle="collapse" data-parent="#cursos_acc" data-target="#collapseOne" href="#">
								<bean:message key="portal.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" />
							</a>
						</h3>
					</div>
					<div id="collapseOne" class="panel-collapse collapse in">
						<div class="panel-body">
							<logic:iterate id="office" name="authorizationsBean" property="administrativeOffices" 
										   type="org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice">
								<div class="draggable_course office">
									<div id="oid" style="display:none"><bean:write name="office" property="oid"/></div>
									<div id="presentationName" style="display:none"><bean:write name="office" property="unit.name"/></div>
									<div id="name"><bean:write name="office" property="unit.name"/></div>
								</div>
							</logic:iterate>
						</div>
					</div>
				</div>
		
				<logic:iterate id="degreeType" name="authorizationsBean" property="degreeTypes" type="org.fenixedu.academic.domain.degree.DegreeType">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a data-toggle="collapse" data-parent="#cursos_acc" data-target="#collapse${degreeType.externalId}" href="#">
								<bean:write name="degreeType" property="name.content" bundle="ACADEMIC_OFFICE_RESOURCES"/>
							</a>
						</h3>
					</div>
					<div id="collapse${degreeType.externalId}" class="panel-collapse collapse">
						<div class="panel-body">
							<logic:iterate id="degree" name="authorizationsBean" property="degrees">
								<logic:equal value="${degreeType}" name="degree" property="degreeType">
										<div class="draggable_course degree">
											<div id="oid" style="display:none"><bean:write name="degree" property="oid"/></div>
											<div id="presentationName" style="display:none"><bean:write name="degree" property="presentationName"/></div>
											<div id="name"><bean:write name="degree" property="name"/></div>
										</div>
								</logic:equal>
							</logic:iterate>
						</div>
					</div>
				</div>
				</logic:iterate>

				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a data-toggle="collapse" data-parent="#cursos_acc" data-target="#collapseTwo" href="#">
								<bean:message key="title.phd.programs" bundle="PHD_RESOURCES"/>
							</a>
						</h3>
					</div>
					<div id="collapseTwo" class="panel-collapse collapse">
						<div class="panel-body">
							<logic:iterate id="program" name="authorizationsBean" property="phdPrograms" 
										   type="org.fenixedu.academic.domain.phd.PhdProgram">
								<div class="draggable_course degree">
									<div id="oid" style="display:none"><bean:write name="program" property="oid"/></div>
									<div id="presentationName" style="display:none"><bean:write name="program" property="presentationName"/></div>
									<div id="name"><bean:write name="program" property="name"/></div>
								</div>
							</logic:iterate>
						</div>
					</div>
				</div>
			</div>
		</div><!--sidebar-->
		</div>
