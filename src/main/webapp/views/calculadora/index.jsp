<%@page import="com.ipartek.formacion.nidea.controller.CalculadoraController"%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.ipartek.formacion.nidea.pojo.Material"%>
<%@page import="com.ipartek.formacion.nidea.pojo.Mesa"%>


<form action="calculadora" method="post">

	<div class="form-group">
		<label for="num1">1� Numero</label> 
		<input type="text" class="form-control" name="num1">		
	</div>
	
	<div class="form-group">
		<label for="num2">2� Numero</label> 
		<input type="text" class="form-control" name="num2">		
	</div>

	<select name="operacion">
		<option value="<%=CalculadoraController.OP_SUMAR%>">Sumar</option>
		<option value="<%=CalculadoraController.OP_RESTAR%>">Restar</option>
		<option value="<%=CalculadoraController.OP_MULTIPLICAR%>">Multiplicar</option>
		<option value="<%=CalculadoraController.OP_DIVIDIR%>">Dividir</option>
	</select>


	<input type="submit" class="btn btn-block btn-outline-primary" value="Calcular">
</form>



<jsp:include page="/templates/footer.jsp"></jsp:include>