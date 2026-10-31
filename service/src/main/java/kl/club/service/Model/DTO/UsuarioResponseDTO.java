package kl.club.service.Model.DTO;

public class UsuarioResponseDTO {

    private Integer idUsuario;
    private String nome;
    private String cpf;
    private String telefone;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Integer idUsuario, String nome, String cpf, String telefone) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
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
}
