package duda.meneses.odiometro.model;

import lombok.Builder;
import lombok.Getter;
import lombok.With;

@With
@Getter
@Builder
public class Sentiment {
    private final String name;
    private final int negative;
    private final int total;
}
