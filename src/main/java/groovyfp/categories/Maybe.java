package groovyfp.categories;

/**
 *
 * @param <A>
 */
public class Maybe<A> implements Monad<A> {

    private final A value;
    
    private Maybe(final A a) {
        this.value = a;
    }   

    @Override
    public A getValue() {
        return this.value;
    }

    @Override
    public <T> Monad<T> bind(Function<A, ? extends Monad<T>> fn) {
        return isNothing() ? (Monad<T>) nothing() : just(fn.apply(value).getValue());
    }

    @Override
    public <B> Applicative<B> fapply(Applicative<Function<A, B>> afn) {
        return isNothing() ? (Applicative<B>) nothing() : just(afn.getValue().apply(value));
    }

    @Override
    public <B> Functor<B> fmap(Function<A, B> fn) {
        return isNothing() ? (Functor<B>) nothing() : just(fn.apply(value));
    }
    
    public static <T> Maybe<T> just(T value) {
        return new Maybe(value);
    }
    
    public static <T> Maybe<T> nothing() {
        return new Maybe(null);
    }
    
    public Boolean isNothing() {
        return this.value == null;
    }
    
}
