package DataBeans.gesdis;

import java.util.Date;

/**
 * @author EP 15
 * @author jmota
 */

public class InfoAnnouncement {

	protected String title;

	protected Date date;

	protected Date lastModificationDate;

	protected String information;

	public InfoAnnouncement(String title, Date date, Date lastModifiedDate, String information) {

		this.title = title;

		this.date = date;

		this.lastModificationDate = lastModifiedDate;

		this.information = information;

	}

	public String getTitle() {

		return title;

	}

	public Date getDate() {

		return date;

	}

	public Date getLastModificationDate() {

		return lastModificationDate;

	}

	public String getInformation() {

		return information;

	}

	public boolean equals(Object obj) {

		boolean resultado = false;

		if (obj != null && obj instanceof InfoAnnouncement) {

			resultado =
				getTitle().equals(((InfoAnnouncement) obj).getTitle())
					&& getDate().equals(((InfoAnnouncement) obj).getDate())
					&& getLastModificationDate().equals(
						((InfoAnnouncement) obj).getLastModificationDate())
					&& getInformation().equals(
						((InfoAnnouncement) obj).getInformation());

		}

		return resultado;

	}

	/*
	 public String toString() {
	    String result = "[ANUNCIOVIEW";
	    result += ", titulo=" + _titulo;
	    result += ", informacao=" + _informacao;
		result += ", data=" + _data.toString();
		result += ", data_utlima_act=" + _dataUltimaAlteracao.toString();
	    result += "]";
	    return result;
	}
	 */

}
