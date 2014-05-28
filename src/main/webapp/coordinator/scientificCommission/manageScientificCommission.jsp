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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionDegreeId" name="executionDegreeId"/>

<h2>
    <bean:message key="title.coordinator.degreeCurricularPlan.scientificCommissionTeam"/>
</h2>

<div class="dinline forminline">
    <fr:form action="<%= "/scientificCommissionTeamDA.do?method=manage&amp;degreeCurricularPlanID=" + degreeCurricularPlanId %>">
        <fr:edit id="executionDegreeChoice" name="executionDegreeBean" schema="executionDegree.context.choose.noNull">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle dinline"/>
                <fr:property name="columnClasses" value=",,tdclear"/>
            </fr:layout>
            <fr:destination name="chooseExecutionDegree" path="<%= "/scientificCommissionTeamDA.do?method=manage&degreeCurricularPlanID=" + degreeCurricularPlanId %>"/>
        </fr:edit>
    </fr:form>
</div>

<logic:present name="executionDegree">
    <div class="mtop15">
        <logic:empty name="members">
            <em><bean:message key="label.coordinator.degreeCurricularPlan.scientificCommissionTeam.empty"/></em>
        </logic:empty>
        
        <logic:notEmpty name="members">
            <fr:form action="<%= String.format("/scientificCommissionTeamDA.do?method=manage&amp;degreeCurricularPlanID=%s&amp;executionDegreeID=%s", degreeCurricularPlanId, executionDegreeId) %>">
                <div class="infoop2">
                    <p class="mvert0"><bean:message key="label.coordinator.scientificCommision.contact.info"/></p>
                </div>
        
                <logic:present name="updateContactConfirmation">
                	<p class="mtop15">
	                    <span class="success0"><bean:message key="label.coordinator.scientificCommision.contact.updated"/></span>
                    </p>
                </logic:present>
            
                <logic:present name="responsible">
                    <fr:edit id="membersContacts" name="members" schema="coordinator.commissionTeam.manage.contacts">
                        <fr:layout name="tabular-editable">
                            <fr:property name="classes" value="tstyle1 thlight"/>
                            <fr:property name="columnClasses" value=",acenter,"/>
                                
                                <fr:property name="link(delete)" value="<%= String.format("/scientificCommissionTeamDA.do?method=removeMember&amp;degreeCurricularPlanID=%s&amp;executionDegreeID=%s", degreeCurricularPlanId, executionDegreeId) %>"/>
                                <fr:property name="param(delete)" value="externalId/memberID"/>
                                <fr:property name="key(delete)" value="label.coordinator.scientificCommision.remove"/>
                        </fr:layout>
                    </fr:edit>
                </logic:present>
                <logic:notPresent name="responsible">
                    <fr:view name="members" schema="coordinator.commissionTeam.manage.contacts">
                        <fr:layout name="tabular">
                            <fr:property name="classes" value="tstyle1"/>
                            <fr:property name="columnClasses" value=",acenter,"/>
                        </fr:layout>
                    </fr:view>
                </logic:notPresent>

            <logic:present name="responsible">
                <html:submit>
                    <bean:message key="button.change"/> 
                </html:submit>
            </logic:present>
            </fr:form>
        </logic:notEmpty>
    </div>
    
    <logic:present name="responsible">

            <p class="mtop2 mbottom05"><strong><bean:message key="title.coordinator.scientificCommision.add"/></strong></p>
            
            <p class="mvert05">
                <span class="color888"><bean:message key="label.coordinator.scientificCommision.add.message"/></span>
            </p>
        
            <logic:messagesPresent message="true" property="addError">
                <html:messages id="message" message="true" property="addError">
                    <p><span class="error0"><bean:write name="message"/></span></p>
                </html:messages>
            </logic:messagesPresent>
            
            <fr:form action="<%= String.format("/scientificCommissionTeamDA.do?method=addMember&amp;degreeCurricularPlanID=%s&amp;executionDegreeID=%s", degreeCurricularPlanId, executionDegreeId) %>">
                <fr:edit id="usernameChoice" name="usernameBean" schema="coordinator.commissionTeam.addMemberByIstId">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle5 thlight thright thmiddle mvert05"/>
                        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
                    </fr:layout>
                    
                    <fr:destination name="invalid" path="<%= "/scientificCommissionTeamDA.do?method=manage&amp;degreeCurricularPlanID=" + degreeCurricularPlanId %>"/>
                </fr:edit>
    
                <html:submit styleClass="mtop05">
                    <bean:message key="button.add"/> 
                </html:submit>
            </fr:form>

    </logic:present>
</logic:present>
