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

<jsp:include page="styles.jsp"/>

<em><bean:message key="scientificCouncil"/></em>
<h2><bean:message key="title.scientificCouncil.thesis" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<bean:define id="thesis" name="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>
<%
	final Enrolment enrolment = thesis.getEnrolment();
	final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	final Degree degree = enrolment.getCurricularCourse().getDegree();
%>

<h3 class="mtop3 mbottom05">
	<bean:write name="thesis" property="student.person.name"/>
	<span class="color777" style="font-weight:normal;">(
	<bean:write name="thesis" property="student.person.username"/>
	)</span>
</h3>

<table>
	<tr>
		<td>
			<br/>
			<div style="border: 1px solid #ddd; padding: 8px; margin: 0 20px 20px 0;">
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="thesis" property="student.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</div> 
		</td>
		<td>
			<div id="operations" class="cf"> 
				<div class="grey-box" style="max-width: none;">
					<table>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.period"/>:
							</td>
							<td>
								<%= executionSemester.getQualifiedName() %>
							</td>
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.degree"/>:
							</td>
							<td>
								<%= degree.getPresentationName() %>
							</td>
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.state"/>:
							</td>
							<td>
								<%= ThesisPresentationState.getThesisPresentationState(thesis).getLabel() %>
							</td>
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.evaluate.discussion.date"/>:
							</td>
							<td>
								<%= thesis.getDiscussed() == null ? "" : thesis.getDiscussed().toString("yyyy-MM-dd HH:mm") %>
							</td>							
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.evaluate.mark"/>:
							</td>
							<td>
								<%= thesis.getMark() == null ? "" : thesis.getMark().toString() %>
							</td>							
						</tr>
					</table>
				</div> 
			</div>
		</td>
	</tr>
</table>

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

<br/>

<h4>
	<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="comment"/>
</h4>
<p>
	<%= thesis.getComment() %>
</p>