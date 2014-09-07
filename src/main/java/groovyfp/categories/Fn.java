package groovyfp.categories;

/**
 *
 * @author mario
 */
public final class Fn {
    
    static <T> Either.Left<T> Left(T source) {
        return Either.left(source);
    }
    
    static <T> Either.Right<T> Right(T source) {
        return Either.right(source);
    }
    
    static <T> Maybe.Just<T> Just(T source) {
        return Maybe.just(source);
    }
    
    static <T> Maybe.Nothing<T> Nothing() {
        return Maybe.nothing();
    }
    
}
