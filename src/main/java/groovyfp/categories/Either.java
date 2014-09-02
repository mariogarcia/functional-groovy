package groovyfp.categories;

/**
 *
 */
public class Either<A> implements Monad<A> {

    static enum Type {
        LEFT, RIGHT
    }
    
    private A value;
    private Type type;
    
    private Either(A value, Type type) {
        this.value = value;
        this.type = type;
    }
    
    @Override
    public <T> Monad<T> bind(Function<A, ? extends Monad<T>> fn) {
        return isLeft() ? (Monad<T>) left(value) : fn.apply(value);
    }

    @Override
    public A getValue() {
        return this.value;
    }

    @Override
    public <B> Applicative<B> fapply(Applicative<Function<A, B>> afn) {
        return isLeft() ? (Monad<B>)left(value) : right(afn.getValue().apply(value));
    }
    
    public Boolean isLeft() {
        return this.type == Type.LEFT;
    }

    @Override
    public <B> Functor<B> fmap(Function<A, B> fn) {
        return isLeft() ? (Monad<B>) left(value) : right(fn.apply(value));
    }
    
    public static <T> Either<T> left(T value) {
        return new Either(value, Type.LEFT);
    }
    
    public static <T> Either<T> right(T value) {
        return new Either(value, Type.RIGHT);
    }
    
}
