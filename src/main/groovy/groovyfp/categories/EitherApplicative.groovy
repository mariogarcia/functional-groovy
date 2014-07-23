package groovyfp.categories

class EitherApplicative<T> implements Applicative, Functor<T> {

    static enum Type {
        LEFT, RIGHT
    }

    T value
    Type type

    EitherApplicative<T> fmap(Closure fn) {
        return (type == Type.LEFT) ? this : right(fn(value))
    }

    EitherApplicative fapply(Applicative av) {
        return (type == Type.LEFT) ? this : av.fmap(value)
    }

    EitherApplicative pure(Object v) {
        return right(v)
    }

    static EitherApplicative<T> right(T v) {
        return new EitherApplicative<T>(value: v, type: Type.RIGHT)
    }

    static EitherApplicative<T> left(T v) {
        return new EitherApplicative<T>(value: v, type: Type.LEFT)
    }


}
