package uk.ac.standrews.variantchessgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.standrews.variantchessgame.model.VariantChessBoard;

@Configuration
public class GameConfig {

    @Bean
    public VariantChessBoard variantChessBoard() {
        return new VariantChessBoard();
    }
}
