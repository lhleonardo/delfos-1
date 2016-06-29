package br.com.delfos.model.basic;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import br.com.delfos.model.generic.AbstractModel;

@Entity
public class Endereco extends AbstractModel<Endereco> {

	@NotNull
	private String logradouro;
	private String descricao;
	@NotNull
	private String numero;

	private String cep;
	@NotNull
	private String bairro;

	@OneToOne(mappedBy = "endereco")
	private Pessoa pessoa;

	@OneToOne(cascade = CascadeType.DETACH)
	private TipoLogradouro tipoLogradouro;

	@OneToOne
	private Cidade cidade;

	public Endereco(String logradouro, String descricao, String numero, String cep, String bairro,
			TipoLogradouro tipoLogradouro, Cidade cidade) {
		this.logradouro = logradouro;
		this.descricao = descricao;
		this.numero = numero;
		this.cep = cep;
		this.bairro = bairro;
		this.tipoLogradouro = tipoLogradouro;
		this.cidade = cidade;
	}

	public Endereco(Long id, String logradouro, String descricao, String numero, String cep, String bairro,
			TipoLogradouro tipoLogradouro, Cidade cidade) {
		this.id = id;
		this.logradouro = logradouro;
		this.descricao = descricao;
		this.numero = numero;
		this.cep = cep;
		this.bairro = bairro;
		this.tipoLogradouro = tipoLogradouro;
		this.cidade = cidade;
	}

	public Endereco() {
		super();
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getNumero() {
		return numero;
	}

	public String getCep() {
		return cep;
	}

	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

}
