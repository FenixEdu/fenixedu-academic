
/**
 *
 * Autor: Ricardo Nortadas 
 *
 */

package Util;

import java.io.Serializable;

public class Mes implements Serializable {

	public static final int JANEIRO = 1;
	public static final int FEVEREIRO = 2;
	public static final int MARCO = 3;
	public static final int ABRIL = 4;
	public static final int MAIO = 5;
	public static final int JUNHO = 6;
	public static final int JULHO = 7;
	public static final int AGOSTO = 8;
	public static final int SETEMBRO = 9;
	public static final int OUTUBRO = 10;
	public static final int NOVEMBRO = 11;
	public static final int DEZEMBRO = 12;

	private Integer _Mes;

		public Mes() { }

		public Mes(int Mes) {
			this._Mes = new Integer(Mes);
		}

		public Mes(Integer Mes) {
			this._Mes = Mes;
		}

		public Integer getMes() {
			return this._Mes;
		}

		public void setMes(int Mes) {
			this._Mes = new Integer(Mes);
		}

		public void setMes(Integer Mes) {
			this._Mes = Mes;
		}

		public boolean equals(Object obj) {
			boolean resultado = false;
			if (obj instanceof Mes) {
				Mes Mes = (Mes)obj;
				resultado = (this.getMes().intValue() == Mes.getMes().intValue() );
			}
			return resultado;
		}


	public String toString() {
			int mes = this._Mes.intValue();
			switch (mes) {
				case JANEIRO : return "Janeiro";
				case FEVEREIRO : return "Fevereiro";
				case MARCO : return "Marco";
				case ABRIL : return "Abril";
				case MAIO : return "Maio";
				case JUNHO : return "Junho";
				case JULHO : return "Julho";
				case AGOSTO : return "Agosto";
				case SETEMBRO : return "Setembro";
				case OUTUBRO : return "Outubro";
				case NOVEMBRO : return "Novembro";
				case DEZEMBRO : return "Dezembro";
			}
			return "Erro: Invalid month";
		}
}
