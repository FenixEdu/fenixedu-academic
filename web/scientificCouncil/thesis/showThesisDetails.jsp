<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisFile"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%>
<%@page import="net.sourceforge.fenixedu.domain.Degree"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%>
<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<jsp:include page="viewThesisHeader.jsp"/>

<bean:define id="thesis" name="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>

<div style="margin-left: 35px; width: 90%;">
	<html:link action="<%= "/manageSecondCycleThesis.do?method=editThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>">
		<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.edit.thesis.details"/>
	</html:link>
	|
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
</div>


<%-- Dissertation Details --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.evaluated.view"/></h3>

<%
	final List<Language> languages = thesis.getLanguages();
%>
<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
	<tr>
		<th>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.language"/>
		</th>
		<%
			for (final Language language : languages) {
		%>
				<th>
					<bean:message bundle="LANGUAGE_RESOURCES" key="<%= "language." + language.name() %>"/>
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
			for (final Language language : languages) {
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
			for (final Language language : languages) {
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
			for (final Language language : languages) {
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

<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
	<tr>
		<th>
			<bean:message key="title.scientificCouncil.thesis.evaluation.extendedAbstract"/>
		</th>
		<th>
			<bean:message key="title.scientificCouncil.thesis.evaluation.dissertation"/>
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
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="thesis" property="creator.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</td>
			<td><bean:write name="thesis" property="creator.personName" /> </td>
			<td><bean:write name="thesis" property="creator.person.username" /></td>
			<td><%=((org.joda.time.DateTime)dateCreator).toString("dd/MM/yyyy hh:mm")%> </td>
		</tr>
	</logic:present>
		<logic:present name="thesis" property="submission" > 
		<tr>
		<bean:define id="dateSubmission" name="thesis" property="submission" />
			<td><bean:message key="label.thesis.operation.submission"  bundle="STUDENT_RESOURCES"/></td>
			<td>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="thesis" property="submitter.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</td>
			<td><bean:write name="thesis" property="submitter.personName"/></td>
			<td><bean:write name="thesis" property="submitter.person.username"/></td>
			<td><%=((org.joda.time.DateTime)dateSubmission).toString("dd/MM/yyyy hh:mm")%> </td>
		</tr>
	</logic:present>	

	<logic:present name="thesis" property="confirmation" > 
		<tr>
			<bean:define id="dateConfirmation" name="thesis" property="confirmation" />
			<td><bean:message key="label.thesis.operation.confirmation" bundle="STUDENT_RESOURCES" /></td>
			<td>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="thesis" property="confirmer.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</td>
			<td><bean:write name="thesis" property="confirmer.personName"/></td>
			<td><bean:write name="thesis" property="confirmer.person.username"/></td>
			<td><%=((org.joda.time.DateTime)dateConfirmation).toString("dd/MM/yyyy hh:mm")%> </td>
		</tr>
	</logic:present>	
	<logic:present name="thesis" property="approval"> 
		<tr>
			<bean:define id="dateApproval" name="thesis" property="approval" />
			<td><bean:message key="label.thesis.operation.approval" bundle="STUDENT_RESOURCES" /></td>
			<td>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="thesis" property="proposalApprover.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</td>
			<td><bean:write name="thesis" property="proposalApprover.personName"/></td>
			<td><bean:write name="thesis" property="proposalApprover.person.username"/></td>
			<td><%=((org.joda.time.DateTime)dateApproval).toString("dd/MM/yyyy hh:mm")%> </td>
		</tr>
	</logic:present>	
	</tbody>
	</table>
