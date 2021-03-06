package br.com.delfos.model.auditoria;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.delfos.model.basic.Pessoa;
import br.com.delfos.model.generic.AbstractModel;

enum Status {
	ATIVO, INATIVO;
}

@Entity
public class Usuario extends AbstractModel<Usuario> {

	@NotNull
	@Size(min = 4, message = "O login deve conter, no mínimo, quatro caracteres.")
	private String login;

	@Column(unique = true)
	@NotNull
	@Size(min = 6, message = "A senha deve conter, no mínimo, seis caracteres.")
	private String senha;

	private String descricao;

	@ManyToOne(fetch = FetchType.EAGER)
	private PerfilAcesso perfilAcesso;

	@OneToOne(fetch = FetchType.EAGER)
	private Pessoa pessoa;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Usuario(Usuario usuario) {
		this.id = usuario.id;
		this.login = usuario.login;
		this.senha = usuario.senha;
		this.descricao = usuario.descricao;
		this.perfilAcesso = usuario.perfilAcesso;
		this.status = usuario.status;
	}

	public void setStatus(boolean status) {
		if (status) {
			this.status = Status.ATIVO;
		} else {
			this.status = Status.INATIVO;
		}
	}

	public boolean isAtivo() {
		return this.status == Status.ATIVO;
	}

	public Usuario() {
	}

	public Usuario(String login, String senha, PerfilAcesso perfilAcesso) {
		super();
		this.login = login;
		this.senha = senha;
		this.perfilAcesso = perfilAcesso;
	}

	public Usuario(String login, String senha, String descricao, PerfilAcesso perfilAcesso) {
		super();
		this.login = login;
		this.senha = senha;
		this.descricao = descricao;
		this.perfilAcesso = perfilAcesso;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public PerfilAcesso getPerfilAcesso() {
		return perfilAcesso;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
		pessoa.setUsuario(this);
	}

	public void setPerfilAcesso(PerfilAcesso perfilAcesso) {
		this.perfilAcesso = perfilAcesso;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", login=" + login + ", descricao=" + descricao + ", perfilAcesso=" + perfilAcesso
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((perfilAcesso == null) ? 0 : perfilAcesso.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Usuario)) {
			return false;
		}
		Usuario other = (Usuario) obj;
		if (descricao == null) {
			if (other.descricao != null) {
				return false;
			}
		} else if (!descricao.equals(other.descricao)) {
			return false;
		}
		if (login == null) {
			if (other.login != null) {
				return false;
			}
		} else if (!login.equals(other.login)) {
			return false;
		}
		if (perfilAcesso == null) {
			if (other.perfilAcesso != null) {
				return false;
			}
		} else if (!perfilAcesso.equals(other.perfilAcesso)) {
			return false;
		}
		if (pessoa == null) {
			if (other.pessoa != null) {
				return false;
			}
		} else if (!pessoa.equals(other.pessoa)) {
			return false;
		}
		if (senha == null) {
			if (other.senha != null) {
				return false;
			}
		} else if (!senha.equals(other.senha)) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		return true;
	}

}
