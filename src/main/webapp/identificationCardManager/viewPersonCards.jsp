<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>

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
			<fr:property name="param(view)" value="externalId/personId" />
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
				<fr:property name="param(view)" value="externalId/cardGenerationEntryId" />
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
				<th><bean:message key="cardGeneration.field"/></th>
				<th><bean:message key="cardGeneration.content"/></th>
				<th><bean:message key="cardGeneration.description"/></th>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.campusCode"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="campusCode"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.campusCode.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.courseCode"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="courseCode"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft">
					<bean:message key="cardGeneration.courseCode.description"/>
					<ul>
					<%
						for (final DegreeType degreeType : DegreeType.values()) {
						    if (degreeType == DegreeType.BOLONHA_DEGREE
							    	|| degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
						    	request.setAttribute("degreeType", degreeType);
					%>
								<li>
									<html:link page="/manageCardGeneration.do?method=showDegreeCodesAndLabels" paramId="degreeType" paramName="degreeType" paramProperty="name">
		   								<bean:message bundle="ENUMERATION_RESOURCES" name="degreeType" property="name"/>
									</html:link>
								</li>
					<%
						    }
						}
					%>
					</ul>
					<bean:message key="cardGeneration.courseCode.description.suffix"/>
				</td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.entityCode"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="entityCode"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.entityCode.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.categoryCode"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="categoryCode"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft">
					<bean:message key="cardGeneration.categoryCode.description"/>
					<ul>
						<li>
							<html:link page="/manageCardGeneration.do?method=showCategoryCodes">
								<bean:message key="link.manage.card.generation.consult.category.codes" />
							</html:link>
						</li>
					</ul>
				</td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.memberNumber"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="memberNumber"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.memberNumber.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.registerPurpose"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="registerPurpose"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.registerPurpose.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.expirationDate"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="expirationDate"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.expirationDate.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.reservedField1"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="reservedField1"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.reservedField1.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.reservedField2"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="reservedField2"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.reservedField2.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.subClassCode"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="subClassCode"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft">
					<bean:message key="cardGeneration.subClassCode.description"/>
					<ul>
						<li>
							<html:link page="/manageCardGeneration.do?method=showCategoryCodes">
								<bean:message key="link.manage.card.generation.consult.category.codes" />
							</html:link>
						</li>
					</ul>
				</td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.cardViaNumber"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="cardViaNumber"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.cardViaNumber.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.courseCode2"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="courseCode2"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.courseCode2.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.secondaryCategoryCode"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="secondaryCategoryCode"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft">
					<bean:message key="cardGeneration.secondaryCategoryCode.description"/>
					<ul>
						<li>
							<html:link page="/manageCardGeneration.do?method=showCategoryCodes">
								<bean:message key="link.manage.card.generation.consult.category.codes" />
							</html:link>
						</li>
					</ul>
					<bean:message key="cardGeneration.secondaryCategoryCode.description.suffix"/>
				</td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.secondaryMemberNumber"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="secondaryMemberNumber"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.secondaryMemberNumber.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.course"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="course"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.course.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.editedStudentNumber"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="editedStudentNumber"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.editedStudentNumber.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.editedSecondaryMemberNumber"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="editedSecondaryMemberNumber"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.editedSecondaryMemberNumber.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.levelOfEducation"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="levelOfEducation"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.levelOfEducation.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.registrationYear"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="registrationYear"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.registrationYear.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.issueDate"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="issueDate"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.issueDate.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.secondaryCategory"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="secondaryCategory"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.secondaryCategory.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.workPlace"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="workPlace"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.workPlace.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.extraInformation"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="extraInformation"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.extraInformation.description"/></td>
			</tr>
			<tr>
				<td class="aleft"><bean:message key="cardGeneration.studentCompleteName"/></td>
				<td class="aleft">
					<bean:define id="textToUnderline" type="java.lang.String" toScope="request"
							name="cardGenerationEntry" property="studentCompleteName"/>
					<jsp:include page="underline.jsp"/>
				</td>
				<td class="aleft"><bean:message key="cardGeneration.studentCompleteName.description"/></td>
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
