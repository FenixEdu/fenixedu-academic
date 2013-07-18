<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<jsp:include page="authorizationsScripts.jsp" />

<script type="text/javascript">
	$(document).ready(function() {
		$('#createRuleLink').click(function() {
			$('#createRuleLink').fadeOut(300, function() {
				$('#createRule').fadeIn();
			})
		});
	});
</script>

<p>
	<a id="createRuleLink" href="#" onclick="showCreateRule"><strong>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.academicAdministration.authorizations.create"/>
	</strong></a>
</p>

<div id="createRule" style="display: none">
	<h2>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.academicAdministration.authorizations.create"/>
	</h2>

	<fr:form action="/authorizations.do?method=managePartyAuthorization">
		<table>
			<tr>
				<td><fr:edit id="authorizationsBean" name="authorizationsBean" required="true">
						<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
							type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AuthorizationsManagementBean">
							<fr:slot name="party" layout="autoComplete" key="label.academicAdministration.authorizations.member">
								<fr:property name="size" value="50" />
								<fr:property name="labelField" value="presentationName" />
								<fr:property name="indicatorShown" value="true" />
								<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchAllActiveParties" />
								<fr:property name="args" value="slot=name,size=50,internal=true" />
								<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.Party" />
								<fr:property name="minChars" value="4" />
							</fr:slot>
						</fr:schema>
						<fr:destination name="cancel" path="/authorizations.do?method=authorizations" />
					</fr:edit></td>
				<td><html:submit>
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.submit" />
					</html:submit> <html:cancel>
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.cancel" />
					</html:cancel></td>
			</tr>
		</table>
	</fr:form>
</div>

<div id="authorizationList" style="margin-top: 15px">
	<logic:iterate id="memberRules" name="groups">
		<bean:define id="partyOid" name="memberRules" property="key.externalId" />
		<div class="edit-authorizations">
			<div id="period" class="authorization period  ui-droppable">
				<header id="header">
					<h2>
						<logic:equal name="memberRules" property="key.person" value="true">
							<bean:message bundle="APPLICATION_RESOURCES" key="person" />:
							<bean:write name="memberRules" property="key.name" /> -
							<bean:write name="memberRules" property="key.username" />
						</logic:equal>
						<logic:equal name="memberRules" property="key.unit" value="true">
							<bean:message bundle="APPLICATION_RESOURCES" key="label.party" />:
							<bean:write name="memberRules" property="key.name" />
						</logic:equal>
					</h2>
					<html:link action="/authorizations.do?method=managePartyAuthorization" paramId="partyId" paramName="memberRules"
							paramProperty="key.externalId" styleClass="edit-auth">
						<img src="../images/iconEditOff.png" />
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.academicAdministration.authorizations.manage" />
					</html:link>
				</header>
				<ul style="display: none">
					<logic:iterate id="rule" name="memberRules" property="value">
						<li>Pode <strong><bean:write name="rule" property="operation.localizedName" /></strong>
							<logic:notEmpty	name="rule" property="office"> para os cursos geridos pelas secretarias:
								<logic:iterate id="office" name="rule" property="office">
									<bean:write name="office" property="unit.name" />
								</logic:iterate>
							</logic:notEmpty>
							<logic:notEmpty name="rule" property="program">e para os cursos:
								<logic:iterate id="program"	name="rule" property="program">
									<bean:write name="program" property="presentationName"/>
								</logic:iterate>
							</logic:notEmpty>
							<html:link action="<%= "/authorizations.do?method=removePartyFromGroup&partyId=" + partyOid %>" paramId="groupId"
										paramName="rule" paramProperty="externalId" 
										onclick="return confirm('Tem a certeza que pretende remover a autorização?');">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.remove" />
							</html:link>
						</li>
					</logic:iterate>
				</ul>
			</div>
		</div>
	</logic:iterate>
</div>