package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public class UsuarioRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    @CPF
    @Size(min = 11, max =11)
    private String cpf;

    @NotBlank
    @Size(max = 20, min = 11)
    private String telefone;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(String nome, String cpf, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
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
