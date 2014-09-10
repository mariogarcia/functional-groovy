package groovyfp.categories;

/**
 *
 * @param <A>
 */
public abstract class Maybe<A> implements Monad<A> {

    private final Type<A> value;

    protected Maybe(Type<A> value) {
        this.value = value;
    }

    @Override
    public Type<A> getValue() {
        return this.value;
    }

    public static class Just<JUST> extends Maybe<JUST> {

        public Just(Type<JUST> value) {
            super(value);
        }

        // tag::fapply[]
        @Override
        public <B> Just<B> fapply(Applicative<Function<JUST, B>> afn) {
            return this.fmap(afn.getValue().getValue());
        }
        // end::fapply[]

        // tag::functorspec2[]
        @Override
        public <B, F extends Functor<B>> F fmap(Function<JUST, B> fn) {
            return (F) just(fn.apply(this.getValue().getValue()));
        }
        // end::functorspec2[]

        @Override
        public <B, M extends Monad<B>> M bind(Function<JUST, M> fn) {
            return fn.apply(getValue().getValue());
        }

    }

    public static class Nothing<NOTHING> extends Maybe<NOTHING> {

        public Nothing() {
            super(new Type(null));
        }

        @Override
        public <B> Nothing<B> fapply(Applicative<Function<NOTHING, B>> afn) {
            return new Nothing();
        }


        @Override
        public <B, M extends Monad<B>> M bind(Function<NOTHING, M> fn) {
            return (M) new Nothing();
        }

        @Override
        public <B, F extends Functor<B>> F fmap(Function<NOTHING, B> fn) {
        return (F)new Nothing();
        }

    }

    public static <T> Maybe.Just<T> just(T value) {
        return new Maybe.Just(new Type(value));
    }

    public static <T> Maybe.Nothing<T> nothing() {
        return new Maybe.Nothing();
    }

}
