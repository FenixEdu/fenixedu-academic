<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<%@page import="net.sourceforge.fenixedu.domain.degree.DegreeType"%>
<%@page import="net.sourceforge.fenixedu.domain.Teacher"%>
<%@page import="net.sourceforge.fenixedu.domain.Employee"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Student"%>
<%@page import="net.sourceforge.fenixedu.domain.StudentCurricularPlan"%>
<%@page import="net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry"%>
<%@page import="net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch"%>
<%@page import="net.sourceforge.fenixedu.domain.RootDomainObject"%><html:xhtml/>

<em>Cartões de Identificação</em>
<h2><bean:message key="link.card.generation.search.people" /></h2>

<p><html:link page="/searchPeople.do?method=search">« Voltar</html:link></p>

<logic:present name="person">
	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
	<fr:view name="person" schema="card.generation.search.person.list">
		<fr:layout name="tabular">
<%--			<fr:property name="classes" value="tstyle1 thlight thleft thtop"/>
--%>
			<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
			<fr:property name="rowClasses" value=",bgcolorfafafa"/>
			<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>

			<fr:property name="link(view)" value="/searchPeople.do?method=viewPersonCards"/>
			<fr:property name="key(view)" value="label.view" />
			<fr:property name="param(view)" value="idInternal/personId" />
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
			<fr:property name="order(view)" value="1" />
		</fr:layout>
	</fr:view>

	<br/>
	<br/>

	<h3><bean:message key="label.card.generation.entry"/>:</h3>
		<fr:view name="person" property="cardGenerationEntries" schema="card.generation.person.card.list">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
				<fr:property name="rowClasses" value=",bgcolorfafafa"/>
				<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>

				<fr:property name="link(view)" value="/searchPeople.do?method=viewPersonCard"/>
				<fr:property name="key(view)" value="label.view" />
				<fr:property name="param(view)" value="idInternal/cardGenerationEntryId" />
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
				<fr:property name="order(view)" value="1" />

				<logic:present role="MANAGER">
					<fr:property name="link(delete)" value="/manageCardGeneration.do?method=deletePersonCard"/>
					<fr:property name="key(delete)" value="label.delete" />
					<fr:property name="param(delete)" value="OID/cardGenerationEntryId" />
					<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
					<fr:property name="order(delete)" value="2" />
				</logic:present>
			</fr:layout>
		</fr:view>

	<logic:present name="cardGenerationEntry">
	
	<!-- 
		<bean:write name="cardGenerationEntry" property="line"/>
	 -->
	
		<table class="tstyle1 thlight tdcenter">
			<tr>
				<th>Nome</th>
				<th>Conteúdo</th>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.campusCode"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="campusCode"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.courseCode"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="courseCode"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.entityCode"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="entityCode"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.categoryCode"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="categoryCode"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.memberNumber"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="memberNumber"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.registerPurpose"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="registerPurpose"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.expirationDate"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="expirationDate"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.reservedField1"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="reservedField1"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.reservedField2"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="reservedField2"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.subClassCode"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="subClassCode"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.cardViaNumber"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="cardViaNumber"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.courseCode2"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="courseCode2"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.secondaryCategoryCode"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="secondaryCategoryCode"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.secondaryMemberNumber"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="secondaryMemberNumber"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.course"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="course"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.editedStudentNumber"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="editedStudentNumber"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.editedSecondaryMemberNumber"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="editedSecondaryMemberNumber"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.levelOfEducation"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="levelOfEducation"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.registrationYear"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="registrationYear"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.issueDate"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="issueDate"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.secondaryCategory"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="secondaryCategory"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.workPlace"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="workPlace"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.extraInformation"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="extraInformation"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.studentCompleteName"/></td>
				<td class="aleft"><bean:write name="cardGenerationEntry" property="studentCompleteName"/></td>
			</tr>
		</table>
	</logic:present>
	
	<h3><bean:message key="label.card.generation.emission"/>:</h3>
	<logic:empty name="person" property="cardGenerationRegister">
		<bean:message key="label.message.no.card.registers" bundle="CARD_GENERATION_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="person" property="cardGenerationRegister">
		<fr:view name="person" property="cardGenerationRegister" schema="card.generation.register.card.list">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thtop mtop05"/>
				<fr:property name="rowClasses" value=",bgcolorfafafa"/>
				<fr:property name="columnClasses" value="acenter,acenter,,acenter,acenter,acenter,acenter"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<br/>
	<br/>

	<%
		if (person.hasStudent()) {
		    final Student student = person.getStudent();
		    final Teacher teacher = person.getTeacher();
		    final Employee employee = person.getEmployee();

		    if ((teacher == null || !teacher.isActive()) && (employee == null || !employee.isActive())
			    && !student.getActiveRegistrations().isEmpty()) {
				for (final CardGenerationBatch cardGenerationBatch : CardGenerationBatch.getAvailableBatchesFor()) {
				    if (cardGenerationBatch.getExecutionYear().isCurrent() && cardGenerationBatch.getSent() == null) {
						%>
							<%= cardGenerationBatch.getDescription() %>
							<bean:define id="url" type="java.lang.String">/manageCardGeneration.do?method=createNewEntry&amp;cardGenerationBatchID=<%= cardGenerationBatch.getOID() %>&amp;studentID=<%= student.getOID() %></bean:define>
							<html:link page="<%= url %>">
								<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.create.new.card.generation.entry.for.student"/>
							</html:link>
							<br/>
						<%
				    }
				}
			}
		}
	%>

</logic:present>
