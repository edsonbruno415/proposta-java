package com.edson.proposta_app.mapper;

import com.edson.proposta_app.dto.PropostaRequestDto;
import com.edson.proposta_app.dto.PropostaResponseDto;
import com.edson.proposta_app.entity.Proposta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Mapper
public interface PropostaMapper {


    PropostaMapper INSTANCE = Mappers.getMapper(PropostaMapper.class);

    @Mapping(target = "aprovada", ignore = true)
    @Mapping(target = "integrada", constant = "true")
    @Mapping(target = "observacao", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario.nome", source = "nome")
    @Mapping(target = "usuario.sobrenome", source = "sobrenome")
    @Mapping(target = "usuario.cpf", source = "cpf")
    @Mapping(target = "usuario.telefone", source = "telefone")
    @Mapping(target = "usuario.renda", source = "renda")
    Proposta convertDtoToProposta(PropostaRequestDto propostaRequestDto);

    @Mapping(target = "nome", source = "usuario.nome")
    @Mapping(target = "sobrenome", source = "usuario.sobrenome")
    @Mapping(target = "cpf", source = "usuario.cpf")
    @Mapping(target = "telefone", source = "usuario.telefone")
    @Mapping(target = "renda", source = "usuario.renda")
    @Mapping(target = "valorSolicitadoFmt", expression = "java(setValorSolicitadoFmt(proposta))")
    PropostaResponseDto convertEntityToDto(Proposta proposta);

    List<PropostaResponseDto> convertListEntityToListDto(Iterable<Proposta> propostas);

    default String setValorSolicitadoFmt(Proposta proposta){
       return NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(proposta.getValorSolicitado());
    }
}
