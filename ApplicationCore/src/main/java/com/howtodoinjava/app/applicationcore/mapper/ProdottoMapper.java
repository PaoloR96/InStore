package com.howtodoinjava.app.applicationcore.mapper;



import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProdottoMapper {

    ProdottoMapper INSTANCE = Mappers.getMapper(ProdottoMapper.class);

    ProdottoDTO prodottoToProdottoDTO(Prodotto prodotto);

    Prodotto prodottoDTOToProdotto(ProdottoDTO prodottoDTO);
}
