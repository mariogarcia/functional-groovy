package groovyfp.categories;

/**
 *
 * @param <A>
 */
public abstract class Either<A> implements Monad<A> {
    
    private final A value;
    
    protected Either(A value) {
        this.value = value;
    }
    @Override
    public A getValue() {
        return this.value;
    }
    
    public boolean isLeft() {
        return false;
    }
    
    public boolean isRight() {
        return false;
    }
    
    public static class Right<R> extends Either<R> {

        public Right(R value) {
            super(value);
        }
        
        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public <B> Right<B> fapply(Applicative<Function<R, B>> afn) {
            return this.fmap(afn.getValue());
        }
        
        @Override
        public <B> Right<B> fmap(Function<R, B> fn) {
            return right(fn.apply(getValue()));
        }

        @Override
        public <B, M extends Monad<B>> M bind(Function<R, M> fn) {
            return fn.apply(getValue());
        }
        
    }
    
    public static class Left<L> extends Either<L> {

        public Left(L value) {
            super(value);
        }

        @Override
        public boolean isLeft() {
            return true;
        }
        
        @Override
        public <B> Left<B> fapply(Applicative<Function<L, B>> afn) {
            return new Left(getValue());
        }
        
        @Override
        public <B> Left<B> fmap(Function<L, B> fn) {
            return new Left(getValue());
        }

        @Override
        public <B, M extends Monad<B>> M bind(Function<L, M> fn) {
            return (M) new Left(getValue());
        }
        
    }
    
    public static <T> Right<T> right(T value) {
        return new Right(value);
    }
    
    public static <T> Left<T> left(T value) {
        return new Left(value);
    }
    
}
