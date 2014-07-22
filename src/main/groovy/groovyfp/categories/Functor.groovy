package groovyfp.categories

interface Functor<T> {
    Functor<T> fmap(Closure fn)
}
