package groovyfp.categories;

/**
 *
 * @author mario
 */
public final class Fn {
    
    public static <A> Either.Left<A> Left(A source) {
        return Either.left(source);
    }
    
    public static <A> Either.Right<A> Right(A source) {
        return Either.right(source);
    }
    
    public static <A> Maybe.Just<A> Just(A source) {
        return Maybe.just(source);
    }
    
    public static <A> Maybe.Nothing<A> Nothing() {
        return Maybe.nothing();
    }
    
    public static <A,B,M extends Monad<B>> M bind(A a, Function<A,M> fn) {
        return fn.apply(a);
    }

}
