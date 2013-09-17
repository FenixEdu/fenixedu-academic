<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<p class="mvert15">
	<fr:form>
		<fr:edit id="executionSemesterBean" name="executionSemesterBean">
			<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.VariantBean">
				<fr:slot name="domainObject" key="label.inquiries.semester" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.ViewQUCResultsDA$ExecutionSemesterQucProvider" />
					<fr:property name="format" value="${executionYear.year} - ${semester}º Semestre" />
					<fr:property name="nullOptionHidden" value="true"/>
					<fr:property name="destination" value="showPostBack"/>
				</fr:slot>
			</fr:schema>
			<fr:layout>
				<fr:property name="classes" value="thlight thmiddle" />
				<fr:property name="showPostBack" value="/qucAudit.do?method=showAuditProcesses"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
</p>

<logic:notEmpty name="executionCoursesAudits">
	<table class="tstyle1">
		<tr>
			<th><bean:message key="header.inquiries.course.form" bundle="INQUIRIES_RESOURCES"/></th>
			<th><bean:message key="label.inquiry.audit.auditors" bundle="INQUIRIES_RESOURCES"/></th>
			<th/>
		</tr>
		<logic:iterate id="executionCourseAudit" name="executionCoursesAudits">
			<tr>
				<td>
					<bean:define id="ecSite" name="executionCourseAudit" property="executionCourse.site.reversePath" type="java.lang.String"/>
					<!-- NO_CHECKSUM --><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><html:link page="<%= ecSite %>" target="_blank" module="">
						<bean:write name="executionCourseAudit" property="executionCourse.name"/>
					</html:link>
				</td>				
				<td>
					<bean:write name="executionCourseAudit" property="teacherAuditor.person.name"/>, 
					<bean:write name="executionCourseAudit" property="studentAuditor.person.name"/>
				</td>
				<td>
					<html:link page="/qucAudit.do?method=viewProcessDetails" paramId="executionCourseAuditOID" paramName="executionCourseAudit" paramProperty="externalId">
						<bean:message key="label.view" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>