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

<h2><bean:message key="title.coordinator.thesis.evaluated.view"/></h2>

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
     	<html:link page="<%= String.format("/manageThesis.do?method=viewOperationsThesis&degreeCurricularPlanID=%s&executionYear=%s&thesisID=%s",dcpId,executionYearId,thesisId)%>">
            <bean:message key="link.thesis.operation" bundle="STUDENT_RESOURCES"/>
      	</html:link>
     </li> 	
</ul>

<%-- Dissertation --%>
<h3 class="mtop15 mbottom05"><bean:message key="title.coordinator.thesis.confirm.details"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        <fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.abstract"/></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <bean:message key="label.thesis.abstract.empty"/>
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

<h3><bean:message key="label.thesis.keywords"/></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages" value="true">
    <bean:message key="label.thesis.keywords.empty"/>
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

<h3><bean:message key="title.coordinator.thesis.revision.gradeAndDate"/></h3>

<fr:view name="thesis" schema="coordinator.thesis.revision.view">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        <fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<%-- Orientation --%>
<h3 class="separator2 mtop15"><bean:message key="title.coordinator.thesis.edit.section.orientation"/></h3>

<logic:empty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
            <em><bean:message key="title.coordinator.thesis.edit.orientation.empty"/></em>
        </p>
    </logic:empty>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation.orientator"/></h4>
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
	<h4 class="mtop2 mbottom05"><bean:message key="title.coordinator.thesis.edit.section.orientation.coorientator"/></h4>
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

<%-- Jury --%>
<h3 class="separator2 mtop15"><bean:message key="title.coordinator.thesis.edit.section.jury"/></h3>

<%-- Jury/President --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <bean:message key="title.coordinator.thesis.edit.president.empty"/>
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
<h4><bean:message key="title.coordinator.thesis.edit.section.vowels"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <bean:message key="title.coordinator.thesis.edit.vowels.empty"/>
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

