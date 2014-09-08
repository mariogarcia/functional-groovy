package groovyfp.categories;

/**
 *
 * @param <VALUE>
 */
public interface Monad<VALUE> extends Applicative<VALUE> {
    public <B, MONAD extends Monad<B>> MONAD bind(Function<VALUE,MONAD> fn);
}
