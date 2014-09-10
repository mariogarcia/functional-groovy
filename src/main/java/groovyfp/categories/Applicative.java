package groovyfp.categories;

/**
 *
 * @param <A>
 */
public interface Applicative<A> extends Functor<A> {
    public Type<A> getTypedRef();
    public <B> Applicative<B> fapply(Applicative<Function<A,B>> afn);
}
