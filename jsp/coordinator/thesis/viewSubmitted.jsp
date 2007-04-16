<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.proposal"/></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>">
            <bean:message key="title.coordinator.thesis.back"/>
        </html:link>
    </li>
    <logic:equal name="thesis" property="valid" value="true">
        <li>
            <html:link page="<%= String.format("/manageThesis.do?method=printApprovalDocument&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                <bean:message key="label.coordinator.list.submitted.thesis.print"/>
            </html:link>
        </li>
    </logic:equal>
</ul>

<%-- errors --%>
<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<%-- content --%>
<h3><bean:message key="title.coordinator.thesis.edit.proposal"/></h3>

<fr:view name="thesis" schema="coordinator.thesis.state.submitted">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
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


<%-- Jury --%>
<h3 class="separator2 mtop15"><bean:message key="title.coordinator.thesis.edit.section.jury"/></h3>


<%-- Orientation --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation"/></h4>

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
           	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
	        	<fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
    <logic:equal name="thesis" property="orientatorCreditsDistributionNeeded" value="true">
        <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
            <tr>
                <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits"/>:</th>
                <td class="width35em">
                    <logic:empty name="thesis" property="orientatorCreditsDistribution">-</logic:empty>
                    <logic:notEmpty name="thesis" property="orientatorCreditsDistribution">
                        <fr:view name="thesis" property="orientatorCreditsDistribution"/> %
                    </logic:notEmpty>
                </td>
            </tr>
        </table>
    </logic:equal>
</logic:notEmpty>
  
<logic:notEmpty name="thesis" property="coorientator">
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
        	    	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
        	    	<fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
    <logic:equal name="thesis" property="coorientatorCreditsDistributionNeeded" value="true">
        <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
            <tr>
                <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits"/>:</th>
                <td class="width35em">
                    <logic:empty name="thesis" property="coorientatorCreditsDistribution">-</logic:empty>
                    <logic:notEmpty name="thesis" property="coorientatorCreditsDistribution">
                        <fr:view name="thesis" property="coorientatorCreditsDistribution"/> %
                    </logic:notEmpty>
                </td>
            </tr>
        </table>
    </logic:equal>
</logic:notEmpty>

<%-- Jury/President --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.jury.president"/></h4>

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


<%-- Jury/"Vowels" --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.vowels"/></h4>

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
    </logic:iterate>
</logic:notEmpty>
