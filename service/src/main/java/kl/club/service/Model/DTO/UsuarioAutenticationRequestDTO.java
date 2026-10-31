package kl.club.service.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioAutenticationRequestDTO {

    @NotBlank
    @Size(min = 11, max =11)
    private String cpf;

    public UsuarioAutenticationRequestDTO() {
    }

    public UsuarioAutenticationRequestDTO(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
