<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h1 class="mbottom03 cnone"><fr:view name="researchUnit"
	property="nameWithAcronym" /></h1>


<p><bean:message key="link.Publications"
	bundle="RESEARCHER_RESOURCES" /> | <html:link
	page="<%="/researchSite/searchPublication.do?sectionID=" + request.getParameter("sectionID") + "&siteID=" + request.getParameter("siteID") + "&method=prepareSearchPublication" %>">
	<bean:message key="link.Search" bundle="RESEARCHER_RESOURCES" />
</html:link></p>


<h2 class="mtop15"><bean:message key="link.Publications"
	bundle="RESEARCHER_RESOURCES" /></h2>

		<fr:form action="<%="/researchSite/viewResearchUnitSite.do?sectionID=" + request.getParameter("sectionID") + "&siteID=" + request.getParameter("siteID") + "&method=showPublications" %>">
		<fr:edit id="executionYearIntervalBean" name="executionYearIntervalBean" visible="false"/>
		
		<p class="mbottom025"><bean:message key="label.choosen.interval" bundle="RESEARCHER_RESOURCES"/>:</p>
		
		<table class="tstyle5 mtop025">
		<tr>
			<td>
				<bean:message key="label.date.from" bundle="RESEARCHER_RESOURCES"/>:	  
				<fr:edit id="firstYear" name="executionYearIntervalBean" slot="firstExecutionYear">
					<fr:layout name="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
					<fr:property name="format" value="${year}"/>
					<fr:property name="defaultText" value="label.undefined"/>
					<fr:property name="key" value="true"/>
					<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
				</fr:layout>
				</fr:edit>
			</td>
			<td>
				<bean:message key="label.date.to" bundle="RESEARCHER_RESOURCES"/>:
				<fr:edit id="finalYear" name="executionYearIntervalBean" slot="finalExecutionYear">
					<fr:layout name="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
					<fr:property name="format" value="${year}"/>
					<fr:property name="defaultText" value="label.undefined"/>
					<fr:property name="key" value="true"/>
					<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
				</fr:layout>
				</fr:edit>
			</td>
			<td>
				<bean:message key="label.projectType" bundle="RESEARCHER_RESOURCES"/>:
				<fr:edit id="finalYear" name="executionYearIntervalBean" slot="publicationType">
					<fr:layout name="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ResultPublicationTypeProvider"/>
					<fr:property name="defaultText" value="label.all"/>
					<fr:property name="key" value="true"/>
					<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
					<fr:property name="sort" value="true"/>
				</fr:layout>
				</fr:edit>

				<html:submit><bean:message key="label.filter" bundle="RESEARCHER_RESOURCES"/></html:submit>
			</td>
		</tr>
		</table>
		</fr:form>


<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true"
		bundle="RESEARCHER_RESOURCES">
		<p><span class="error1"><!-- Error messages go here --><bean:write
			name="messages" /></span></p>
	</html:messages>
</logic:messagesPresent>

	<logic:notEmpty name="books">
	<p id='books' class="mtop2 mbottom0"><strong><span>
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Books" /></span></strong></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Book" toScope="request" />
		<bean:define id="results" name="books" toScope="request" />
		<jsp:include page="publicationsResume.jsp" />
	</logic:notEmpty>

	<logic:notEmpty name="inbooks">
	<p id='inbooks' class="mtop2 mbottom0"><strong><span>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.BookParts" /></span></strong></p>
		<bean:define id="currentSchema"
			value="result.publication.presentation.BookPart" toScope="request" />
		<bean:define id="results" name="inbooks" toScope="request" />
		<jsp:include page="publicationsResume.jsp" />
	</logic:notEmpty>

<logic:notEmpty name="articles">
	<p id='books' class="mtop2 mbottom0"><strong><span>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Articles" /></span></strong></p>
		<bean:define id="currentSchema"
			value="result.publication.presentation.Article" toScope="request" />
		<bean:define id="results" name="articles" toScope="request" />
		<jsp:include page="publicationsResume.jsp" />
	</logic:notEmpty>

	<logic:notEmpty name="inproceedings">
		<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="research.ResultPublication.Inproceedings"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="proceedings">
		<p id='proceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Proceedings"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Proceedings" toScope="request"/>
		<bean:define id="results" name="proceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="theses">
	<p id='books' class="mtop2 mbottom0"><strong><span>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Theses" />
	</span></strong></p>
		<bean:define id="currentSchema"
			value="result.publication.presentation.Thesis" toScope="request" />
		<bean:define id="results" name="theses" toScope="request" />
		<jsp:include page="publicationsResume.jsp" />
	</logic:notEmpty>

	<logic:notEmpty name="manuals">
	<p id='books' class="mtop2 mbottom0"><strong><span>
		<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.ResultPublication.Manuals" /></span></strong></p>
		<bean:define id="currentSchema"
			value="result.publication.presentation.Manual" toScope="request" />
		<bean:define id="results" name="manuals" toScope="request" />
		<jsp:include page="publicationsResume.jsp" />
	</logic:notEmpty>

	<logic:notEmpty name="technicalReports">
	<p id='books' class="mtop2 mbottom0"><strong><span>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.TechnicalReports" />
	</span></strong></p>
		<bean:define id="currentSchema"
			value="result.publication.presentation.TechnicalReport"
			toScope="request" />
		<bean:define id="results" name="technical-reports" toScope="request" />
		<jsp:include page="publicationsResume.jsp" />
	</logic:notEmpty>

	<logic:notEmpty name="otherPublications">
	<p id='books' class="mtop2 mbottom0"><strong><span>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.OtherPublications" />
	</span></strong></p>
		<bean:define id="currentSchema"
			value="result.publication.presentation.OtherPublication"
			toScope="request" />
		<bean:define id="results" name="otherPublications" toScope="request" />
		<jsp:include page="publicationsResume.jsp" />
	</logic:notEmpty>

	<logic:notEmpty name="unstructureds">
		<logic:empty name="otherPublications">
			<p id='unstructureds' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Unstructureds"/></strong></span></p>		
		</logic:empty>
		<bean:define id="results" name="unstructureds" toScope="request"/>
		<ul>
			<logic:iterate id="result" name="results" scope="request">
	 			<bean:define id="resultId" name="result" property="idInternal"/>
				<li>
		 			<fr:view name="result" layout="values" schema="result.publication.presentation.Unstructured">
		 				<fr:layout>
		 					<fr:property name="htmlSeparator" value=", "/>
		 					<fr:property name="indentation" value="false"/>
		 				</fr:layout>
	 				</fr:view>
	 			</li>
			</logic:iterate>
		</ul>
	</logic:notEmpty>

<logic:empty name="books">
	<logic:empty name="inbooks">
		<logic:empty name="articles">
			<logic:empty name="inproceedings">
				<logic:empty name="proceedings">
					<logic:empty name="theses">
						<logic:empty name="manuals">
							<logic:empty name="technicalReports">
								<logic:empty name="otherPublications">
									<logic:empty name="unstructureds">
										<bean:message key="label.search.noResultsFound" />
									</logic:empty>
								</logic:empty>
							</logic:empty>
						</logic:empty>
					</logic:empty>
				</logic:empty>
			</logic:empty>
		</logic:empty>
	</logic:empty>
</logic:empty>
