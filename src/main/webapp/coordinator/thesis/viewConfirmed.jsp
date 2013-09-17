<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.confirm"/></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>">
            <bean:message key="title.coordinator.thesis.back"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=printApprovalDocument&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
	        <bean:message key="label.coordinator.list.submitted.thesis.reprint"/>
	    </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=downloadIdentificationSheet&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <bean:message key="link.student.thesis.identification.download"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=confirmRevision&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <bean:message key="title.coordinator.thesis.enterRevision"/>
        </html:link>
    </li>
</ul>

<%-- Delete proposal --%>
<logic:present name="confirmRevision">
	<div class="warning0" style="padding: 1em;">
			<p class="mtop0 mbottom1">	
		    	<strong><bean:message key="label.attention"/>:</strong><br/>
		    	<bean:message key="label.coordinator.thesis.revision.confirm"/>
	    	</p>
    	    <div class="forminline">
	        <fr:form action="<%= String.format("/manageThesis.do?method=enterRevision&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
	            <html:submit>
	                <bean:message key="button.coordinator.thesis.enterRevision"/>
	            </html:submit>
	        </fr:form>
	        <fr:form action="<%= String.format("/manageThesis.do?method=viewConfirmed&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
	            <html:cancel>
	                <bean:message key="button.cancel"/>
	            </html:cancel>
	        </fr:form>
	    </div>
    </div>
</logic:present>

<%-- general process message --%>
<logic:notPresent name="confirmRevision">
	<div class="infoop2">
		<p class="mvert0"><bean:message key="message.coordinator.thesis.confirmed.process"/></p>
	</div>
</logic:notPresent>

<%-- Dissertation --%>
<h3><bean:message key="title.coordinator.thesis.confirm.details"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="label.thesis.abstract"/></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <p><em><bean:message key="label.thesis.abstract.empty"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <p>
        <fr:view name="thesis" property="thesisAbstract">
            <fr:layout>
                <fr:property name="language" value="pt"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
    
    <p>
        <fr:view name="thesis" property="thesisAbstract">
            <fr:layout>
                <fr:property name="language" value="en"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
</logic:equal>


<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.keywords"/></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages" value="true">
    <p><em><bean:message key="label.thesis.keywords.empty"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="keywordsInBothLanguages" value="true">
    <p>
        <fr:view name="thesis" property="keywords">
            <fr:layout>
                <fr:property name="language" value="pt"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
    
    <p>
        <fr:view name="thesis" property="keywords">
            <fr:layout>
                <fr:property name="language" value="en"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
</logic:equal>


<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.confirm.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <p><em><bean:message key="label.coordinator.thesis.confirm.noExtendedAbstract"/></em></p>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>)
</logic:notEmpty>


<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.confirm.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <p><em><bean:message key="label.coordinator.thesis.confirm.noDissertation"/></em></p>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>)
</logic:notEmpty>


<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.revision.gradeAndDate"/></h3>

<fr:view name="thesis" schema="coordinator.thesis.revision.view">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>


<%-- Jury --%>
<h3 class="mtop2 separator2"><bean:message key="title.coordinator.thesis.edit.section.jury"/></h3>

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
<h4 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.jury.president"/></h4>

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
<h4 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.vowels"/></h4>

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

