package DataBeans.gesdis;

import java.util.Date;

/**
 * @author EP 15
 * @author jmota
 */

public class AnnouncementView {

	protected String title;

	protected Date date;

	protected Date lastModificationDate;

	protected String information;

	public AnnouncementView(String title, Date date, Date lastModifiedDate, String information) {

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

		if (obj != null && obj instanceof AnnouncementView) {

			resultado =
				getTitle().equals(((AnnouncementView) obj).getTitle())
					&& getDate().equals(((AnnouncementView) obj).getDate())
					&& getLastModificationDate().equals(
						((AnnouncementView) obj).getLastModificationDate())
					&& getInformation().equals(
						((AnnouncementView) obj).getInformation());

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
