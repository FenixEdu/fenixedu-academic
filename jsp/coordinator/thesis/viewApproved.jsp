<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.confirm"/></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s", dcpId) %>">
            <bean:message key="title.coordinator.thesis.back"/>
        </html:link>
    </li>
</ul>

<%-- Dissertation --%>
<h3><bean:message key="title.coordinator.thesis.confirm.details"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<h3><bean:message key="label.thesis.abstract"/></h3>

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

<h3><bean:message key="label.thesis.keywords"/></h3>

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

<h3><bean:message key="title.coordinator.thesis.confirm.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <bean:message key="label.coordinator.thesis.confirm.noExtendedAbstract"/>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>)
</logic:notEmpty>

<h3><bean:message key="title.coordinator.thesis.confirm.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <bean:message key="label.coordinator.thesis.confirm.noDissertation"/>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>)
</logic:notEmpty>

<h3><bean:message key="title.coordinator.thesis.confirm.section"/></h3>

<p>
    <bean:message key="label.coordinator.thesis.confirm.message"/>
</p>

<logic:messagesPresent message="true">
    <html:messages id="message" message="true">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<fr:edit name="thesis" schema="coordinator.thesis.evaluate"
         action="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s", dcpId) %>">
    <fr:layout name="tabular">
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s", dcpId) %>"/>
</fr:edit>

<%-- Jury --%>
<h3><bean:message key="title.coordinator.thesis.edit.section.jury"/></h3>

<%-- Orientator --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.orientation.orientator"/></h4>

<logic:empty name="thesis" property="orientator">
    <p>
        <bean:message key="title.coordinator.thesis.edit.orientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <logic:empty name="thesis" property="orientator.externalPerson">
        <logic:empty name="thesis" property="orientator.teacher">
            <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:empty>
        <logic:notEmpty name="thesis" property="orientator.teacher">
            <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person.teacher">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:notEmpty>
    </logic:empty>
    <logic:notEmpty name="thesis" property="orientator.externalPerson">
        <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>

<%-- Coorientator --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.orientation.coorientator"/></h4>

<logic:empty name="thesis" property="coorientator">
    <p>
        <bean:message key="title.coordinator.thesis.edit.coorientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="coorientator">
    <logic:empty name="thesis" property="coorientator.externalPerson">
        <logic:empty name="thesis" property="coorientator.teacher">
            <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:empty>
        <logic:notEmpty name="thesis" property="coorientator.teacher">
            <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person.teacher">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:notEmpty>
    </logic:empty>
    <logic:notEmpty name="thesis" property="coorientator.externalPerson">
        <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>

<%-- Jury/President --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <bean:message key="title.coordinator.thesis.edit.president.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <logic:empty name="thesis" property="president.externalPerson">
        <logic:empty name="thesis" property="president.teacher">
            <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:empty>
        <logic:notEmpty name="thesis" property="president.teacher">
            <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person.teacher">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:notEmpty>
    </logic:empty>
    <logic:notEmpty name="thesis" property="president.externalPerson">
        <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.vowels"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <bean:message key="title.coordinator.thesis.edit.vowels.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels">
        <logic:empty name="vowel" property="externalPerson">
            <logic:empty name="vowel" property="teacher">
                <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                    </fr:layout>
                </fr:view>
            </logic:empty>
            <logic:notEmpty name="vowel" property="teacher">
                <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person.teacher">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                    </fr:layout>
                </fr:view>
            </logic:notEmpty>
        </logic:empty>
    </logic:iterate>
</logic:notEmpty>

