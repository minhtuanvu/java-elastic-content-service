package de.funkedigital.fuzo.contentservice.traits;

import java.util.function.Function;

public interface Mappable<T> {

    default <R> R map(Function<T, R> function) {
        return function.apply((T) this);
    }

}
