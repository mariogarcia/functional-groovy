package groovyfp.categories;

/**
 * @param <I> the input type to be transformed
 * @param <O> the type of the transformation result
 *
 */
public interface Function<I,O> {
    O apply(I input);
}
