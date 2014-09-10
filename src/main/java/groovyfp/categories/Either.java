package groovyfp.categories;

/**
 *
 * @param <A>
 */
public abstract class Either<A> implements Monad<A> {
    
    private final Type<A> value;
    
    protected Either(Type<A> value) {
        this.value = value;
    }
    
    @Override
    public Type<A> getValue() {
        return this.value;
    }
    
    public boolean isLeft() {
        return false;
    }
    
    public boolean isRight() {
        return false;
    }
    
    public static class Right<R> extends Either<R> {

        public Right(Type<R> value) {
            super(value);
        }
        
        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public <B> Right<B> fapply(Applicative<Function<R, B>> afn) {
            return this.fmap(afn.getValue().getValue());
        }


        @Override
        public <B, M extends Monad<B>> M bind(Function<R, M> fn) {
            return fn.apply(getValue().getValue());
        }

        @Override
        public <B, F extends Functor<B>> F fmap(Function<R, B> fn) {
            return (F) right(fn.apply(getValue().getValue()));
        }
        
    }
    
    public static class Left<L> extends Either<L> {

        public Left(Type<L> value) {
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
        public <B, M extends Monad<B>> M bind(Function<L, M> fn) {
            return (M) new Left(getValue());
        }

        @Override
        public <B, F extends Functor<B>> F fmap(Function<L, B> fn) {
            return (F) new Left(getValue());
        }
        
    }
    
    public static <T> Right<T> right(T value) {
        return new Right(new Type(value));
    }
    
    public static <T> Left<T> left(T value) {
        return new Left(new Type(value));
    }
    
}
