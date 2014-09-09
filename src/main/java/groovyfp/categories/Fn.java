package groovyfp.categories;

/**
 *
 * @author mario
 */
public final class Fn {
    
    public static <T> Either.Left<T> Left(T source) {
        return Either.left(source);
    }
    
    public static <T> Either.Right<T> Right(T source) {
        return Either.right(source);
    }
    
    public static <T> Maybe.Just<T> Just(T source) {
        return Maybe.just(source);
    }
    
    public static <T> Maybe.Nothing<T> Nothing() {
        return Maybe.nothing();
    }
    
    public static <A,B,M extends Monad<B>> M bind(A a, Function<A,M> fn) {
        return fn.apply(a);
    }

}
