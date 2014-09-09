package groovyfp.categories;

/**
 *
 * @param <TYPE>
 */
public abstract class Maybe<TYPE> implements Monad<TYPE> {

    private final TYPE value;

    protected Maybe(TYPE value) {
        this.value = value;
    }
    
    @Override
    public TYPE getValue() {
        return this.value;
    }

    public static class Just<JUST> extends Maybe<JUST> {

        public Just(JUST value) {
            super(value);
        }

        // tag::fapply[]
        @Override
        public <B> Just<B> fapply(Applicative<Function<JUST, B>> afn) {
            return this.fmap(afn.getValue());
        }
        // end::fapply[]

        // tag::functorspec2[]
        @Override
        public <B> Just<B> fmap(Function<JUST, B> fn) {
            return new Just(fn.apply(getValue()));
        }
        // end::functorspec2[]

        @Override
        public <B, M extends Monad<B>> M bind(Function<JUST, M> fn) {
            return fn.apply(getValue());
        }
      
    }
    
    public static class Nothing<NOTHING> extends Maybe<NOTHING> {

        public Nothing() {
            super(null);
        }
        
        @Override
        public <B> Nothing<B> fapply(Applicative<Function<NOTHING, B>> afn) {
            return new Nothing();
        }

        @Override
        public <B> Nothing<B> fmap(Function<NOTHING, B> fn) {
            return new Nothing();
        }

        @Override
        public <B, M extends Monad<B>> M bind(Function<NOTHING, M> fn) {
            return (M) new Nothing();
        }
    
    }
    
    public static <T> Maybe.Just<T> just(T value) {
        return new Maybe.Just(value);
    }
    
    public static <T> Maybe.Nothing<T> nothing() {
        return new Maybe.Nothing();
    }
    
}
