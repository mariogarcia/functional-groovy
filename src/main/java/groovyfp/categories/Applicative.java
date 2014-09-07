package groovyfp.categories;

/**
 *
 * @param <VALUE>
 */
public interface Applicative<VALUE> extends Functor<VALUE> {
    public VALUE getValue();
    public <B> Applicative<B> fapply(Applicative<Function<VALUE,B>> afn);
}
