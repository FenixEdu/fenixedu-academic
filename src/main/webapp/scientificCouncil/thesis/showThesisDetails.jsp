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
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisFile"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%>
<%@page import="net.sourceforge.fenixedu.domain.Degree"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%>
<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<jsp:include page="viewThesisHeader.jsp"/>

<bean:define id="thesis" name="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>

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

<div style="margin-left: 35px; width: 90%;">
    <logic:equal name="thesis" property="submitted" value="true">
    	<html:link action="<%= "/manageSecondCycleThesis.do?method=approveProposal&amp;thesisOid=" + thesis.getExternalId() %>">
			<bean:message key="link.scientificCouncil.thesis.proposal.approve" />
		</html:link>
		|
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><html:link href="#rejectProposalDivA" onclick="document.getElementById('rejectProposalDiv').style.display='block'">
			<bean:message key="link.scientificCouncil.thesis.proposal.reject" />
		</html:link>
    </logic:equal>
    <logic:equal name="thesis" property="approved" value="true">
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><html:link href="#rejectProposalDivA" onclick="document.getElementById('rejectProposalDiv').style.display='block'">
			<bean:message key="link.scientificCouncil.thesis.proposal.disapprove"/>
		</html:link>
    </logic:equal>
	<logic:equal name="thesis" property="confirmed" value="true">
		<bean:define id="confirmApprove" type="java.lang.String">return confirm('<bean:message key="label.scientificCouncil.thesis.evaluation.approve.confirm" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>')</bean:define>
		<html:link action="<%= "/manageSecondCycleThesis.do?method=approveThesis&amp;thesisOid=" + thesis.getExternalId() %>"
				onclick="<%= confirmApprove %>">
			<bean:message key="title.scientificCouncil.thesis.evaluation.approve"/>
		</html:link>
	</logic:equal>
</div>

<div id="rejectProposalDiv" style="margin-left: 35px; width: 90%; display: none;">
   	<br/>
    <div class="warning0" style="padding: 1em">
        <p class="mtop0 mbottom1">
            <strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/>
            <bean:message key="label.scientificCouncil.thesis.proposal.reject.confirm"/>
        </p>

	    <fr:form action="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>">
	        <fr:edit id="thesisRejection" name="thesis" schema="thesis.rejection.comment">
	            <fr:layout name="tabular">
		           <fr:property name="classes" value="thtop thlight mbottom0"/>
		           <fr:property name="columnClasses" value="width125px,,tdclear tderror1"/>
	            </fr:layout>
	            <fr:destination name="cancel" path="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"/>
	            <fr:destination name="invalid" path="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"/>
	        </fr:edit>

	        <table class="mtop0 tgluetop">
	        <tr>
		        <td class="width125px">
		        </td>
		        <td>
		            <html:submit>
		                <bean:message key="button.submit"/>
		            </html:submit>
		            <html:cancel>
		                <bean:message key="button.cancel"/>
		            </html:cancel>
		        </td>
	        </tr>
	        </table>
	    </fr:form>
    </div>
</div>

<%-- Dissertation Details --%>
<h3 class="separator2"><bean:message key="title.scientificCouncil.thesis.evaluated.view"/></h3>

<div style="margin-left: 35px; width: 90%;">
	<html:link action="<%= "/manageSecondCycleThesis.do?method=editThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>">
		<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.edit.thesis.details"/>
	</html:link>
	|
	<html:link action="<%= "/manageSecondCycleThesis.do?method=downloadIdentificationSheet&amp;thesisOid=" + thesis.getExternalId() %>">
		<bean:message key="link.student.thesis.identification.download" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</div>

<%
	final List<Locale> languages = thesis.getLanguages();
%>
<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
	<tr>
		<th>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.language"/>
		</th>
		<%
			for (final Locale language : languages) {
		%>
				<th>
					<bean:message bundle="LANGUAGE_RESOURCES" key="<%= "language." + language %>"/>
				</th>
		<%
			}
		%>
	</tr>
	<tr>
		<th>
			<bean:message bundle="STUDENT_RESOURCES" key="finalDegreeWorkProposalHeader.title"/>
		</th>
		<%
			for (final Locale language : languages) {
			    final MultiLanguageString mls = thesis.getTitle();
			    final String string = mls == null ? null : mls.getContent(language);
		%>
				<td>
					<%= string == null ? "" : string %>
				</td>
		<%
			}
		%>
	</tr>
	<tr>
		<th>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.keywords"/>
		</th>
		<%
			for (final Locale language : languages) {
			    final MultiLanguageString mls = thesis.getKeywords();
			    final String string = mls == null ? null : mls.getContent(language);
		%>
				<td>
					<%= string == null ? "" : string %>
				</td>
		<%
			}
		%>
	</tr>
	<tr>
		<th>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.abstract"/>
		</th>
		<%
			for (final Locale language : languages) {
			    final MultiLanguageString mls = thesis.getThesisAbstract();
			    final String string = mls == null ? null : mls.getContent(language);
		%>
				<td>
					<%= string == null ? "" : string %>
				</td>
		<%
			}
		%>
	</tr>
</table>

<%
	if (thesis.hasDissertation()) {
%>
<div style="margin-left: 35px; width: 90%;">
	<logic:equal name="thesis" property="visibility" value="<%= net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType.INTRANET.toString() %>">
		<html:link action="<%= "/manageSecondCycleThesis.do?method=changeThesisFilesVisibility&amp;thesisOid=" + thesis.getExternalId() %>">
			<bean:message key="link.coordinator.thesis.edit.changeVisibilityToPublic" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
   	    </html:link>
	</logic:equal>
	<logic:equal name="thesis" property="visibility" value="<%= net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType.PUBLIC.toString() %>">
		<html:link action="<%= "/manageSecondCycleThesis.do?method=changeThesisFilesVisibility&amp;thesisOid=" + thesis.getExternalId() %>">
			<bean:message key="link.coordinator.thesis.edit.changeVisibilityToPrivate" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
   	    </html:link>
	</logic:equal>
	|
	<%
		if (thesis.areThesisFilesReadable()) {
	%>
			<bean:define id="confirmUnavailable" type="java.lang.String">return confirm('<bean:message key="message.thesis.make.documents.unavailable" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>')</bean:define>	
			<html:link action="<%= "/manageSecondCycleThesis.do?method=makeDocumentUnavailable&amp;thesisOid=" + thesis.getExternalId() %>"
					onclick="<%= confirmUnavailable %>">
				<bean:message key="link.thesis.make.documents.unavailable"/>
			</html:link>
	<%
		} else {
	%>
			<bean:define id="confirmAvailable" type="java.lang.String">return confirm('<bean:message key="message.thesis.make.documents.available" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>')</bean:define>
			<html:link action="<%= "/manageSecondCycleThesis.do?method=makeDocumentAvailable&amp;thesisOid=" + thesis.getExternalId() %>"
					onclick="<%= confirmAvailable %>">
				<bean:message key="link.thesis.make.documents.available"/>
			</html:link>
	<%
		}
	%>
</div>
<%
	}
%>

<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
	<tr>
		<th>
			<bean:message key="title.scientificCouncil.thesis.evaluation.extendedAbstract"/>
			<%
				if (thesis.hasDissertation() && !thesis.areThesisFilesReadable()) {
			%>
					&nbsp;&nbsp;&nbsp;
					<em>
						<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><html:link href="#thesisDissertationFileBeanDivA" onclick="document.getElementById('thesisDissertationFileBeanDiv').style.display='block'">
							<bean:message key="link.thesis.substitute.extended.abstract"/>
						</html:link>
					</em>
			<%
				}
			%>
		</th>
		<th>
			<bean:message key="title.scientificCouncil.thesis.evaluation.dissertation"/>
			<%
				if (thesis.hasDissertation() && !thesis.areThesisFilesReadable()) {
			%>
					&nbsp;&nbsp;&nbsp;
					<em>
						<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><html:link href="#thesisExtendendAbstractFileBeanDivA" onclick="document.getElementById('thesisExtendendAbstractFileBeanDiv').style.display='block'">
							<bean:message key="link.thesis.substitute.extended.abstract"/>
						</html:link>
					</em>
			<%
				}
			%>
		</th>
	</tr>
	<tr>
		<td>
			<logic:empty name="thesis" property="extendedAbstract">
    			<bean:message key="label.scientificCouncil.thesis.evaluation.noExtendedAbstract"/>
			</logic:empty>

			<logic:notEmpty name="thesis" property="extendedAbstract">
    			<fr:view name="thesis" property="extendedAbstract" layout="values" schema="coordinator.thesis.file"/>
    			(<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>)
			</logic:notEmpty>
		</td>
		<td>
			<logic:empty name="thesis" property="dissertation">
    			<bean:message key="label.scientificCouncil.thesis.evaluation.noDissertation"/>
			</logic:empty>

			<logic:notEmpty name="thesis" property="dissertation">
    			<fr:view name="thesis" property="dissertation" layout="values" schema="coordinator.thesis.file"/>
    			(<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>)
			</logic:notEmpty>
		</td>
	</tr>
</table>

<logic:present name="thesisDissertationFileBean">
	<div id="thesisDissertationFileBeanDiv" style="margin-left: 35px; width: 90%; display: none;">
		
		<div class="infoop2 mvert15">
    		<p>
    			<bean:message key="label.student.thesis.upload.extended.abstract.message"/>
    		</p>
		</div>

		<fr:form encoding="multipart/form-data" action="<%= "/manageSecondCycleThesis.do?method=substituteExtendedAbstract&amp;thesisOid=" + thesis.getExternalId() %>">
    		<fr:edit id="thesisDissertationFileBean" name="thesisDissertationFileBean" schema="student.thesisBean.upload.dissertation">
        		<fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
        		</fr:layout>
        
    	    	<fr:destination name="cancel" path="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"/>
    		</fr:edit>
    
	    	<html:submit>
    	    	<bean:message key="button.submit"/>
    		</html:submit>
    		<html:cancel>
	        	<bean:message key="button.cancel"/>
	    	</html:cancel>
		</fr:form>
	</div>
</logic:present>

<logic:present name="thesisExtendendAbstractFileBean">
	<div id="thesisExtendendAbstractFileBeanDiv" style="margin-left: 35px; width: 90%; display: none;">
		
		<div class="infoop2 mvert15">
    		<p>
    			<bean:message key="label.student.thesis.upload.dissertation.message"/>
    		</p>
		</div>

		<fr:form encoding="multipart/form-data" action="<%= "/manageSecondCycleThesis.do?method=substituteDissertation&amp;thesisOid=" + thesis.getExternalId() %>">
    		<fr:edit id="thesisExtendendAbstractFileBean" name="thesisExtendendAbstractFileBean" schema="student.thesisBean.upload">
        		<fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
        		</fr:layout>
        
    	    	<fr:destination name="cancel" path="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"/>
    		</fr:edit>
    
	    	<html:submit>
    	    	<bean:message key="button.submit"/>
    		</html:submit>
    		<html:cancel>
		        <bean:message key="button.cancel"/>
	    	</html:cancel>
		</fr:form>
	</div>
</logic:present>

<jsp:include page="viewThesisOrientation.jsp"/>

<jsp:include page="viewThesisJury.jsp"/>

<h3 class="separator2 mtop2">
	<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="comment"/>
</h3>
<p>
	<%= thesis.getComment() == null ? "" : thesis.getComment() %>
</p>

<h3 class="separator2 mtop2">
	<bean:message key="label.thesis.operation.title" bundle="STUDENT_RESOURCES"/>
</h3>

	<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
		<thead>
		<tr>
			<th><bean:message key="label.thesis.operation.operation"  bundle="STUDENT_RESOURCES" /></th>
			<th></th>
			<th><bean:message key="label.thesis.operation.how" bundle="STUDENT_RESOURCES"/></th>
			<th><bean:message key="label.username" bundle="APPLICATION_RESOURCES"/></th>
			<th><bean:message key="label.thesis.operation.date" bundle="STUDENT_RESOURCES"/></th>
		</tr>
	</thead>
	<tbody> 
		<logic:present name="thesis" property="creation"> 		
		<tr>
			<bean:define id="dateCreator" name="thesis" property="creation" />
			<td><bean:message key="label.thesis.operation.creation" bundle="STUDENT_RESOURCES"/></td>
			<td>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=<bean:write name="thesis" property="creator.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</td>
			<td><bean:write name="thesis" property="creator.personName" /> </td>
			<td><bean:write name="thesis" property="creator.person.username" /></td>
			<td><%=((org.joda.time.DateTime)dateCreator).toString("dd/MM/yyyy hh:mm")%> </td>
		</tr>
	</logic:present>
	<logic:present name="thesis" property="submission" >
	<logic:present name="thesis" property="submitter" >  
		<tr>
		<bean:define id="dateSubmission" name="thesis" property="submission" />
			<td><bean:message key="label.thesis.operation.submission"  bundle="STUDENT_RESOURCES"/></td>
			<td>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=<bean:write name="thesis" property="submitter.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</td>
			<td><bean:write name="thesis" property="submitter.personName"/></td>
			<td><bean:write name="thesis" property="submitter.person.username"/></td>
			<td><%=((org.joda.time.DateTime)dateSubmission).toString("dd/MM/yyyy hh:mm")%> </td>
		</tr>
	</logic:present>
	</logic:present>
	<logic:present name="thesis" property="confirmation" > 
		<tr>
			<bean:define id="dateConfirmation" name="thesis" property="confirmation" />
			<td><bean:message key="label.thesis.operation.confirmation" bundle="STUDENT_RESOURCES" /></td>
			<td>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=<bean:write name="thesis" property="confirmer.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</td>
			<td><bean:write name="thesis" property="confirmer.personName"/></td>
			<td><bean:write name="thesis" property="confirmer.person.username"/></td>
			<td><%=((org.joda.time.DateTime)dateConfirmation).toString("dd/MM/yyyy hh:mm")%> </td>
		</tr>
	</logic:present>	
	<logic:present name="thesis" property="approval">
	<logic:present name="thesis" property="proposalApprover">
		<tr>
			<bean:define id="dateApproval" name="thesis" property="approval" />
			<td><bean:message key="label.thesis.operation.approval" bundle="STUDENT_RESOURCES" /></td>
			<td>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=<bean:write name="thesis" property="proposalApprover.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</td>
			<td><bean:write name="thesis" property="proposalApprover.personName"/></td>
			<td><bean:write name="thesis" property="proposalApprover.person.username"/></td>
			<td><%=((org.joda.time.DateTime)dateApproval).toString("dd/MM/yyyy hh:mm")%> </td>
		</tr>
	</logic:present>
	</logic:present>	
	</tbody>
	</table>
