<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.publications.Management"/></h2>
<logic:messagesPresent>
		<span class="error">
			<html:errors property="nonValidating"/>
			<html:errors property="nonValidatingName"/>
			<html:errors property="nonValidatingOrganization"/>
		</span>
</logic:messagesPresent>
<html:form action="/searchAuthor">
<br/>
<h3>
		<bean:message key="message.publications.insertPublication" />/
		<bean:message key="message.publications.insertAuthors" />
</h3>

	<bean:message key="message.publication.InsertSearchAuthors" /><br/>
	<bean:message key="message.publication.manageSearchAuthors" /><br/>
	<bean:message key="message.publication.manageSearchAuthors4" /><br/>
	<bean:message key="message.publication.manageSearchAuthors5" /><br/>
	<bean:message key="message.publication.manageSearchAuthors6" /><br/>
	<bean:message key="message.publication.manageSearchAuthors7" /><br/>

</P>
	<br />
	
	<html:hidden property="authorsId"/>
	<html:hidden property="authorsName"/>
	<html:hidden property="index"/>

	<html:hidden property="title" />
	<html:hidden property="description" />
	<html:hidden property="infoPublicationTypeId" />
	<html:hidden property="subtype" />
	<html:hidden property="journalName" />
	<html:hidden property="volume" />
	<html:hidden property="firstPage" />
	<html:hidden property="lastPage" />
	<html:hidden property="language" />
	<html:hidden property="format" />
	<html:hidden property="observation" />
	<html:hidden property="number" />
	<html:hidden property="month" />
	<html:hidden property="year" />
	<html:hidden property="month_end" />
	<html:hidden property="year_end" />
	<html:hidden property="editor" />
	<html:hidden property="country" />
	<html:hidden property="issn" />
	<html:hidden property="scope" />
	<html:hidden property="url" />
	<html:hidden property="editorCity" />
	<html:hidden property="numberPages" />
	<html:hidden property="edition" />
	<html:hidden property="fascicle" />
	<html:hidden property="serie" />
	<html:hidden property="isbn" />
	<html:hidden property="local" />
	<html:hidden property="conference" />
	<html:hidden property="instituition" />
	<html:hidden property="originalLanguage" />
	<html:hidden property="translatedAuthor" />
	<html:hidden property="criticizedAuthor" />
	<html:hidden property="publicationType" />
	<html:hidden property="university" />
	
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="infoPublicationTypeId"/>
	<html:hidden property="typePublication"/>
	<html:hidden property="method" value="searchAuthor"/>
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="DataBeans.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
	</logic:present>
		<table>
			<tr>
				<td>
					<bean:message key="message.publication.stringNomeSearch" />
				</td>
				<td>
					<html:text size="20"  property="searchAuthorString"/>
				</td>
				<td>
					<html:submit styleClass="inputbutton" property="searchAuthorString">
						<bean:message key="button.publication.search"/>
					</html:submit> 
				</td>
				<td>
					<html:reset styleClass="inputbutton"><bean:message key="button.publication.clear"/>
					</html:reset>
				</td>
			</tr>
		</table>
		<br/>
</html:form>	

<html:form action="/insertAuthorsInPublication">
	
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="infoPublicationTypeId"/>
	<html:hidden property="typePublication"/>
	<html:hidden property="method" value="insertAuthorsInPublication"/>	
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="DataBeans.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
	</logic:present>
	
	<logic:present name="infoAuthorsPersons">
		<logic:notEmpty name="infoAuthorsPersons">
			<bean:message key="message.publication.insertAuthorExplanation"	/>
			<table>
				<br/>
				<tr>
					<bean:message key="message.publications.table.result" />
					<br/>
				</tr>
				<tr>
					<td class="listClasses-header">&nbsp;
					</td>
					<td class="listClasses-header"><bean:message key="message.publications.table.name" />
					</td>
					<td class="listClasses-header"><bean:message key="message.publications.table.organization" />
					</td>
				</tr>
				<logic:iterate id="authorPersons" name="infoAuthorsPersons" type="DataBeans.publication.InfoAuthorPerson">
					<tr>	 			
						<td class="listClasses">
							<html:multibox property="authorsPersonsCodes">
								<bean:write name="authorPersons" property="keyFinal"/>
							</html:multibox>
						</td>
						<td class="listClasses" style="text-align:left"><bean:write name="authorPersons" property="name"/>
						</td>
						<td class="listClasses" style="text-align:left"><bean:write name="authorPersons" property="organization"/>
						</td>
					</tr>
				</logic:iterate>
				<br/>
				<tr>
					<td>
						<html:submit styleClass="inputbutton" property="confirm">
							<bean:message key="button.publication.InsertAuthors"/>
						</html:submit> 
					</td>
				</tr>						
			</table>
		</logic:notEmpty>
		<br/>
	</logic:present>			
</html:form>

<html:form action="/insertAuthorInPublication">
	
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="infoPublicationTypeId"/>
	<html:hidden property="typePublication"/>
	<html:hidden property="method" value="insertAuthorInPublication"/>	
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="DataBeans.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
	</logic:present>				
	<logic:present name="infoAuthorsPersons">	
		<logic:empty name="infoAuthorsPersons">
			<bean:message key="message.publications.notFound" />
			<br/>
			<table>
				<tr>
					<td>
						<bean:message key="message.publications.name" />
					</td>
					<td>
						<html:text size="50"  property="authorName"/>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="message.publications.organization" />
					</td>
					<td>
						<html:text size="50"  property="authorOrganization"/>
					</td>
				</tr>
			</table>
			<html:submit styleClass="inputbutton" property="confirm">
				<bean:message key="button.publication.InsertAuthor"/>
			</html:submit> 
		</logic:empty>
	</logic:present>
</html:form>

<html:form action="/deleteAuthorInPublication">
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="infoPublicationTypeId"/>
	<html:hidden property="typePublication"/>
	<html:hidden property="method" value="deleteAuthorInPublication"/>
<h4><bean:message key="message.publications.authorsInserteds" />
</h4>
	<logic:present name="infoAuthorsList">
		<logic:notEmpty name="infoAuthorsList">
			<bean:message key="message.publication.deleteAuthorExplanation" />
			<br/>			
			<bean:message key="message.publications.teacherPresentAndListPresent" />
			<br/>
			<table>
				<tr>
					<td class="listClasses-header"><bean:message key="message.publications.table.name" />
					</td>
					<td class="listClasses-header"><bean:message key="message.publications.table.organization" />
					</td>
					<td class="listClasses-header"><bean:message key="message.publications.table.select" />
					</td>
				</tr>
				<logic:iterate id="infoAuthor" name="infoAuthorsList" type="DataBeans.publication.InfoAuthor">
					<bean:define id="authorIdDeleted" name="infoAuthor" property="idInternal"/>
					<html:hidden property="authorsIds" value="<%= pageContext.findAttribute("authorIdDeleted").toString() %>"/>
					<tr>
						<td class="listClasses" style="text-align:center">
							<logic:notEmpty name="infoAuthor" property="infoPessoa">
								<bean:define id="infoPessoa" name="infoAuthor" property="infoPessoa" type="DataBeans.InfoPerson"/>
								<bean:write name="infoPessoa" property="nome"/>
							</logic:notEmpty>
							<logic:empty name="infoAuthor" property="infoPessoa">
								<bean:write name="infoAuthor" property="author"/>
							</logic:empty>
						</td>
						<td class="listClasses" style="text-align:center">
							<bean:write name="infoAuthor" property="organization"/>
						</td>
						<td class="listClasses" style="text-align:center">
							<html:multibox property="authorsIdstoDelete">
								<bean:write name="infoAuthor" property="idInternal"/>
							</html:multibox>
						</td>
					</tr>
				</logic:iterate>
			</table>
			<html:submit styleClass="inputbutton" property="confirm">
				<bean:message key="button.publication.DeleteAuthors"/>
			</html:submit> 
		</logic:notEmpty>
		<logic:empty name="infoAuthorsList">
			<bean:message key="message.publications.teacherPresent" />
		</logic:empty>
	</logic:present>
</html:form>	


<html:form action="/readPublicationAttributes">
	
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="infoPublicationTypeId"/>
	<html:hidden property="typePublication"/>
	<html:hidden property="method" value="edit"/>
	
	<logic:present name="infoAuthorsList">
		<logic:iterate id="infoAuthor" name="infoAuthorsList" type="DataBeans.publication.InfoAuthor">
			<bean:define id="authorIdInserted" name="infoAuthor" property="idInternal"/>
			<html:hidden property="authorsIds" value="<%= pageContext.findAttribute("authorIdInserted").toString() %>"/>
		</logic:iterate>
	</logic:present>
<html:submit styleClass="inputbutton" property="confirm"><bean:message key="button.publication.continue"/>
</html:submit> 
<br/> 
</html:form>
