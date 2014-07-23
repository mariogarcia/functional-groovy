package groovyfp.categories

class Either<T> implements Applicative, Functor<T>, Monad {

    static enum Type {
        LEFT, RIGHT
    }

    T value
    Type type

    Either<T> fmap(Closure fn) {
        return (type == Type.LEFT) ? this : right(fn(value))
    }

    Either fapply(Applicative av) {
        return (type == Type.LEFT) ? this : av.fmap(value)
    }

    Either pure(Object v) {
        return right(v)
    }

    Either bind(Closure<Monad> fn) {
        if(type == Type.LEFT) return this

        return fn(value)
    }

    static Either<T> right(T v) {
        return new Either<T>(value: v, type: Type.RIGHT)
    }

    static Either<T> left(T v) {
        return new Either<T>(value: v, type: Type.LEFT)
    }


}
