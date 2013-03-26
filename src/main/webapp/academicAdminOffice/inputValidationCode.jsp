<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />


<em><bean:message key="label.person.main.title" /></em>
 <h2>Validação de Contactos</h2>
<p><html:link page="/student.do?method=prepareEditPersonalData" paramId="studentID" paramName="student" paramProperty="idInternal"><bean:message bundle="APPLICATION_RESOURCES" key="label.return"/></html:link></p>

O utilizador <bean:write name="student" property="person.presentationName"/> terá que validar o contacto para este ser considerado válido.