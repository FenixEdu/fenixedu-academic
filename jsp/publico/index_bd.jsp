<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="/chooseContextDA.do?method=prepare&amp;nextPage=classSearch&amp;inputPage=chooseContext" ><strong><bean:message key="link.classes.consult"/></strong></html:link>
    		</td>
  		</tr>
	</table>
<br />
<p>
Nesta área poderá efectuar uma pesquisa por curso (actualmente só encontrará licenciaturas) e ano lectivo.
O semestre é o actual.
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="/chooseContextDA.do?method=prepare&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext"><strong><bean:message key="link.executionCourse.consult"/></strong></html:link></td>
  		</tr>
	</table>
<br />
<p>
Nesta área encontrará a informação relativamente à disciplina que pretende. Informação essa que é: 
turnos, horário e carga curricular
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="/prepareConsultRooms.do"><strong><bean:message key="link.rooms.consult"/></strong></html:link></td>
  		</tr>
	</table>
<br />
<p>
Nesta área encontrará a informação relativamente a uma sala, onde poderá consultar o horário lectivo dessa mesma sala.
</p>  