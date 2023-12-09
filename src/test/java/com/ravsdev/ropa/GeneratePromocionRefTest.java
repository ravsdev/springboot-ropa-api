package com.ravsdev.ropa;

import static com.ravsdev.ropa.util.GeneratePromocionRef.generateRef;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class GeneratePromocionRefTest {

    @Test
    void generatePromocionRefCorrectly() throws Exception{
        String ref = generateRef("Black Friday 2023");

        assertThat(ref).isEqualTo("BF2023");
    }
}
