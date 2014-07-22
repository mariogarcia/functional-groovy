package groovyfp.categories

interface Applicative extends Functor {
    Applicative fapply(Applicative av)
    Applicative pure(Object v)
    Object getValue()
}
