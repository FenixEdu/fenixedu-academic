<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p><span class="error1"><!-- Error messages go here --><bean:write name="messages" /></span></p>
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

	
	<bean:define id="hasArticles" value="false" toScope="request"/>
	<logic:notEmpty name="national-articles"> 
		<bean:define id="hasArticles" value="true" toScope="request"/>
	</logic:notEmpty>
	<logic:notEmpty name="international-articles"> 
		<bean:define id="hasArticles" value="true" toScope="request"/>
	</logic:notEmpty>	
	
	<logic:equal name="hasArticles" value="true">
		<p id='books' class="mtop2 mbottom0"/>
		<bean:define id="currentSchema" value="result.publication.presentation.Article" toScope="request"/>

		<logic:notEmpty name="international-articles">
		<p class="mtop2 mbottom0"><strong><bean:message key="label.internationalArticles" bundle="RESEARCHER_RESOURCES"/></strong></p>
		<bean:define id="results" name="international-articles" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
		</logic:notEmpty>
		
		<logic:notEmpty name="national-articles">
		<p class="mtop2 mbottom0"><strong><bean:message key="label.nationalArticles" bundle="RESEARCHER_RESOURCES"/></strong></p>
		<bean:define id="results" name="national-articles" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
		</logic:notEmpty>
	</logic:equal>
	
	<logic:notEmpty name="international-inproceedings">
		<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.InternationalInproceedings"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="international-inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="national-inproceedings">
		<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.NationalInproceedings"/></strong></span></p>
		<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
		<bean:define id="results" name="national-inproceedings" toScope="request"/>
		<jsp:include page="publicationsResume.jsp"/>
	</logic:notEmpty>
	

	<logic:notEqual name="hasArticles" value="true">
	<logic:notEmpty name="articles">
	<p id='books' class="mtop2 mbottom0"><strong><span>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.Articles" /></span></strong></p>
		<bean:define id="currentSchema"
			value="result.publication.presentation.Article" toScope="request" />
		<bean:define id="results" name="articles" toScope="request" />
		<jsp:include page="publicationsResume.jsp" />
	</logic:notEmpty>
	</logic:notEqual>
	
	<logic:empty name="international-inproceedings">
		<logic:empty name="national-inproceedings">
			<logic:notEmpty name="inproceedings">
				<p id='inproceedings' class="mtop2 mbottom0"/><span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="research.ResultPublication.Inproceedings"/></strong></span></p>
				<bean:define id="currentSchema" value="result.publication.presentation.Inproceedings" toScope="request"/>
				<bean:define id="results" name="inproceedings" toScope="request"/>
				<jsp:include page="publicationsResume.jsp"/>
			</logic:notEmpty>
		</logic:empty>
	</logic:empty>
	
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
		<bean:define id="results" name="technicalReports" toScope="request" />
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
	 			<bean:define id="resultId" name="result" property="externalId"/>
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
			<logic:empty name="international-articles">
				<logic:empty name="national-articles">
					<logic:empty name="inproceedings">
						<logic:empty name="international-inproceedings">
							<logic:empty name="national-inproceedings">
								<logic:empty name="proceedings">
									<logic:empty name="theses">
										<logic:empty name="manuals">
											<logic:empty name="technical-reports">
												<logic:empty name="other-publications">
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
			</logic:empty>
		</logic:empty>
	</logic:empty>
</logic:empty>