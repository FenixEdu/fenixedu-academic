package middleware.middlewareDomain;


public class MWAreasCientificasIleec
{
	private Integer idAreaCientifica;
	private String nome;

	public String toString()
	{
		return " [idAreaCientifica] " + idAreaCientifica + " [nome] " + nome;
	}

	public Integer getIdAreaCientifica()
	{
		return idAreaCientifica;
	}

	public void setIdAreaCientifica(Integer idAreaCientifica)
	{
		this.idAreaCientifica = idAreaCientifica;
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof MWAreasCientificasIleec) {
			MWAreasCientificasIleec mwAreasCientificasIleec = (MWAreasCientificasIleec) obj;
			result = this.getNome().equals(mwAreasCientificasIleec.getNome());
		}
		return result;
	}
}