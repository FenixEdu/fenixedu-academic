<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<center>	
	<h2>Exemplo de Perguntas</h2>
	<b>Esta página apresenta exemplos de perguntas geradas segundo a especificação IMS. O sistema de respostas é meramente indicativo.</b>
</center>
<span class="error"></span>
<br>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="10">
	<tbody><tr><td><hr></td></tr>
	<tr><td><b>Pergunta:</b>&nbsp;1</td></tr>
	<tr><td><b>Cotação:</b>&nbsp;3</td></tr>
	<tr><td>
	</td></tr><tr><td>
	</td></tr><tr><td>
		O pêndulo de um relógio move-se descrevendo o movimento
	</td></tr><tr><td>
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_013.gif" align="middle">
	</td></tr><tr><td>
		No instante t (em segundos), a distância ao centro (em cm) é  dada por
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_011.gif" align="middle">
		Qual a maior distância a que o pêndulo se encontra do centro?
	<table><tbody><tr><td>
	</td></tr><tr><td>
		<input type="radio" name="question[0].response" value="1">
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_012.gif" align="middle">
	</td></tr><tr><td>
		<input type="radio" name="question[0].response" value="2">
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_015.gif" align="middle">
	</td></tr><tr><td>
		<input type="radio" name="question[0].response" value="3">
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_005.gif" align="middle">
	</td></tr><tr><td>
		<input type="radio" name="question[0].response" value="4">
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_007.gif" align="middle">
	</td></tr></tbody></table>
	</td></tr><tr><td>
	</td></tr><tr><td><hr></td></tr>
	<tr><td><b>Pergunta:</b>&nbsp;2</td></tr>
	<tr><td><b>Cotação:</b>&nbsp;4</td></tr>
	<tr><td></td></tr><tr><td>
	</td></tr><tr><td>
		Seja
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_008.gif" align="middle">
		uma função tal que:
	</td></tr><tr><td>
		i)  as rectas que passam pelo ponto (1,1), excluindo esse ponto, são as curvas de nível da função
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_014.gif" align="middle">
		;
	</td></tr><tr><td>
		ii)   a rectas diferentes correspondem valores diferentes da função
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_002.gif" align="middle">
		.
	</td></tr><tr><td>
		Considere as seguintes afirmações:
	</td></tr><tr><td>
		Diga qual a lista completa de afirmações correctas que podem ser deduzidas do enunciado.
	<table><tbody><tr><td>
	</td></tr><tr><td>
		<input type="checkbox" name="question[1].response" value="1">
		não existe o limite segundo a recta
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_006.gif" align="middle">
		da função
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_003.gif" align="middle">
		no ponto (1,1)
	</td></tr><tr><td>
		<input type="checkbox" name="question[1].response" value="2">
		a derivada parcial de
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_010.gif" align="middle">
		em ordem a
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_009.gif" align="middle">
		, no ponto (0,1), é igual a zero
	</td></tr><tr><td>
		<input type="checkbox" name="question[1].response" value="3">
		todos os limites direccionais de
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests.gif" align="middle">
		em (1,1) são diferentes
	</td></tr><tr><td>
		<input type="checkbox" name="question[1].response" value="4">
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_004.gif" align="middle">
		tem as mesmas linhas de nível que a função
		<img src="<%= request.getContextPath()%>/images/imsExample/studentTests_016.gif" align="middle">
	</td></tr><tr><td>
		<input type="checkbox" name="question[1].response" value="5">
		Nenhuma
	</td></tr></tbody></table>
	</td></tr><tr><td>
	</td></tr><tr><td><hr></td></tr>
	<tr><td><b>Pergunta:</b>&nbsp;3</td></tr>
	<tr><td><b>Cotação:</b>&nbsp;4</td></tr>
	<tr><td>
	</td></tr><tr><td>
		Qual o valor de PI com três casas decimais?
		<input type="text" name="question[2].response" maxlength="6" size="6" value="">
	</td></tr><tr><td>
	</td></tr><tr><td>
	</td></tr><tr><td><hr></td></tr>
	<tr><td><b>Pergunta:</b>&nbsp;4</td></tr>
	<tr><td><b>Cotação:</b>&nbsp;1</td></tr>
	<tr><td>
	</td></tr><tr><td>
		Complete a sequência:
		</td></tr><tr><td>
		Inverno, Primavera, Verão,
		<input type="text" name="question[3].response" maxlength="6" size="6" value="">
		.
	</td></tr><tr><td>
	</td></tr><tr><td>
	</td></tr><tr><td><hr></td></tr>
	<tr><td><b>Pergunta:</b>&nbsp;5</td></tr>
	<tr><td><b>Cotação:</b>&nbsp;0</td></tr>
	<tr><td>
	</td></tr><tr><td>
		Utilize o espaço seguinte para fazer qualquer comentário.
	</td></tr><tr><td>
		<textarea name="question[4].response" cols="80" rows="20"></textarea>
	</td></tr><tr><td>
	</td></tr><tr><td>
	</td></tr><tr><td><hr></td></tr>
	</tbody>
</table>
