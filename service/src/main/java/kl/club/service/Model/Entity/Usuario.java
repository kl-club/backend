package kl.club.service.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 11, columnDefinition = "CHAR(11)")
    private String cpf;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "conta_criada", nullable = false)
    private LocalDateTime contaCriada;

    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidos = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String nome, String cpf, String telefone, LocalDateTime contaCriada) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.contaCriada = contaCriada;
    }

    @PrePersist
    void prePersist() {
        if (contaCriada == null) {
            contaCriada = LocalDateTime.now();
        }
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getContaCriada() {
        return contaCriada;
    }

    public void setContaCriada(LocalDateTime contaCriada) {
        this.contaCriada = contaCriada;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario other)) return false;
        return idUsuario != null && Objects.equals(idUsuario, other.idUsuario);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Usuario{idUsuario=" + idUsuario + ", nome='" + nome + "', telefone='" + telefone
                + "', contaCriada=" + contaCriada + "}";
    }
}
