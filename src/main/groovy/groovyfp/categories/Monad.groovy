package groovyfp.categories

interface Monad {
    Monad bind(Closure<Monad> mfn)
}
