<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />


<em><bean:message key="label.person.main.title" /></em>
 <h2>Validação de Contactos</h2>
<p><html:link page="/student.do?method=prepareEditPersonalData" paramId="studentID" paramName="student" paramProperty="externalId"><bean:message bundle="APPLICATION_RESOURCES" key="label.return"/></html:link></p>

O utilizador <bean:write name="student" property="person.presentationName"/> terá que validar o contacto para este ser considerado válido.