<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<center>
  <font size="+2"><bean:write name="Sitio" property="nome"/></font>
  <br/>
  <font size="+1">
    <bean:write name="Sitio" property="anoCurricular"/>º Ano
    <bean:write name="Sitio" property="semestre"/>º Semestre
    <bean:write name="Sitio" property="curso"/>
    <bean:write name="Sitio" property="departamento"/>
  </font>
  <br/>
</center>