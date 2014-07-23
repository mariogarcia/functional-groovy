package groovyfp.categories

interface Applicative {
    Applicative fapply(Applicative av)
    Applicative pure(Object v)
    Object getValue()
}
