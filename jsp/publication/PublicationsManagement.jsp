<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="title.publications.Management"/></h2>
<logic:present name="infoSitePublications"> 
		<logic:messagesPresent>
		<span class="error">
			<html:errors/>
		</span>
		</logic:messagesPresent>
		
		<table class="listClasses" width="100%">
			<tr>
				<td>
					<p align="left"><b><bean:message key="message.publications.warning" /></b></p>
				</td>
			</tr>
		</table>
		</br>
		<table class="infoselected" width="100%">
			<tr>
				<td width="70%"><b><bean:message key="message.teacherInformation.name" /></b>
					&nbsp;<bean:write name="infoSitePublications" property="infoTeacher.infoPerson.nome" /> </td> 
				<td width="30%"><b><bean:message key="message.teacherInformation.birthDate" /></b>
					&nbsp;<bean:write name="infoSitePublications" property="infoTeacher.infoPerson.nascimento" /> </td>	
			</tr>
			<tr>
				<td><b><bean:message key="message.teacherInformation.category" /></b>
					&nbsp;<bean:write name="infoSitePublications" property="infoTeacher.infoCategory.shortName" /></td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">1</span>
		<bean:message key="message.publications.authorPublications" />
		</p>
		<br />
		<bean:message key="message.publications.managementEditPublication"/>
		<bean:message key="message.publications.managementInsertPublication"/>
		<br/>
		<table style="text-align:left" width="100%">	
			<logic:iterate id="infoPublicationDidatic" name="infoSitePublications" property="infoDidaticPublications">
				<tr>
					<td class="listClasses" style="text-align:left">
						<bean:write name="infoPublicationDidatic" property="publicationString" />
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:define id="publicationTypeId" name="infoPublicationDidatic" property="keyPublicationType"/>
						<html:link page='<%= "/publicationDidatic.do?method=prepareEdit&amp;typePublication=Didatic&amp;page=0&amp;publicationTypeId=" + publicationTypeId %>'
							paramId="idInternal"
							paramName="infoPublicationDidatic" 
							paramProperty="idInternal">
							<bean:message key="label.edit" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<div class="gen-button">
				<html:link page="/readPublicationTypes.do?method=prepareEdit&amp;typePublication=Didatic&amp;page=0"
					paramId="infoTeacher#idInternal" 
					paramName="infoSitePublications" 
					paramProperty="infoTeacher.idInternal">
					<bean:message key="message.publications.insert" />
				</html:link>
			</div>
		<br />

		<p class="infoop"><span class="emphasis-box">2</span>
		<bean:message key="message.publications.cientificPublications" /></p>
		<br />
		<bean:message key="message.publications.managementEditPublication"/>
		<bean:message key="message.publications.managementInsertPublication"/>	
		<br/>
		<table style="text-align:left" width="100%">
			<logic:iterate id="infoPublicationCientific" name="infoSitePublications" property="infoCientificPublications">
				<tr>
					<td class="listClasses" style="text-align:left">
						<bean:write name="infoPublicationCientific" property="publicationString" />
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:define id="publicationTypeId" name="infoPublicationCientific" property="keyPublicationType"/>
						<html:link page='<%= "/publicationCientific.do?method=prepareEdit&amp;typePublication=Cientific&amp;page=0&amp;publicationTypeId=" + publicationTypeId %>'
							paramId="idInternal"
							 paramName="infoPublicationCientific" 
							paramProperty="idInternal">
							<bean:message key="label.edit" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<div class="gen-button">
			<html:link page="/readPublicationTypes.do?method=prepareEdit&amp;typePublication=Cientific&amp;page=0"
					paramId="infoTeacher#idInternal" 
					paramName="infoSitePublications" 
					paramProperty="infoTeacher.idInternal">
				<bean:message key="message.publications.insert" />
			</html:link>
		</div>	
</logic:present>