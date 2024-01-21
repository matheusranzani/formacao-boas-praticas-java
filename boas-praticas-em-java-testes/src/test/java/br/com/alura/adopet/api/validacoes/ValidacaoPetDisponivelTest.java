package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidacaoPetDisponivelTest {
    @InjectMocks // Objeto que se deseja instanciar e injetar nele os mocks
    private ValidacaoPetDisponivel validacao;
    @Mock
    private PetRepository petRepository;

    @Mock
    private Pet pet;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Test
    void deveriaPermitirSolicitacaoDeAdocaoPet() {
        // Arrange
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(
                7L,
                2L,
                "Motivo qualquer"
        );

        // GWT (Given When Then)
        BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAdotado()).willReturn(false);

        // Assert + Act
        Assertions.assertDoesNotThrow(() -> validacao.validar((dto))); // validar é um método void
    }

    @Test
    void naoDeveriaPermitirSolicitacaoDeAdocaoPet() {
        // Arrange
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(
                7L,
                2L,
                "Motivo qualquer"
        );

        // GWT (Given When Then)
        BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAdotado()).willReturn(true);

        // Assert + Act
        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar((dto))); // validar pode lançar uma exceçãp
    }
}