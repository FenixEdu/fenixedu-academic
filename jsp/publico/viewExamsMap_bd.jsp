<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<center>
<font color="red" size="3">Atenção aos comentários em baixo!</font>

<br />
<font color="red" size="3">Aviso:</font>
As horas apresentadas em cada exame referem-se ao periodo de reserva da sala e não à duração do exame.
<br/>
O início do exame coincide com o início da reserva.
</center>
<br/>
<br/>
<div>
	<app:generateNewExamsMap name="<%= SessionConstants.INFO_EXAMS_MAP %>" user="public" mapType=" "/>
</div>
</logic:present>