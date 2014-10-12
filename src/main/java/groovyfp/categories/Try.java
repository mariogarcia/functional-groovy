package groovyfp.categories;

/**
 *
 * @author mario
 * @param <A>
 */
public abstract class Try<A extends Function<?,?>> implements Monad<A> {

	 private final Type<A> typedRef;

	 public Try(Type<A> fn) {
		this.typedRef = fn;
	 }

	 @Override
	 public Type<A> getTypedRef() {
		  return this.typedRef;
	 }

	 public abstract Boolean isSuccess();
	 public abstract Boolean isFailure() ;

	 public static class Failure<FN extends Function<?,?>> extends Try<FN> {

		private final Throwable throwable;

		public Failure(Type<FN> valueRef) {
		  super(valueRef);
		  this.throwable = 
			  new IllegalStateException("Value: " + valueRef.getValue() + " is not valid");
		}

		public Failure(Type<FN> valueRef, Throwable throwable) {
		  super(valueRef);
		  this.throwable = throwable;
		}

		@Override
		public <B, M extends Monad<B>> M bind(Function<FN, M> fn) {
		  return (M) new Failure(this.getTypedRef());
		}

		@Override
		public <B> Applicative<B> fapply(Applicative<Function<FN, B>> afn) {
			return new Failure(this.getTypedRef());
		}

		@Override
		public <B, F extends Functor<B>> F fmap(Function<FN, B> fn) {
			return (F) new Failure(this.getTypedRef());
		}

		@Override
		public Boolean isFailure() {
			return true;
		}

		@Override
		public Boolean isSuccess() {
			return false;
		}

		public void throwException() throws Throwable {
			throw this.throwable;
		}
		
		public Throwable getException() {
			 return this.throwable;
		}
		
	 }

	 public static class Success<FN extends Function<?,?>> extends Try<FN> {

		public Success(Type<FN> valueRef) {
			super(valueRef);
		}

		@Override
		public Boolean isSuccess() {
			return true;
		}

		@Override
		public Boolean isFailure() {
			return false;
		}

		@Override
		public <B, M extends Monad<B>> M bind(Function<FN, M> fn) {
			 return fn.apply(getTypedRef().getValue());
		}

		@Override
		public <B> Applicative<B> fapply(Applicative<Function<FN, B>> afn) {
			  return this.fmap(afn.getTypedRef().getValue());
		}

		@Override
		public <B, F extends Functor<B>> F fmap(Function<FN, B> fn) {
			try {
				return (F) new Success(new Type(fn.apply(getTypedRef().getValue())));
			} catch (Throwable th) {
				return (F) new Failure(this.getTypedRef(), th);
			}
		}

	 }
	
	 public static <FN extends Function<?,?>> Try<FN> Try(Function<?,?> fn) {
		 return new Try.Success<>(new Type(fn));
	 }
	
}
