package groovyfp.categories;

/**
 *
 * @param <A>
 */
public interface Monad<A> extends Applicative<A> {
    public <T> Monad<T> bind(Function<A,? extends Monad<T>> fn);
}
