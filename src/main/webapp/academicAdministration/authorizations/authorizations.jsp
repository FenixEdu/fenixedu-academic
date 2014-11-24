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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<jsp:include page="authorizationsScripts.jsp" />

<div id="authorizationList" style="margin-top: 15px">
	<logic:iterate id="memberRules" name="groups">
		<div class="edit-authorizations">
			<div id="period" class="authorization period  ui-droppable">
				<header id="header">
					<h4>
					   <bean:write name="memberRules" property="key.localizedName" />
					</h4>
					<html:link action="/authorizations.do?method=manageOperation" paramId="operation" paramName="memberRules"
							paramProperty="key" styleClass="edit-auth">
						<img src="${pageContext.request.contextPath}/images/iconEditOff.png" />
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.academicAdministration.authorizations.manage" />
					</html:link>
				</header>
				<ul style="display: none" class="small">
					<logic:notEmpty name="memberRules" property="value">
					<logic:iterate id="rule" name="memberRules" property="value">
					   <li>
					       <strong>
								<logic:equal name="rule" property="party.person" value="true">
									<bean:message bundle="APPLICATION_RESOURCES" key="person" />:
									<bean:write name="rule" property="party.name" /> -
									<bean:write name="rule" property="party.username" />
								</logic:equal>
								<logic:equal name="rule" property="party.unit" value="true">
									<bean:message bundle="APPLICATION_RESOURCES" key="label.party" />:
									<bean:write name="rule" property="party.name" />
								</logic:equal>
							</strong> pode aceder 
					       <logic:notEmpty	name="rule" property="offices"> para os cursos geridos pelas secretarias:
								<logic:iterate id="office" name="rule" property="offices">
									<bean:write name="office" property="name.content" />
								</logic:iterate>
							</logic:notEmpty>
							<logic:notEmpty name="rule" property="programs">e para os cursos:
								<logic:iterate id="program"	name="rule" property="programs">
									<bean:write name="program" property="presentationName"/>
								</logic:iterate>
							</logic:notEmpty>
							<html:link action="/authorizations.do?method=revokeRule" paramId="ruleId"
										paramName="rule" paramProperty="id"
										onclick="return confirm('Tem a certeza que pretende remover a autorização?');">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.remove" />
							</html:link>
						</li>
					</logic:iterate>
					</logic:notEmpty>
					<logic:empty name="memberRules" property="value">
						<p>Nobody has access</p>
					</logic:empty>
				</ul>
			</div>
		</div>
	</logic:iterate>
</div>