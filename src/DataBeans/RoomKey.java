/*
 * KeysSala.java
 *
 * Created on 31 de Outubro de 2002, 12:27
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
public class RoomKey extends InfoObject {
	protected String _nomeSala;

	public RoomKey() {
	}

	public RoomKey(String nomeSala) {
		setNomeSala(nomeSala);
	}

	public String getNomeSala() {
		return _nomeSala;
	}

	public void setNomeSala(String nomeSala) {
		_nomeSala = nomeSala;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof RoomKey) {
			RoomKey keySala = (RoomKey) obj;

			resultado = (getNomeSala().equals(keySala.getNomeSala()));
		}

		return resultado;
	}

	public String toString() {
		String result = "[KEYSALA";
		result += ", sala=" + _nomeSala;
		result += "]";
		return result;
	}

}
