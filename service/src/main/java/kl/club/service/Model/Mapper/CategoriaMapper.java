package kl.club.service.Model.Mapper;

import kl.club.service.Model.DTO.CategoriaRequestDTO;
import kl.club.service.Model.DTO.CategoriaResponseDTO;
import kl.club.service.Model.Entity.Categoria;

import java.util.ArrayList;
import java.util.List;

public final class CategoriaMapper {

    private CategoriaMapper() {
    }

    public static CategoriaResponseDTO toResponse(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getIdCategoria(),
                categoria.getNome(),
                categoria.getAtivo());
    }

    public static List<CategoriaResponseDTO> toResponse(List<Categoria> categorias) {

        List<CategoriaResponseDTO> categoriasResponse = new ArrayList<>();

        for (Categoria categoria : categorias) {
            categoriasResponse.add(toResponse(categoria));
        }

        return categoriasResponse;
    }

    public static Categoria toEntity(CategoriaRequestDTO dto) {

        Categoria categoria = new Categoria();

        categoria.setNome(dto.nome());

        return categoria;
    }
}
