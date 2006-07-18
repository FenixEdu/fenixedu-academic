<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2>Prova de Aferiï¿½ï¿½o de Fï¿½sica - 2003/2004</h2>
<table width="90%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
<bean:define id="testslink" type="java.lang.String"><bean:message key="link.gauging.physics.test" bundle="GLOBAL_RESOURCES"/></bean:define>
Os enunciados e as respectivas chaves de correcï¿½ï¿½o estï¿½o disponï¿½veis em <html:link href="<%= testslink %>" target="about_blank"><%= testslink %></html:link>
</td></tr>
</table>
<br/>
<logic:notPresent name="gaugingTestResult">
<span class="error">Não fez a prova de aferiï¿½ï¿½o</span> 
</logic:notPresent>
<logic:present name="gaugingTestResult">
<table width="90%">
<tr>
<th class="listClasses-header">Enunciado</th>
<th class="listClasses-header">Nï¿½mero de Respostas em branco</th>
<th class="listClasses-header">Nï¿½mero de Respostas correctas</th>
<th class="listClasses-header">Nï¿½mero de Respostas erradas</th>
<th class="listClasses-header">Classificaï¿½ï¿½o Final</th>
</tr>
<tr>
<td class="listClasses"><bean:write name="gaugingTestResult" property="test"/></td>
<td class="listClasses"><bean:write name="gaugingTestResult" property="unanswered"/></td>
<td class="listClasses"><bean:write name="gaugingTestResult" property="correct"/></td>
<td class="listClasses"><bean:write name="gaugingTestResult" property="wrong"/></td>
<td class="listClasses"><bean:write name="gaugingTestResult" property="cf"/></td>
</tr>

</table>
<br/>
<br/>
<br/>


<table width="90%">
<tr>
<th class="listClasses-header">Pergunta</th><th class="listClasses-header">Resultado</th>
</tr><tr>
<td class="listClasses">P1</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p1"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P2</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p2"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P3</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p3"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P4</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p4"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P5</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p5"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P6</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p6"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P7</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p7"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P8</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p8"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P9</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p9"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P10</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p10"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P11</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p11"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P12</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p12"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P13</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p13"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P14</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p14"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P15</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p15"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P16</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p16"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P17</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p17"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P18</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p18"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P19</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p19"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P20</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p20"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P21</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p21"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P22</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p22"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P23</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p23"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P24</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p24"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P25</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p25"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P26</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p26"/>&nbsp;</td>
</tr><tr>
<td class="listClasses">P27</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p27"/>&nbsp;</td>
</tr>
<logic:present name="gaugingTestResult" property="p28">
<tr>
<td class="listClasses">P28</td><td class="listClasses"><bean:write name="gaugingTestResult" property="p28"/>&nbsp;</td>
</tr>
</logic:present>
</table>	



</logic:present>
