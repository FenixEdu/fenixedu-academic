<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>


Gestão de qualifications... todo!!!!.. fazer a tabela igual á gestão de contractos

<%--
<html:form action="/editGrantContract">
    <html:hidden property="method" value="prepareEditGrantContractForm"/>
    <html:hidden property="idInternal" value='<%= request.getAttribute("idInternalPerson").toString() %>'/>       
    <html:submit styleClass="inputbutton">
        <bean:message key="button.createNewContract"/>
    </html:submit> 
</html:form>
--%>
<html:form action="/manageGrantOwner" style="display:inline">
	<html:hidden property="method" value="prepareManageGrantOwnerForm"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal" value='<%= request.getAttribute("idInternal").toString() %>'/>
	<html:submit styleClass="inputbutton" style="display:inline">
		<bean:message key="button.manageGrantOwner"/>
	</html:submit>
</html:form>		