/*
 * DiaSemana.java
 *
 * Created on 11 de Outubro de 2002, 17:10
 */

package Util;


/**
 *
 * @author  tfc130
 */
public class TipoAula extends FenixUtil implements Comparable {
	public static final int TEORICA = 1;
	public static final int PRATICA = 2;
	public static final int TEORICO_PRATICA = 3;
	public static final int LABORATORIAL = 4;
	public static final int DUVIDAS = 5;
	public static final int RESERVA = 6;

	private Integer tipo;

	public TipoAula() {
	}

	public TipoAula(int tipo_aula) {
		this.tipo = new Integer(tipo_aula);
	}

	public TipoAula(Integer tipo_aula) {
		this.tipo = tipo_aula;
	}

	public Integer getTipo() {
		return this.tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = new Integer(tipo);
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof TipoAula) {
			TipoAula tipo = (TipoAula) obj;
			resultado =
				(this.getTipo().intValue() == tipo.getTipo().intValue());
		}
		return resultado;
	}

	public String toString() {
		int value = this.tipo.intValue();
		switch (value) {
			case TEORICA :
				return "T";
			case PRATICA :
				return "P";
			case TEORICO_PRATICA :
				return "TP";
			case LABORATORIAL :
				return "L";
			case DUVIDAS :
				return "D";
			case RESERVA :
				return "R";
		}
		return "Error: Invalid lesson type";
	}

	public String getSiglaTipoAula() {
		int value = this.tipo.intValue();
		switch (value) {
			case TEORICA :
				return "T";
			case PRATICA :
				return "P";
			case TEORICO_PRATICA :
				return "TP";
			case LABORATORIAL :
				return "L";
			case DUVIDAS :
				return "D";
			case RESERVA :
				return "R";
		}
		return "Error: Invalid lesson type";
	}

	public String getFullNameTipoAula() {
			int value = this.tipo.intValue();
			switch (value) {
				case TEORICA :
					return "Teórica";
				case PRATICA :
					return "Prática";
				case TEORICO_PRATICA :
					return "TeoricoPrática";
				case LABORATORIAL :
					return "Laboratorial";
				case DUVIDAS :
					return "Dúvidas";
				case RESERVA :
					return "Reserva";
			}
			return "Error: Invalid lesson type";
		}


	public int compareTo(Object arg0) {
		// T(1) < TP(3) < P(2) < L(4) < D(5) < R(6)
		TipoAula tipoAula = (TipoAula) arg0;

		if (getTipo().intValue() == 3 && tipoAula.getTipo().intValue() == 2) {
			// 3 < 2
			return -1;
		}
		if (getTipo().intValue() == 2 && tipoAula.getTipo().intValue() == 3) {
			// 2 > 3
			return 1;
		}
		return	getTipo().compareTo(tipoAula.getTipo());
	}

}