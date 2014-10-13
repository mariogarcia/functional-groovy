package groovyfp.categories;

import java.util.List;

/**
 *
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

    public static <A> ListMonad<A> List(A... values) {
        return ListMonad.list(values);
    }

    public static <A> ListMonad<A> List(List<A> values) {
        return ListMonad.list(values);
    }

    public static <A,B,F extends Function<A,B>> Try.Success Try(F fn) {
        return new Try.Success<F>(new Type(fn));
    }

    public static <A,B,MA extends Monad<A>,MB extends Monad<B>> MB bind(MA ma, Function<A,MB> fn) {
       return ma.bind(fn);
    }

    public static <A,B, FA extends Functor<A>, FB extends Functor<B>> FB fmap(FA fa, Function<A,B> fn) {
        return fa.fmap(fn);
    }

}
