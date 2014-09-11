package groovyfp.categories;

/**
 *
 */
public final class Fn {

    public static <A> Either.Left<A> left(A source) {
        return Either.left(source);
    }

    public static <A> Either.Right<A> light(A source) {
        return Either.right(source);
    }

    public static <A> Maybe.Just<A> just(A source) {
        return Maybe.just(source);
    }

    public static <A> Maybe.Nothing<A> nothing() {
        return Maybe.nothing();
    }

    public static <A,B,MA extends Monad<A>,MB extends Monad<B>> MB bind(MA ma, Function<A,MB> fn) {
       return ma.bind(fn);
    }

    public static <A,B, FA extends Functor<A>, FB extends Functor<B>> FB fmap(FA fa, Function<A,B> fn) {
        return fa.fmap(fn);
    }

}
