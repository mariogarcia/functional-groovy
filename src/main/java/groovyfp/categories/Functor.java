package groovyfp.categories;

/**
 *
 * @param <A>
 */
public interface Functor<A> {
    public <B> Functor<B> fmap(Function<A,B> fn);
}
