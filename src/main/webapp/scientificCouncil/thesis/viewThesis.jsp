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
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="thesisOid" name="thesis" property="externalId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>
<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml/>

<em><bean:message key="scientificCouncil.thesis.process" /></em>
<h2><bean:message key="title.scientificCouncil.thesis.evaluated.view"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="mail">
    <html:messages id="message" message="true" property="mail">
        <p><span class="warning0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<ul>
	<logic:notEmpty name="degreeId"><logic:notEmpty name="executionYearId">
    <li>
		<bean:define id="url">/scientificCouncilManageThesis.do?method=listScientificComission&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
		<html:link page="<%= url %>">
			<bean:message key="link.list.scientific.comission"/>
		</html:link>
    </li>
    </logic:notEmpty></logic:notEmpty>
    <li>
		<bean:define id="url">/scientificCouncilManageThesis.do?method=listThesis&amp;degreeID=<bean:write name="degreeId"/>&amp;executionYearID=<bean:write name="executionYearId"/></bean:define>
		<html:link page="<%= url %>">
            <bean:message key="link.scientificCouncil.thesis.list.back"/>
        </html:link>
    </li>
	<logic:present name="thesis" property="dissertation">
		<logic:present name="containsThesisFileReadersGroup">
    		<li>
				<bean:define id="url">/scientificCouncilManageThesis.do?method=showMakeDocumentUnavailablePage&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/>&amp;thesisID=<bean:write name="thesisId"/></bean:define>
				<html:link page="<%= url %>">
					<bean:message key="link.thesis.make.documents.unavailable"/>
				</html:link>
    		</li>
    	</logic:present>
		<logic:notPresent name="containsThesisFileReadersGroup">
    		<li>
				<bean:define id="url">/scientificCouncilManageThesis.do?method=showMakeDocumentsAvailablePage&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/>&amp;thesisID=<bean:write name="thesisId"/></bean:define>
				<html:link page="<%= url %>">
					<bean:message key="link.thesis.make.documents.available"/>
				</html:link>
    		</li>
    		<li>
				<bean:define id="url">/scientificCouncilManageThesis.do?method=showSubstituteDocumentsPage&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/>&amp;thesisID=<bean:write name="thesisId"/></bean:define>
				<html:link page="<%= url %>">
					<bean:message key="link.thesis.substitute.documents"/>
				</html:link>
    		</li>
    		<li>
				<bean:define id="url">/scientificCouncilManageThesis.do?method=showSubstituteExtendedAbstractPage&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/>&amp;thesisID=<bean:write name="thesisId"/></bean:define>
				<html:link page="<%= url %>">
					<bean:message key="link.thesis.substitute.extended.abstract"/>
				</html:link>
    		</li>
    	</logic:notPresent>
   		<li>
        	<html:link href="<%= request.getContextPath() + String.format("/coordinator/manageThesis.do?method=printApprovalDocument&amp;executionYearId=%s&amp;thesisID=%s", executionYearId, thesisOid) %>">
		        <bean:message bundle="APPLICATION_RESOURCES" key="label.coordinator.list.submitted.thesis.reprint"/>
	    	</html:link>
   		</li>
    </logic:present>
    <li>
     	<html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=viewOperationsThesis&thesisID=%s",thesisId)%>">
            <bean:message key="link.thesis.operation" bundle="STUDENT_RESOURCES" />
      	</html:link>
     </li> 	
    
</ul>

<%-- Approve proposal --%>
<logic:present name="showMakeDocumentUnavailablePage">
    <div class="warning0" style="padding: 1em;">
        <strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/>
        <bean:message key="message.thesis.make.documents.unavailable"/>
        <div class="mtop1 forminline">
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=makeDocumentUnavailablePage&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <html:submit>
                    <bean:message key="button.scientificCouncil.thesis.documents.make.unavailable"/>
                </html:submit>
            </fr:form>
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=viewThesis&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <html:cancel>
                    <bean:message key="button.cancel"/>
                </html:cancel>
            </fr:form>
        </div>
    </div>
</logic:present>
<logic:present name="showMakeDocumentsAvailablePage">
    <div class="warning0" style="padding: 1em;">
        <strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/>
        <bean:message key="message.thesis.make.documents.available"/>
        <div class="mtop1 forminline">
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=makeDocumentAvailablePage&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <html:submit>
                    <bean:message key="button.scientificCouncil.thesis.documents.make.available"/>
                </html:submit>
            </fr:form>
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=viewThesis&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <html:cancel>
                    <bean:message key="button.cancel"/>
                </html:cancel>
            </fr:form>
        </div>
    </div>
</logic:present>
<logic:present name="showSubstituteDocumentsPage">
	<div class="infoop2 mvert15">
    	<p>
        	<bean:message key="label.student.thesis.upload.dissertation.message"/>
    	</p>
	</div>

	<fr:form encoding="multipart/form-data" action="<%= String.format("/scientificCouncilManageThesis.do?method=substituteDocuments&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
    	<fr:edit id="dissertationFile" name="fileBean" schema="student.thesisBean.upload.dissertation">
        	<fr:layout name="tabular">
            	<fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
        	</fr:layout>
        
    	    <fr:destination name="cancel" path="<%= String.format("/scientificCouncilManageThesis.do?method=viewThesis&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
    	</fr:edit>
    
	    <html:submit>
    	    <bean:message key="button.submit"/>
    	</html:submit>
    	<html:cancel>
	        <bean:message key="button.cancel"/>
	    </html:cancel>
	</fr:form>
</logic:present>

<logic:present name="showSubstituteExtendedAbstractPage">
	<div class="infoop2 mvert15">
    	<p>
        	<bean:message key="label.student.thesis.upload.extended.abstract.message"/>
    	</p>
	</div>

	<fr:form encoding="multipart/form-data" action="<%= String.format("/scientificCouncilManageThesis.do?method=substituteExtendedAbstract&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
    	<fr:edit id="extendedAbstractFile" name="fileBean" schema="student.thesisBean.upload">
        	<fr:layout name="tabular">
            	<fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
        	</fr:layout>
        
    	    <fr:destination name="cancel" path="<%= String.format("/scientificCouncilManageThesis.do?method=viewThesis&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
    	</fr:edit>
    
	    <html:submit>
    	    <bean:message key="button.submit"/>
    	</html:submit>
    	<html:cancel>
	        <bean:message key="button.cancel"/>
	    </html:cancel>
	</fr:form>
</logic:present>


<%-- Dissertation --%>

<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.evaluation.details"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>
<html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changeInformationWithDocs&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
    <bean:message key="link.coordinator.thesis.edit.changeInformation"  bundle="APPLICATION_RESOURCES"/>
</html:link>

<%-- general process information --%>
<div class="infoop2 mtop1">
	<bean:define id="stateKey" type="java.lang.String">ThesisPresentationState.<bean:write name="thesisPresentationState" property="name"/>.label</bean:define>
	<p class="mvert0">
		<strong><bean:message bundle="APPLICATION_RESOURCES" key="<%= stateKey %>"/></strong>
	</p>
</div>

<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.abstract"/></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <bean:message key="label.thesis.abstract.empty"/>
</logic:notEqual>

<logic:equal name="thesis" property="thesisAbstractInBothLanguages" value="true">
	<logic:notPresent name="editThesisAbstract">
	    <div style="border: 1px solid #ddd; background: #fafafa; padding: 0.5em; margin-bottom: 1em;">
    	    <fr:view name="thesis" property="thesisAbstract">
        	    <fr:layout>
            	    <fr:property name="language" value="pt"/>
                	<fr:property name="showLanguageForced" value="true"/>
            	</fr:layout>
        	</fr:view>
    	</div>

	    <div style="border: 1px solid #ddd; background: #fafafa; padding: 0.5em; margin-bottom: 1em;">
			<fr:view name="thesis" property="thesisAbstract">
				<fr:layout>
					<fr:property name="language" value="en"/>
					<fr:property name="showLanguageForced" value="true"/>
				</fr:layout>
			</fr:view>
		</div>

		<logic:notPresent name="containsThesisFileReadersGroup">
			<bean:define id="url">/scientificCouncilManageThesis.do?method=editThesisAbstract&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/>&amp;thesisID=<bean:write name="thesisId"/></bean:define>
			<html:link page="<%= url %>">
				<bean:message key="link.edit"/>
			</html:link>
   		</logic:notPresent>
	</logic:notPresent>
	<logic:present name="editThesisAbstract">
		<bean:define id="url">/scientificCouncilManageThesis.do?method=viewThesis&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/>&amp;thesisID=<bean:write name="thesisId"/></bean:define>
   	    <fr:edit id="editThesisAbstract" name="thesis" action="<%= url %>">
			<fr:schema type="net.sourceforge.fenixedu.domain.thesis.Thesis" bundle="STUDENT_RESOURCES">
    			<fr:slot name="thesisAbstractPt" layout="longText" key="label.thesis.abstract.pt">
        			<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
            			<fr:property name="type" value="word"/>
            			<fr:property name="length" value="250"/>
        			</fr:validator>
         			<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
        			<fr:property name="columns" value="65"/>
        			<fr:property name="rows" value="12"/>
    			</fr:slot>
    			<fr:slot name="thesisAbstractEn" layout="longText" key="label.thesis.abstract.en">
        			<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
            			<fr:property name="type" value="word"/>
            			<fr:property name="length" value="250"/>
        			</fr:validator>
        			<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
        			<fr:property name="columns" value="65"/>
        			<fr:property name="rows" value="12"/>
    			</fr:slot>
			</fr:schema>
		    <fr:layout name="tabular">
        		<fr:property name="classes" value="tstyle5 thlight thright mtop05 tdtop"/>
        		<fr:property name="columnClasses" value=",,tdclear tderror1 "/>
    		</fr:layout>
       	</fr:edit>
	</logic:present>
</logic:equal>





















<h3 class="mtop15 mbottom05"><bean:message key="label.thesis.keywords"/></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages" value="true">
	<p>
		<em><bean:message key="label.thesis.keywords.empty"/></em>
    </p>
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

<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.evaluation.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <bean:message key="label.scientificCouncil.thesis.evaluation.noExtendedAbstract"/>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>)
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.evaluation.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <bean:message key="label.scientificCouncil.thesis.evaluation.noDissertation"/>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>)
</logic:notEmpty>

<logic:equal name="thesis" property="visibility" value="<%= net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType.INTRANET.toString() %>">
	<p>
		<html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changeThesisFilesVisibility&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
			<bean:message key="link.coordinator.thesis.edit.changeVisibilityToPublic" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
   	    </html:link>
	</p>
</logic:equal>
<logic:equal name="thesis" property="visibility" value="<%= net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType.PUBLIC.toString() %>">
	<p>
		<html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changeThesisFilesVisibility&amp;&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
			<bean:message key="link.coordinator.thesis.edit.changeVisibilityToPrivate" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
   	    </html:link>
	</p>
</logic:equal>

<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.evaluation.gradeAndDate"/></h3>

<fr:view name="thesis" schema="coordinator.thesis.revision.view">
    <fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<%-- Orientation --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.orientation"/></h3>

<logic:empty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
            <em><bean:message key="title.scientificCouncil.thesis.review.orientation.empty"/></em>
        </p>
    </logic:empty>
</logic:empty>

<logic:empty name="thesis" property="orientator">
    <p>
 	   <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changePerson&amp;target=orientator&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
	   		<bean:message key="link.coordinator.thesis.edit.addOrientation" bundle="APPLICATION_RESOURCES"/>
	   </html:link>
    </p>
</logic:empty>
<logic:notEmpty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
	        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changePerson&amp;target=coorientator&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
                <bean:message key="link.coordinator.thesis.edit.addCoorientation" bundle="APPLICATION_RESOURCES"/>
    	    </html:link>
        </p>
    </logic:empty>
</logic:notEmpty>

<logic:notEmpty name="thesis" property="orientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.orientation.orientator"/></h4>
    <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
            <fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
    <logic:equal name="thesis" property="orientatorCreditsDistributionNeeded" value="true">
        <logic:present name="editOrientatorCreditsDistribution">
            <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
                <tr>
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits" bundle="APPLICATION_RESOURCES"/>:</th>
                    <td class="width35em">
                        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=editProposal&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
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
        	<table class="tstyle2 thlight thright mtop0 mbottom05 tglue	top">
	            <tr>
    	            <th class="width12em"><bean:message key="label.scientificCouncil.thesis.edit.teacher.credits"/>:</th>
        	        <td class="width35em">
            	        <logic:empty name="thesis" property="orientatorCreditsDistribution">-</logic:empty>
                	    <logic:notEmpty name="thesis" property="orientatorCreditsDistribution">
                    	    <fr:view name="thesis" property="orientatorCreditsDistribution"/> %
                    	</logic:notEmpty>
              	        	&nbsp;&nbsp;&nbsp;&nbsp;
                  	    	(<html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changeCredits&amp;target=orientator&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
                       	    	<bean:message key="label.change" bundle="APPLICATION_RESOURCES"/>
                       		</html:link>)
                	</td>
            	</tr>
        	</table>
        </logic:notPresent>
    </logic:equal>

    <p class="mtop05">
        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changeParticipationInfo&amp;target=orientator&amp;remove=true&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson"  bundle="APPLICATION_RESOURCES"/>
        </html:link>,
        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changePerson&amp;target=orientator&amp;remove=true&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson"  bundle="APPLICATION_RESOURCES"/>
        </html:link>
    </p>
</logic:notEmpty>
  
<logic:notEmpty name="thesis" property="coorientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.orientation.coorientator"/></h4>
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
            <fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
    <logic:equal name="thesis" property="coorientatorCreditsDistributionNeeded" value="true">
        <logic:present name="editCoorientatorCreditsDistribution">
            <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
                <tr>
                    <th class="width12em"><bean:message key="label.coordinator.thesis.edit.teacher.credits" bundle="APPLICATION_RESOURCES"/>:</th>
                    <td class="width35em">
                        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=editProposal&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
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
                	<th class="width12em"><bean:message key="label.scientificCouncil.thesis.edit.teacher.credits"/>:</th>
                	<td class="width35em">
	                    <logic:empty name="thesis" property="coorientatorCreditsDistribution">-</logic:empty>
    	                <logic:notEmpty name="thesis" property="coorientatorCreditsDistribution">
        	                <fr:view name="thesis" property="coorientatorCreditsDistribution"/> %
            	        </logic:notEmpty>
                	        &nbsp;&nbsp;&nbsp;&nbsp;
                    	    (<html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changeCredits&amp;target=coorientator&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
                        	    <bean:message key="label.change" bundle="APPLICATION_RESOURCES"/>
                        	</html:link>)
                	</td>
            	</tr>
        	</table>
        </logic:notPresent>
    </logic:equal>
    <p class="mtop05">
        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changeParticipationInfo&amp;target=coorientator&amp;remove=true&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson" bundle="APPLICATION_RESOURCES"/>
        </html:link>,
        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changePerson&amp;target=coorientator&amp;remove=true&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson" bundle="APPLICATION_RESOURCES"/>
        </html:link>
    </p>
</logic:notEmpty>


<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.jury"/></h3>

<%-- Jury/President --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.president.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
        		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        		<fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
<%--
	<bean:define id="urlChangePerson">/scientificCouncilManageThesis.do?method=changeParticipationInfo&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
    <html:link page="<%= String.format(urlChangePerson + "&amp;target=president&amp;thesisID=%s", thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.changePerson" bundle="APPLICATION_RESOURCES"/>
    </html:link>
    <bean:define id="urlRemovePerson">/scientificCouncilManageThesis.do?method=changePerson&amp;remove=true&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
    <html:link page="<%= String.format(urlRemovePerson + "&amp;thesisID=%s", thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.removePerson" bundle="APPLICATION_RESOURCES"/>
    </html:link>
 --%>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.vowels"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.vowels.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels" type="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant">
        <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
            <fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            		<fr:property name="columnClasses" value="width12em,width35em,"/>
            </fr:layout>
        </fr:view>
        
        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=%s&amp;remove=true&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", vowel.getExternalId(), degreeId, executionYearId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson" bundle="APPLICATION_RESOURCES"/>
        </html:link>
    </logic:iterate>
</logic:notEmpty>
