package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AbrigoService {
    @Autowired
    AbrigoRepository abrigoRepository;

    @Autowired
    PetRepository petRepository;

    public List<AbrigoDto> listar() {
        return abrigoRepository
                .findAll()
                .stream()
                .map(AbrigoDto::new)
                .toList();
    }

    public void cadastrar(CadastroAbrigoDto dto) {
        boolean jaCadastrado = abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email());

        if (jaCadastrado) {
            throw  new ValidacaoException("Dados já cadastrados para outro abrigo!");
        }

        abrigoRepository.save(new Abrigo());
    }

    public List<PetDto> listarPetsDoAbrigo(String idOuNome) {
        Abrigo abrigo = carregarAbrigo(idOuNome);

        return petRepository
                .findByAbrigo(abrigo)
                .stream()
                .map(PetDto::new)
                .toList();
    }

    public Abrigo carregarAbrigo(String idOuNome) {
        Optional<Abrigo> optional;

        try {
            Long id = Long.parseLong(idOuNome);
            optional = abrigoRepository.findById(id);
        } catch (NumberFormatException exception) {
            optional = Optional.ofNullable(abrigoRepository.findByNome(idOuNome));
        }

        return optional.orElseThrow(() -> new ValidacaoException("Abrigo não encontrado"));
    }
}
