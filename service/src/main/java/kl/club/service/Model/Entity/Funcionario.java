package kl.club.service.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kl.club.service.Model.enums.NivelFuncionario;

import java.util.Objects;

@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Integer idFuncionario;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false)
    private NivelFuncionario nivel;

    @Column(name = "ativo", nullable = false, columnDefinition = "TINYINT")
    private Boolean ativo;

    public Funcionario() {
    }

    public Funcionario(Integer idFuncionario, String nome, String email, String senha, NivelFuncionario nivel,
                       Boolean ativo) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.nivel = nivel;
        this.ativo = ativo;
    }

    @PrePersist
    void prePersist() {
        if (ativo == null) {
            ativo = true;
        }
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public NivelFuncionario getNivel() {
        return nivel;
    }

    public void setNivel(NivelFuncionario nivel) {
        this.nivel = nivel;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Funcionario other)) return false;
        return idFuncionario != null && Objects.equals(idFuncionario, other.idFuncionario);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Funcionario{idFuncionario=" + idFuncionario + ", nome='" + nome + "', email='" + email
                + "', nivel=" + nivel + ", ativo=" + ativo + "}";
    }
}
