package groovyfp.categories;

/**
 *
 * @param <VALUE>
 */
public interface Monad<VALUE> extends Applicative<VALUE> {
    public <B> Monad<B> bind(Function<VALUE,Monad<B>> fn);
}
