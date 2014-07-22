package groovyfp.categories

class EitherFunctor<T> implements Functor<T> {

    static enum Type {
        LEFT, RIGHT
    }

    T value
    Type type

    EitherFunctor<T> fmap(Closure fn) {
        return (type == Type.LEFT) ? this : right(fn(value))
    }

    static EitherFunctor<T> right(T v) {
        return new EitherFunctor<T>(value: v, type: Type.RIGHT)
    }

    static EitherFunctor<T> left(T v) {
        return new EitherFunctor<T>(value: v, type: Type.LEFT)
    }
}
