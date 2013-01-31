<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="org.apache.struts.action.ActionMessages"%>

<jsp:include page="authorizationsScripts.jsp"/>

		<html:link action="/authorizations.do?method=managePartyAuthorization" paramId="partyId"
					paramName="managementBean" paramProperty="party.externalId" styleId="reloadLink"/>

		<header>
			<h2><bean:message key="label.authorizations" bundle="ACADEMIC_OFFICE_RESOURCES" />
			<span><bean:write name="managementBean" property="party.name"/>
			</span>
			</h2>
		</header>
		
		<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>" >
			<p>
				<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
			</p>
		</html:messages>

		<div id="all">
		<div id="main">

			<section id="authorizations">
			
				<logic:equal value="false" name="managementBean" property="hasNewObject">
					<fr:form action="/authorizations.do?method=addNewAuthorization">
						<fr:edit id="managementBean" name="managementBean" visible="false" />
						<html:submit value="Criar nova Autorização"/>
					</fr:form>
				</logic:equal>
				
				<br />
				
				<br />
				
				
				
				<div id="authorizationList">

				<logic:iterate id="group" name="managementBean" property="groups">
				<bean:define id="newObject" name="group" property="newObject" type="java.lang.Boolean"/>
				
				<bean:define id="id" name="group" property="id" type="java.lang.Long" />
				
				<div class="edit-authorizations">
				<fr:form action="/authorizations.do">
				
				<html:hidden property="method" value="authorizationsPerPerson"/>
				
				<fr:edit id="managementBean" name="managementBean" visible="false" />
								
				<div id="period" class="authorization period <%= newObject ? "newObject" : "" %>">
					<header id="header">
						<h2>
						<logic:notPresent name="group" property="operation">
							<bean:message key="label.new.authorization" bundle="ACADEMIC_OFFICE_RESOURCES"/>
						</logic:notPresent>
						<logic:present name="group" property="operation">
							<bean:write name="group" property="operation.localizedName"/>
						</logic:present>
						</h2>
						<span class="saveButton"><i><bean:message key="label.authorizations.unsavedChanges" bundle="ACADEMIC_OFFICE_RESOURCES"/></i></span>
						<a href="javascript:void(0);" class="edit-auth"><img src="../images/iconEditOff.png" /> <bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></a>
					</header>
					
					<div class="authorization-edit">
						<fr:edit name="group">
							<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AuthorizationGroupBean" bundle="ACADEMIC_OFFICE_RESOURCES">
								<fr:slot name="operation" key="label.operation">
									<fr:property name="defaultOptionHidden" value="true"/>
									<fr:property name="sort" value="true"/>
									<fr:property name="onChange" value="changedValue($(this));"/>
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
							<a class="eliminar" onclick="<%= "deleteAuthorization($(this)," + id + ")"%>" style="color: red"><bean:message key="label.delete" bundle="APPLICATION_RESOURCES"/></a>
						</div>
						</logic:equal>
					</div>
					<table width="100%">
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
						<html:submit value="Descartar Alterações" styleClass="saveButton" onclick="reload(); return false;"/>
						<html:button value="Guardar Alterações" styleClass="saveButton" onclick="<%= "editAuthorizationPrograms($(this).parents().eq(3), " + id + ");" %>" 
								     property="saveChanges" style="margin-right: 8px;"/>
						</li>
					</ul>
				</div>
				
				</fr:form>
				</div>
				
				</logic:iterate>				
				
				<logic:equal name="managementBean" property="party.unit" value="true">
					<div class="edit-authorizations">
						<div id="period" class="peopleInUnit period">
							<header id="header" align="center">
								<h2>Pessoas na unidade</h2>
							</header>
							<ul style="display: none">
								<logic:iterate id="person" name="managementBean" property="peopleInUnit">
									<li><bean:write name="person" property="name"/>
										<span style="float: right"><bean:write name="person" property="username"/></span>
									</li>
								</logic:iterate>
							</ul>
						</div>
					</div>
				</logic:equal>

				</div>
			
			</section>
		</div><!--main-->
		
		
		<div id="sidebar">
			
				<div id="cursos_acc">
				
					<h3><bean:message key="portal.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
					
					<div>
					<logic:iterate id="office" name="managementBean" property="administrativeOffices" 
								   type="net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice">
						<div class="draggable_course tooltip office">
							<div id="oid" style="display:none"><bean:write name="office" property="oid"/></div>
							<div id="presentationName" style="display:none"><bean:write name="office" property="unit.name"/></div>
							<div id="name"><bean:write name="office" property="unit.name"/></div>
						</div>
					</logic:iterate>
					</div>
			
					<logic:iterate id="degreeType" name="managementBean" property="degreeTypes" type="net.sourceforge.fenixedu.domain.degree.DegreeType">
				
					<h3><bean:write name="degreeType" property="localizedName" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
					<div>
						<logic:iterate id="degree" name="managementBean" property="degrees">
							<logic:equal value="<%= degreeType.name() %>" name="degree" property="degreeType.name">
									<div class="draggable_course tooltip degree">
										<div id="oid" style="display:none"><bean:write name="degree" property="oid"/></div>
										<div id="presentationName" style="display:none"><bean:write name="degree" property="presentationName"/></div>
										<div id="name"><bean:write name="degree" property="name"/></div>
									</div>
							</logic:equal>
						</logic:iterate>
					</div>
					
					</logic:iterate>
					
					<h3><bean:message key="title.phd.programs" bundle="PHD_RESOURCES"/></h3>
					
					<div>
					<logic:iterate id="program" name="managementBean" property="phdPrograms" 
								   type="net.sourceforge.fenixedu.domain.phd.PhdProgram">
						<div class="draggable_course tooltip degree">
							<div id="oid" style="display:none"><bean:write name="program" property="oid"/></div>
							<div id="presentationName" style="display:none"><bean:write name="program" property="presentationName"/></div>
							<div id="name"><bean:write name="program" property="name"/></div>
						</div>
					</logic:iterate>
					</div>
				</div>
		</div><!--sidebar-->
		</div>
		</div>
		