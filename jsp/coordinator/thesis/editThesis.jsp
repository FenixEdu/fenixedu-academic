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

<%-- Delete proposal --%>
<logic:present name="confirmDelete">
    <bean:message key="label.coordinator.thesis.delete.confirm"/>

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
    <p><span class="warning0"><bean:message key="title.coordinator.thesis.edit.rejected"/></span></p>
    <p>
    	<em>
	        <fr:view name="thesis" property="rejectionComment" type="java.lang.String">
	            <fr:layout name="null-as-label">
	                <fr:property name="label" value="label.coordinator.thesis.edit.rejected.empty"/>
	                <fr:property name="key" value="true"/>
	            </fr:layout>
	        </fr:view>
        </em>
    </p>
</logic:equal>


<%-- Jury --%>
<h3 class="separator2 mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.jury"/></h3>

<%--  problems in the jury --%>
<logic:notEmpty name="conditions">
	<div class="warning0">
	<ul class="mbottom05">
	    <logic:iterate id="condition" name="conditions">
	       	<li>
	        	<em>
		            <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
		            <bean:message key="<%= key %>"/>
	            </em>
			</li>
	    </logic:iterate>
    </ul>
    </div>
</logic:notEmpty>


<%-- Orientator --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation.orientator"/></h4>

<logic:empty name="thesis" property="orientator">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.orientator.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
           	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
	    	<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>
  
<html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=orientator&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
    <bean:message key="link.coordinator.thesis.edit.changePerson"/>
</html:link>

<logic:notEmpty name="thesis" property="orientator">
    , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=orientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.removePerson"/>
    </html:link>
</logic:notEmpty>


<%-- Coorientator --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation.coorientator"/></h4>

<logic:empty name="thesis" property="coorientator">
    <p>
        <em><bean:message key="title.coordinator.thesis.edit.coorientator.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="coorientator">
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
	    	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
	    	<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=coorientator&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
    <bean:message key="link.coordinator.thesis.edit.changePerson"/>
</html:link>
<logic:notEmpty name="thesis" property="coorientator">
    , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=coorientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.removePerson"/>
    </html:link>
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
	    	<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=president&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
    <bean:message key="link.coordinator.thesis.edit.changePerson"/>
</html:link>
<logic:notEmpty name="thesis" property="president">
    , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=president&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
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
		    	<fr:property name="columnClasses" value="width12em,,"/>
            </fr:layout>
        </fr:view>
    
        <bean:define id="vowelId" name="vowel" property="person.idInternal"/>
        
        <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=%s&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", vowelId, dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson"/>
        </html:link>
        , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=%s&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", vowelId, dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson"/>
        </html:link>
    </logic:iterate>
</logic:notEmpty>
