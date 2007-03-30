<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.proposal"/></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s", dcpId) %>">
            <bean:message key="title.coordinator.thesis.back"/>
        </html:link>
    </li>
    <li>
        <logic:equal name="thesis" property="valid" value="true">
            <html:link page="<%= String.format("/manageThesis.do?method=submitProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
                <bean:message key="title.coordinator.thesis.submit"/>
            </html:link>
        </logic:equal>
        <logic:notEqual name="thesis" property="valid" value="true">
            <bean:message key="title.coordinator.thesis.submit"/>
        </logic:notEqual>
    </li>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=confirmDeleteProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="title.coordinator.thesis.delete"/>
        </html:link>
    </li>
</ul>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<%-- Delete proposal --%>
<logic:present name="confirmDelete">

    <div class="warning0" style="padding: 1em;">
        <p class="mtop0">
            <strong><bean:message key="label.attention"/>:</strong><br/>
            <bean:message key="label.coordinator.thesis.delete.confirm"/>
        </p>
        <div class="forminline">
            <fr:form action="<%= String.format("/manageThesis.do?method=deleteProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
                <html:submit>
                    <bean:message key="button.coordinator.thesis.delete"/>
                </html:submit>
            </fr:form>
            <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
                <html:submit>
                    <bean:message key="button.cancel"/>
                </html:submit>
            </fr:form>
        </div>
    </div>
</logic:present>


<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.proposal"/></h3>

<fr:view name="thesis" schema="coordinator.thesis.state">
    <fr:layout name="tabular">
    	<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
    	<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>


<%-- Dissertation --%>
<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.dissertation"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
    	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    	<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<p class="mtop05">
	<html:link page="<%= String.format("/manageThesis.do?method=changeInformation&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
	    <bean:message key="link.coordinator.thesis.edit.changeInformation"/>
	</html:link>
</p>

<%-- Rejected information --%>
<logic:equal name="thesis" property="rejected" value="true">
    <div class="warning0" style="padding: 1em;">
        <p class="mtop0 mbottom025"><strong><bean:message key="label.attention"/>:</strong><br/> <bean:message key="title.coordinator.thesis.edit.rejected"/></span></p>
        <p class="mtop025 mbottom0">
            	<logic:notEmpty name="thesis" property="rejectionComment" >
                <bean:message key="label.coordinator.thesis.edit.rejected.comment"/>:
            </logic:notEmpty>
    	        <fr:view name="thesis" property="rejectionComment" type="java.lang.String">
    	            <fr:layout name="null-as-label">
    	                <fr:property name="label" value="label.coordinator.thesis.edit.rejected.empty"/>
    	                <fr:property name="key" value="true"/>
                    <fr:property name="classes" value="italic"/>
    	            </fr:layout>
    	        </fr:view>
        </p>
    </div>
</logic:equal>


<%-- Jury --%>
<h3 class="separator2 mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.jury"/></h3>

<%--  problems in the jury --%>
<logic:notEmpty name="conditions">
	<div class="warning0" style="padding: 1em;">
    <strong><bean:message key="label.attention"/>:</strong><br/>
	<ul class="mbottom05">
	    <logic:iterate id="condition" name="conditions">
	       	<li>
	            <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
	            <bean:message key="<%= key %>"/>
			</li>
	    </logic:iterate>
    </ul>
    </div>
</logic:notEmpty>

<%-- Orientation --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation"/></h4>

<logic:empty name="thesis" property="orientator">
    <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=orientator&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <p>
            <bean:message key="link.coordinator.thesis.edit.addOrientation"/>
        </p>
    </html:link>
</logic:empty>
<logic:notEmpty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=coorientator&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <p>
                <bean:message key="link.coordinator.thesis.edit.addOrientation"/>
            </p>
        </html:link>
    </logic:empty>
</logic:notEmpty>
    
<logic:empty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
            <em><bean:message key="title.coordinator.thesis.edit.orientation.empty"/></em>
        </p>
    </logic:empty>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
           	<fr:property name="classes" value="tstyle2 thlight thright mbottom0"/>
           	<fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>

    <logic:present name="orientatorCreditsDistribution">
        <logic:present name="editOrientatorCreditsDistribution">
            <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
                <tr>
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits"/>:</th>
                    <td class="width35em">
                        <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
                            <fr:edit id="editCreditsOrientator" name="thesis" slot="orientatorCreditsDistribution">
                                <fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.LongRangeValidator">
                                    <fr:property name="lowerBound" value="0"/>
                                    <fr:property name="upperBound" value="100"/>
                                </fr:validator>
                                <fr:layout>
                                    <fr:property name="size" value="10"/>
                                </fr:layout>
                            </fr:edit> %
                            
                            <html:submit>
                                <bean:message key="button.submit"/>
                            </html:submit>
                            <html:cancel>
                                <bean:message key="button.cancel"/>
                            </html:cancel>
                        </fr:form>
                    </td>
                    <td class="tdclear">
                        <span class="error0">
                            <fr:message for="editCreditsOrientator" type="validation"/>
                        </span>
                    </td>
                </tr>
            </table>
        </logic:present>
        <logic:notPresent name="editOrientatorCreditsDistribution">
            <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
                <tr>
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits"/>:</th>
                    <td class="width35em">
                        <logic:empty name="thesis" property="orientatorCreditsDistribution">-</logic:empty>
                        <logic:notEmpty name="thesis" property="orientatorCreditsDistribution">
                            <fr:view name="thesis" property="orientatorCreditsDistribution"/> %
                        </logic:notEmpty>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        (<html:link page="<%= String.format("/manageThesis.do?method=changeCredits&amp;target=orientator&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
                            <bean:message key="label.change"/>
                        </html:link>)
                    </td>
                </tr>
            </table>
        </logic:notPresent>
    </logic:present>
    
    <p class="mtop05">
        <html:link page="<%= String.format("/manageThesis.do?method=changeParticipationInfo&amp;target=orientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson"/>
        </html:link>,
        <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=orientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson"/>
        </html:link>
    </p>
</logic:notEmpty>
  
<logic:notEmpty name="thesis" property="coorientator">
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
        	    	<fr:property name="classes" value="tstyle2 thlight thright mbottom0"/>
        	    	<fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>

    <logic:present name="coorientatorCreditsDistribution">
        <logic:present name="editCoorientatorCreditsDistribution">
            <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
                <tr>
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits"/>:</th>
                    <td class="width35em">
                        <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
                            <fr:edit id="editCreditsCoorientator" name="thesis" slot="coorientatorCreditsDistribution">
                                <fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.LongRangeValidator">
                                    <fr:property name="lowerBound" value="0"/>
                                    <fr:property name="upperBound" value="100"/>
                                </fr:validator>
                                <fr:layout>
                                    <fr:property name="size" value="10"/>
                                </fr:layout>
                            </fr:edit> %
                            
                            <html:submit>
                                <bean:message key="button.submit"/>
                            </html:submit>
                            <html:cancel>
                                <bean:message key="button.cancel"/>
                            </html:cancel>
                        </fr:form>
                    </td>
                    <td class="tdclear">
                        <span class="error0">
                            <fr:message for="editCreditsCoorientator" type="validation"/>
                        </span>
                    </td>
                </tr>
            </table>
        </logic:present>
        <logic:notPresent name="editCoorientatorCreditsDistribution">
            <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
                <tr>
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits"/>:</th>
                    <td class="width35em">
                        <logic:empty name="thesis" property="coorientatorCreditsDistribution">-</logic:empty>
                        <logic:notEmpty name="thesis" property="coorientatorCreditsDistribution">
                            <fr:view name="thesis" property="coorientatorCreditsDistribution"/> %
                        </logic:notEmpty>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        (<html:link page="<%= String.format("/manageThesis.do?method=changeCredits&amp;target=coorientator&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
                            <bean:message key="label.change"/>
                        </html:link>)
                    </td>
                </tr>
            </table>
        </logic:notPresent>
    </logic:present>

    <p class="mtop05">
        <html:link page="<%= String.format("/manageThesis.do?method=changeParticipationInfo&amp;target=coorientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson"/>
        </html:link>,
        <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=coorientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson"/>
        </html:link>
    </p>
</logic:notEmpty>

<%-- Jury/President --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.president.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
	    	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
	    	<fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:empty name="thesis" property="president">
    <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=president&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.addPerson"/>
    </html:link>
</logic:empty>
<logic:notEmpty name="thesis" property="president">
    <html:link page="<%= String.format("/manageThesis.do?method=changeParticipationInfo&amp;target=president&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.changePerson"/>
    </html:link>,
    <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=president&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.removePerson"/>
    </html:link>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.vowels"/></h4>

<bean:size id="vowelsSize" name="thesis" property="vowels"/>
<logic:lessThan name="vowelsSize" value="3">
    <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.addVowel"/>
    </html:link>
</logic:lessThan>

<logic:empty name="thesis" property="vowels">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.vowels.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels">
        <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person">
            <fr:layout name="tabular">
		    	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		    	<fr:property name="columnClasses" value="width12em,width35em,"/>
            </fr:layout>
        </fr:view>
    
        <bean:define id="vowelId" name="vowel" property="idInternal"/>
        
        <html:link page="<%= String.format("/manageThesis.do?method=changeParticipationInfo&amp;target=vowel&amp;vowelID=%s&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", vowelId, dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson"/>
        </html:link>
        , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=%s&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", vowelId, dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson"/>
        </html:link>
    </logic:iterate>
</logic:notEmpty>
