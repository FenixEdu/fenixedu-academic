package ServidorApresentacao.formbeans.assiduousness;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

public final class MostrarListaForm extends ActionForm {

	private ArrayList headers = null;
	private ArrayList body = null;

	private ArrayList headers2 = null;
	private ArrayList body2 = null;

	private ArrayList headers3 = null;
	private ArrayList body3 = null;

	public ArrayList getHeaders() {
		return headers;
	}
	public ArrayList getBody() {
		return body;
	}

	public void setHeaders(ArrayList headers) {
		this.headers = headers;
	}
	public void setBody(ArrayList body) {
		this.body = body;
	}

	public ArrayList getHeaders2() {
		return headers2;
	}
	public ArrayList getBody2() {
		return body2;
	}

	public void setHeaders2(ArrayList headers2) {
		this.headers2 = headers2;
	}
	public void setBody2(ArrayList body2) {
		this.body2 = body2;
	}

	public ArrayList getHeaders3() {
		return headers3;
	}
	public ArrayList getBody3() {
		return body3;
	}

	public void setHeaders3(ArrayList headers3) {
		this.headers3 = headers3;
	}
	public void setBody3(ArrayList body3) {
		this.body3 = body3;
	}
}