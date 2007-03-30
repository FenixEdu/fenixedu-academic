<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="executionDegreeId" name="executionDegreeId"/>

<h3>
    <bean:message key="title.coordinator.degreeCurricularPlan.scientificCommissionTeam"/>
</h3>

<div class="dinline forminline">
    <fr:form action="<%= "/scientificCommissionTeamDA.do?method=manage&amp;degreeCurricularPlanID=" + degreeCurricularPlanId %>">
        <fr:edit id="executionDegreeChoice" name="executionDegreeBean" schema="executionDegree.context.choose">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle dinline"/>
                <fr:property name="columnClasses" value=",,tdclear"/>
            </fr:layout>
            
            <fr:destination name="chooseExecutionDegree" path="<%= "/scientificCommissionTeamDA.do?method=manage&degreeCurricularPlanID=" + degreeCurricularPlanId %>"/>
        </fr:edit>
        
        <html:submit styleClass="switchNone">
            <bean:message key="button.submit"/>
        </html:submit>
    </fr:form>
</div>

<logic:present name="executionDegree">
    <div class="mtop2">
        <logic:empty name="members">
            <em><bean:message key="label.coordinator.degreeCurricularPlan.scientificCommissionTeam.empty"/></em>
        </logic:empty>
        
        <logic:notEmpty name="members">
            <fr:form action="<%= String.format("/scientificCommissionTeamDA.do?method=manage&amp;degreeCurricularPlanID=%s&amp;executionDegreeID=%s", degreeCurricularPlanId, executionDegreeId) %>">
                <div class="infoop2">
                    <p class="mvert0"><bean:message key="label.coordinator.scientificCommision.contact.info"/></p>
                </div>
        
                <logic:present name="updateContactConfirmation">
                    <span class="success0"><bean:message key="label.coordinator.scientificCommision.contact.updated"/></span>
                </logic:present>
            
                <fr:edit id="membersContacts" name="members" schema="coordinator.commissionTeam.manage.contacts">
                    <fr:layout name="tabular-editable">
                        <logic:present name="responsible">
                            <fr:property name="classes" value="tstyle1"/>
                            <fr:property name="columnClasses" value=",acenter,"/>
                            
                            <fr:property name="link(delete)" value="<%= String.format("/scientificCommissionTeamDA.do?method=removeMember&amp;degreeCurricularPlanID=%s&amp;executionDegreeID=%s", degreeCurricularPlanId, executionDegreeId) %>"/>
                            <fr:property name="param(delete)" value="idInternal/memberID"/>
                            <fr:property name="key(delete)" value="label.coordinator.scientificCommision.remove"/>
                        </logic:present>
                    </fr:layout>
                </fr:edit>

            <html:submit>
                <bean:message key="button.change"/> 
            </html:submit>
            </fr:form>
        </logic:notEmpty>
    </div>
    
    <logic:present name="responsible">
        <div class="mtop2">
            <strong><bean:message key="title.coordinator.scientificCommision.add"/></strong>
            
            <p>
                <em><bean:message key="label.coordinator.scientificCommision.add.message"/></em>
            </p>
        
            <logic:messagesPresent message="true" property="addError">
                <html:messages id="message" message="true" property="addError">
                    <p><span class="error0"><bean:write name="message"/></span></p>
                </html:messages>
            </logic:messagesPresent>
            
            <fr:form action="<%= String.format("/scientificCommissionTeamDA.do?method=addMember&amp;degreeCurricularPlanID=%s&amp;executionDegreeID=%s", degreeCurricularPlanId, executionDegreeId) %>">
                <fr:edit id="usernameChoice" name="usernameBean" schema="coordinator.commissionTeam.addMember">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle5 tdmiddle thlight thright thmiddle mbottom0"/>
                        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
                    </fr:layout>
                    
                    <fr:destination name="invalid" path="<%= "/scientificCommissionTeamDA.do?method=manage&amp;degreeCurricularPlanID=" + degreeCurricularPlanId %>"/>
                </fr:edit>
    
                <html:submit styleClass="mtop05">
                    <bean:message key="button.add"/> 
                </html:submit>
            </fr:form>
        </div>
    </logic:present>
</logic:present>

<script type="text/javascript">
    switchGlobal();
</script>
