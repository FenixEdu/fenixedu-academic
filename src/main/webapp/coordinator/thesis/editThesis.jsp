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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>

<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="title.coordinator.thesis.proposal" bundle="APPLICATION_RESOURCES"/></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>">
            <bean:message key="title.coordinator.thesis.back" bundle="APPLICATION_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <logic:equal name="thesis" property="valid" value="true">
            <html:link page="<%= String.format("/manageThesis.do?method=submitProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                <bean:message key="title.coordinator.thesis.submit" bundle="APPLICATION_RESOURCES"/>
            </html:link>
        </logic:equal>
        <logic:notEqual name="thesis" property="valid" value="true">
            <bean:message key="title.coordinator.thesis.submit" bundle="APPLICATION_RESOURCES"/>
        </logic:notEqual>
    </li>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=confirmDeleteProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <bean:message key="title.coordinator.thesis.delete" bundle="APPLICATION_RESOURCES"/>
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
            <strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/>
            <bean:message key="label.coordinator.thesis.delete.confirm" bundle="APPLICATION_RESOURCES"/>
        </p>
        <div class="forminline">
            <fr:form action="<%= String.format("/manageThesis.do?method=deleteProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                <html:submit>
                    <bean:message key="button.coordinator.thesis.delete" bundle="APPLICATION_RESOURCES"/>
                </html:submit>
            </fr:form>
            <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                <html:submit>
                    <bean:message key="button.cancel" bundle="APPLICATION_RESOURCES"/>
                </html:submit>
            </fr:form>
        </div>
    </div>
</logic:present>


<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.proposal" bundle="APPLICATION_RESOURCES"/></h3>

<fr:view name="thesis">
	<fr:schema type="net.sourceforge.fenixedu.domain.thesis.Thesis" bundle="APPLICATION_RESOURCES">
		<fr:slot name="enrolment.executionPeriod.qualifiedName" key="label.curricular.course.semester"/>
    	<fr:slot name="state" key="label.thesis.state"/>
    	<fr:slot name="valid" key="label.thesis.valid">
        	<fr:property name="trueLabel" value="label.coordinator.thesis.valid.true"/>
        	<fr:property name="falseLabel" value="label.coordinator.thesis.valid.false"/>
    	</fr:slot>
	</fr:schema>
    <fr:layout name="tabular">
    	<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
    	<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>


<%-- Dissertation --%>
<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.dissertation" bundle="APPLICATION_RESOURCES"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
    	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    	<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<p class="mtop05">
	<html:link page="<%= String.format("/manageThesis.do?method=changeInformation&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
	    <bean:message key="link.coordinator.thesis.edit.changeInformation"  bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

<%-- Rejected information --%>
<logic:equal name="thesis" property="rejected" value="true">
    <div class="warning0" style="padding: 1em;">
        <p class="mtop0 mbottom025"><strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/> <bean:message key="title.coordinator.thesis.edit.rejected" bundle="APPLICATION_RESOURCES"/></p>
        <p class="mtop025 mbottom0">
            	<logic:notEmpty name="thesis" property="rejectionComment" >
                <bean:message key="label.coordinator.thesis.edit.rejected.comment" bundle="APPLICATION_RESOURCES"/>:
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

<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.discussion" bundle="APPLICATION_RESOURCES"/></h3>

<logic:notPresent name="changeDiscussion">
	<fr:view name="thesis" schema="thesis.jury.proposal.discussion">
	    <fr:layout name="tabular">
	    	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
	    	<fr:property name="columnClasses" value="width12em,,"/>
	    </fr:layout>
	</fr:view>
    
    <html:link page="<%= String.format("/manageThesis.do?method=editProposalDiscussion&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.changeInformation" bundle="APPLICATION_RESOURCES"/>
    </html:link>
</logic:notPresent>

<logic:present name="changeDiscussion">
    <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
        <fr:edit id="details" name="thesis" schema="thesis.jury.proposal.discussion.edit">
            <fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            		<fr:property name="columnClasses" value="width12em,,tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="<%= String.format("/manageThesis.do?method=editProposalDiscussion&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>"/>
        </fr:edit>
        
        <html:submit>
            <bean:message key="button.submit"/>
        </html:submit>
        <html:cancel>
            <bean:message key="button.cancel"/>
        </html:cancel>
    </fr:form>
</logic:present>

<%-- Orientation --%>
<h3 class="separator2 mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation" bundle="APPLICATION_RESOURCES"/></h3>

<logic:empty name="thesis" property="orientator">
    <p>
 	   <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=orientator&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
	   		<bean:message key="link.coordinator.thesis.edit.addOrientation" bundle="APPLICATION_RESOURCES"/>
	   </html:link>
    </p>
</logic:empty>
<logic:notEmpty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
	        <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=coorientator&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                <bean:message key="link.coordinator.thesis.edit.addCoorientation" bundle="APPLICATION_RESOURCES"/>
    	    </html:link>
        </p>
    </logic:empty>
</logic:notEmpty>
    
<logic:empty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
            <em><bean:message key="title.coordinator.thesis.edit.orientation.empty" bundle="APPLICATION_RESOURCES"/></em>
        </p>
    </logic:empty>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation.orientator" bundle="APPLICATION_RESOURCES"/></h4>
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
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits" bundle="APPLICATION_RESOURCES"/>:</th>
                    <td class="width35em">
                        <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                            <fr:edit id="editCreditsOrientator" name="thesis" slot="orientatorCreditsDistribution">
                                <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
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
                            <fr:message for="editCreditsOrientator" type="validation" />
                        </span>
                    </td>
                </tr>
            </table>
        </logic:present>
        <logic:notPresent name="editOrientatorCreditsDistribution">
            <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
                <tr>
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits"  bundle="APPLICATION_RESOURCES"/>:</th>
                    <td class="width35em">
                        <logic:empty name="thesis" property="orientatorCreditsDistribution">-</logic:empty>
                        <logic:notEmpty name="thesis" property="orientatorCreditsDistribution">
                            <fr:view name="thesis" property="orientatorCreditsDistribution"/> %
                        </logic:notEmpty>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        (<html:link page="<%= String.format("/manageThesis.do?method=changeCredits&amp;target=orientator&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                            <bean:message key="label.change"  bundle="APPLICATION_RESOURCES"/>
                        </html:link>)
                    </td>
                </tr>
            </table>
        </logic:notPresent>
    </logic:present>
    
    <p class="mtop05">
        <html:link page="<%= String.format("/manageThesis.do?method=changeParticipationInfo&amp;target=orientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson"  bundle="APPLICATION_RESOURCES"/>
        </html:link>,
        <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=orientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson"  bundle="APPLICATION_RESOURCES"/>
        </html:link>
    </p>
</logic:notEmpty>
  
<logic:notEmpty name="thesis" property="coorientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation.coorientator" bundle="APPLICATION_RESOURCES"/></h4>
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
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits" bundle="APPLICATION_RESOURCES"/>:</th>
                    <td class="width35em">
                        <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
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
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits" bundle="APPLICATION_RESOURCES"/>:</th>
                    <td class="width35em">
                        <logic:empty name="thesis" property="coorientatorCreditsDistribution">-</logic:empty>
                        <logic:notEmpty name="thesis" property="coorientatorCreditsDistribution">
                            <fr:view name="thesis" property="coorientatorCreditsDistribution"/> %
                        </logic:notEmpty>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        (<html:link page="<%= String.format("/manageThesis.do?method=changeCredits&amp;target=coorientator&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                            <bean:message key="label.change" bundle="APPLICATION_RESOURCES"/>
                        </html:link>)
                    </td>
                </tr>
            </table>
        </logic:notPresent>
    </logic:present>

    <p class="mtop05">
        <html:link page="<%= String.format("/manageThesis.do?method=changeParticipationInfo&amp;target=coorientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson" bundle="APPLICATION_RESOURCES"/>
        </html:link>,
        <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=coorientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson" bundle="APPLICATION_RESOURCES"/>
        </html:link>
    </p>
</logic:notEmpty>

<%-- Jury --%>
<h3 class="separator2 mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.jury" bundle="APPLICATION_RESOURCES"/></h3>

<div style="padding: 1em;">
	<p class="mtop0">
		<bean:message key="label.thesis.external.orientators" bundle="APPLICATION_RESOURCES"/>
	</p>
</div>

<%-- problems in the jury --%>
<logic:notEmpty name="conditions">
	<div class="warning0" style="padding: 1em;">
    <strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/>
	<ul class="mbottom05">
	    <logic:iterate id="condition" name="conditions">
	       	<li>
	            <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
	            <bean:message key="<%= key %>" bundle="APPLICATION_RESOURCES"/>
			</li>
	    </logic:iterate>
    </ul>
    </div>
</logic:notEmpty>
<p>
	<bean:message key="label.coordinator.thesis.jury" bundle="APPLICATION_RESOURCES"/>
	<html:link target="_blank" href="http://da.ist.utl.pt/dissertacao-de-mestrado/">
		<bean:message key="link.coordinator.thesis.consultHere" bundle="APPLICATION_RESOURCES"/>
	</html:link>.
</p>

<%-- Jury/President --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.jury.president" bundle="APPLICATION_RESOURCES"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.president.empty" bundle="APPLICATION_RESOURCES"/></em>
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
        <bean:message key="link.coordinator.thesis.edit.addPerson" bundle="APPLICATION_RESOURCES"/>
    </html:link>
</logic:empty>
<logic:notEmpty name="thesis" property="president">
    <html:link page="<%= String.format("/manageThesis.do?method=changeParticipationInfo&amp;target=president&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.changePerson" bundle="APPLICATION_RESOURCES"/>
    </html:link>,
    <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=president&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.removePerson" bundle="APPLICATION_RESOURCES"/>
    </html:link>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.vowels" bundle="APPLICATION_RESOURCES"/></h4>

<bean:size id="vowelsSize" name="thesis" property="vowels"/>
<logic:lessThan name="vowelsSize" value="3">
    <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.addVowel" bundle="APPLICATION_RESOURCES"/>
    </html:link>
</logic:lessThan>

<logic:empty name="thesis" property="vowels">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.vowels.empty" bundle="APPLICATION_RESOURCES"/></em>
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
    
        <bean:define id="vowelId" name="vowel" property="externalId"/>
        
        <html:link page="<%= String.format("/manageThesis.do?method=changeParticipationInfo&amp;target=vowel&amp;vowelID=%s&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", vowelId, dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson" bundle="APPLICATION_RESOURCES"/>
        </html:link>
        , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=%s&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", vowelId, dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson" bundle="APPLICATION_RESOURCES"/>
        </html:link>
    </logic:iterate>
</logic:notEmpty>
