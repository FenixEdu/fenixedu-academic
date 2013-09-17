<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%>

<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.submission"/></h2>

<ul>
    <li>
        <html:link page="/thesisSubmission.do?method=downloadIdentificationSheet"
		        paramId="thesisId" paramName="thesis" paramProperty="externalId">
            <bean:message key="link.student.thesis.identification.download"/>
        </html:link>
    </li>
</ul>

<%-- pending items --%>
<logic:notEmpty name="todo">
    <div class="warning0" style="padding: 1em;">
        <strong><bean:message key="label.attention"/>:</strong><br/>
        <ul class="mbottom05">
            <logic:iterate id="condition" name="todo">
                <li>
                    <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
                    <bean:message key="<%= key %>"/>
                </li>
            </logic:iterate>
        </ul>
    </div>
</logic:notEmpty>

<logic:empty name="todo">
	<div class="infoop2">
		<p class="mvert0"><bean:message key="message.thesis.process.instructions"/></p>
	</div>
</logic:empty>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.details"/></h3>

<logic:notPresent name="changeDetails">
    <fr:view name="thesis" schema="student.thesis.details">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            <fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
    
    <html:link page="/thesisSubmission.do?method=changeThesisDetails"
    		paramId="thesisId" paramName="thesis" paramProperty="externalId">
        <bean:message key="link.student.thesis.submit.edit"/>
    </html:link>
</logic:notPresent>

<bean:define id="callbackUrl" type="java.lang.String">/thesisSubmission.do?method=prepareThesisSubmission&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>
<bean:define id="invalidUrl" type="java.lang.String">/thesisSubmission.do?method=changeThesisDetails&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>

<logic:present name="changeDetails">
    <fr:form action="<%= callbackUrl %>">
        <fr:edit id="details" name="thesis" schema="student.thesis.details.edit">
            <fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            		<fr:property name="columnClasses" value="width12em,,tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="<%= invalidUrl %>"/>
        </fr:edit>
        
        <html:submit>
            <bean:message key="button.submit"/>
        </html:submit>
        <html:cancel>
            <bean:message key="button.cancel"/>
        </html:cancel>
    </fr:form>
</logic:present>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.abstract"/></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <p class="mvert05"><em><bean:message key="label.thesis.abstract.empty" bundle="APPLICATION_RESOURCES"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <fr:view name="thesis" schema="student.thesis.details.abstract">
        <fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:equal>

<html:link page="/thesisSubmission.do?method=editAbstract"
		paramId="thesisId" paramName="thesis" paramProperty="externalId">
    <bean:message key="link.student.thesis.submit.edit"/>
</html:link>


<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.keywords"/></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages" value="true">
    <p class="mvert05"><em><bean:message key="label.thesis.keywords.empty" bundle="APPLICATION_RESOURCES"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="keywordsInBothLanguages" value="true">
    <fr:view name="thesis" schema="student.thesis.details.keywords">
        <fr:layout name="tabular">
        		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        		<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:equal>

<html:link page="/thesisSubmission.do?method=editKeywords"
		paramId="thesisId" paramName="thesis" paramProperty="externalId">
    <bean:message key="link.student.thesis.submit.edit"/>
</html:link>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.declaration"/></h3>

<fr:view name="thesis" schema="student.thesis.details.declaration">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        <fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<html:link page="/thesisSubmission.do?method=viewDeclaration"
		paramId="thesisId" paramName="thesis" paramProperty="externalId">
    <bean:message key="label.student.thesis.declaration.view"/>
</html:link>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.dissertation"/></h3>

<logic:notEqual name="thesis" property="declarationAccepted" value="true">
    <p class="mvert0"><em><bean:message key="label.student.thesis.file.declaration.notAccepted"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="declarationAccepted" value="true">
    <logic:empty name="thesis" property="dissertation">
        <p class="mvert0"><em><bean:message key="label.student.thesis.submit.noDissertation"/></em></p>
    </logic:empty>
    
    <logic:notEmpty name="thesis" property="dissertation">
        <fr:view name="thesis" property="dissertation" schema="student.thesis.file.dissertation">
            <fr:layout name="tabular">
    			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    			<fr:property name="columnClasses" value="width12em,,"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
    
    <p class="mtop05">
        <logic:notEmpty name="thesis" property="dissertation">
            <html:link page="/thesisSubmission.do?method=removeDissertation"
            		paramId="thesisId" paramName="thesis" paramProperty="externalId">
                <bean:message key="link.student.thesis.file.remove"/>
            </html:link>, 
        </logic:notEmpty>
        <html:link page="/thesisSubmission.do?method=prepareUploadDissertation"
		        paramId="thesisId" paramName="thesis" paramProperty="externalId">
            <bean:message key="link.student.thesis.file.upload"/>
        </html:link>
    </p>
</logic:equal>

<%-- 
<fr:form action="/thesisSubmission.do?method=uploadDissertation" encoding="multipart/form-data">
    <fr:edit id="dissertationFile" name="fileBean" schema="student.thesisBean.upload">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="submit"/>
    </html:submit>
</fr:form>
--%>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.extendedAbstract"/></h3>

<logic:notEqual name="thesis" property="declarationAccepted" value="true">
    <p class="mvert0"><em><bean:message key="label.student.thesis.file.declaration.notAccepted"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="declarationAccepted" value="true">
    <logic:empty name="thesis" property="extendedAbstract">
        <p class="mvert0"><em><bean:message key="label.student.thesis.submit.noExtendedAbstract"/></em></p>
    </logic:empty>
    
    <logic:notEmpty name="thesis" property="extendedAbstract">
        <fr:view name="thesis" property="extendedAbstract" schema="student.thesis.file">
            <fr:layout name="tabular">
    			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05 thmiddle"/>
    			<fr:property name="columnClasses" value="width12em,,"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
    
    <p class="mtop05">
        <logic:notEmpty name="thesis" property="extendedAbstract">
            <html:link page="/thesisSubmission.do?method=removeAbstract">
                <bean:message key="link.student.thesis.file.remove"/>
            </html:link>,
        </logic:notEmpty>
        <html:link page="/thesisSubmission.do?method=prepareUploadAbstract"
        		paramId="thesisId" paramName="thesis" paramProperty="externalId">
            <bean:message key="link.student.thesis.file.upload"/>
        </html:link>
    </p>
</logic:equal>

<%-- 
<fr:form action="/thesisSubmission.do?method=uploadAbstract" encoding="multipart/form-data">
    <fr:edit id="abstractFile" name="fileBean" schema="student.thesisBean.upload">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="submit"/>
    </html:submit>
</fr:form>
--%>




<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.jury" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<%-- Orientation --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.orientation" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h4>

<logic:empty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
            <em><bean:message key="title.scientificCouncil.thesis.review.orientation.empty" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
        </p>
    </logic:empty>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
            <fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
    <logic:equal name="thesis" property="orientatorCreditsDistributionNeeded" value="true">
        <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
            <tr>
                <th class="width12em"><bean:message key="label.scientificCouncil.thesis.edit.teacher.credits" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</th>
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
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
            <fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
    <logic:equal name="thesis" property="coorientatorCreditsDistributionNeeded" value="true">
        <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
            <tr>
                <th class="width12em"><bean:message key="label.scientificCouncil.thesis.edit.teacher.credits" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>:</th>
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
<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.jury.president" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.president.empty" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
        		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        		<fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.vowels" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.vowels.empty" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels">
        <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
            <fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            		<fr:property name="columnClasses" value="width12em,width35em,"/>
            </fr:layout>
        </fr:view>
    </logic:iterate>
</logic:notEmpty>
