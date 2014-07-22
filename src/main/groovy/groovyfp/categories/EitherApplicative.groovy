package groovyfp.categories

class EitherApplicative<T> extends EitherFunctor<T> implements Applicative {

    Applicative fapply(Applicative av) {
        return (type == Type.LEFT) ? this : av.fmap(value)
    }

    Applicative pure(Object v) {
        return right(v)
    }

}
