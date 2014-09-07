package groovyfp.categories;

/**
 *
 * @param <TYPE>
 */
public abstract class Maybe<TYPE> implements Monad<TYPE>, Applicative<TYPE>, Functor<TYPE> {

    private final TYPE value;

    public Maybe(TYPE value) {
        this.value = value;
    }
    
    @Override
    public TYPE getValue() {
        return this.value;
    }

    static class Just<JUST> extends Maybe<JUST> {

        public Just(JUST value) {
            super(value);
        }

        @Override
        public <B> Just<B> fapply(Applicative<Function<JUST, B>> afn) {
            return this.fmap(afn.getValue());
        }

        // tag::functorspec2[]
        @Override
        public <B> Just<B> fmap(Function<JUST, B> fn) {
            return new Just(fn.apply(getValue()));
        }
        // end::functorspec2[]

        @Override
        public <B> Just<?> bind(Function<JUST, Monad<B>> fn) {
            return this.fmap(fn);
        }
    }
    
    static class Nothing<NOTHING> extends Maybe<NOTHING> {

        public Nothing() {
            super(null);
        }

        @Override
        public <B> Nothing<?> bind(Function<NOTHING, Monad<B>> fn) {
            return new Nothing();
        }

        @Override
        public <B> Nothing<B> fapply(Applicative<Function<NOTHING, B>> afn) {
            return new Nothing();
        }

        @Override
        public <B> Nothing<B> fmap(Function<NOTHING, B> fn) {
            return new Nothing();
        }
    
    }
    
    static <T> Just<T> just(T value) {
        return new Just(value);
    }
    
    static <T> Nothing<T> nothing() {
        return new Nothing();
    }
    
}
