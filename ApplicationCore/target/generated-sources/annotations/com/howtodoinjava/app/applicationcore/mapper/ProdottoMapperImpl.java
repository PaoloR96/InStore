package com.howtodoinjava.app.applicationcore.mapper;

import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.entity.Taglia;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T16:44:01+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ProdottoMapperImpl implements ProdottoMapper {

    @Override
    public ProdottoDTO prodottoToProdottoDTO(Prodotto prodotto) {
        if ( prodotto == null ) {
            return null;
        }

        ProdottoDTO prodottoDTO = new ProdottoDTO();

        prodottoDTO.setIdProdotto( prodotto.getIdProdotto() );
        prodottoDTO.setNomeProdotto( prodotto.getNomeProdotto() );
        prodottoDTO.setDescrizione( prodotto.getDescrizione() );
        prodottoDTO.setPrezzo( prodotto.getPrezzo() );
        if ( prodotto.getTaglia() != null ) {
            prodottoDTO.setTaglia( prodotto.getTaglia().name() );
        }
        prodottoDTO.setPathImmagine( prodotto.getPathImmagine() );
        prodottoDTO.setQuantitaTotale( prodotto.getQuantitaTotale() );

        return prodottoDTO;
    }

    @Override
    public Prodotto prodottoDTOToProdotto(ProdottoDTO prodottoDTO) {
        if ( prodottoDTO == null ) {
            return null;
        }

        Prodotto prodotto = new Prodotto();

        prodotto.setIdProdotto( prodottoDTO.getIdProdotto() );
        prodotto.setNomeProdotto( prodottoDTO.getNomeProdotto() );
        prodotto.setDescrizione( prodottoDTO.getDescrizione() );
        prodotto.setPrezzo( prodottoDTO.getPrezzo() );
        prodotto.setPathImmagine( prodottoDTO.getPathImmagine() );
        prodotto.setQuantitaTotale( prodottoDTO.getQuantitaTotale() );
        if ( prodottoDTO.getTaglia() != null ) {
            prodotto.setTaglia( Enum.valueOf( Taglia.class, prodottoDTO.getTaglia() ) );
        }

        return prodotto;
    }
}
