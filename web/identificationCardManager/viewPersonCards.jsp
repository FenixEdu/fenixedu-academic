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

<h2>
	<bean:message key="link.card.generation.search.people" />
</h2>

<br/>

<logic:present name="person">
	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
	<fr:view name="person" schema="card.generation.search.person.list">
		<fr:layout name="tabular">
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

			<fr:property name="link(delete)" value="/manageCardGeneration.do?method=deletePersonCard"/>
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="param(delete)" value="OID/cardGenerationEntryId" />
			<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
			<fr:property name="order(delete)" value="2" />
		</fr:layout>
	</fr:view>

	<br/>
	<br/>

	<logic:present name="cardGenerationEntry">
		<bean:write name="cardGenerationEntry" property="line"/>
	</logic:present>

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
