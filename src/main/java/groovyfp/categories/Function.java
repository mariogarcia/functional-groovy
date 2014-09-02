package groovyfp.categories;

/**
 * A function represents a transformation applied to a given input, giving
 * a certain result.
 *
 * Because this interface is in fact a functional interface, it could be
 * substituted by a Closure expression.
 *
 * Be aware that because closures only declare the return type you should
 * add the type to the closure parameter if you care about input type.
 *
 * @param <I> the input type to be transformed
 * @param <O> the type of the transformation result
 *
 */
public interface Function<I,O> {
    O apply(I input);
}
