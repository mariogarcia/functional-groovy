package groovyfp.filter

/**
 * This class can apply several filtering options to the same collection
 */
final class ColFn<T> {

    private final List<T> immutableList
    private Closure<?> filteringConstraints

    /**
     * Default constructor is disabled
     */
    private ColFn() {}
    /**
     * This constructor initializes the immutable list
     *
     * @param initList The list you want to filter
     */
    private ColFn(List<T> initList) {
        this.immutableList = initList
    }

    /**
     * This is the entry point for the collection.
     * Once you pass the list your can filter it
     *
     * @param list
     * @return a new instance of ColFn
     */
    static <T> ColFn<T> from(List<T> list) {
        return new ColFn(list)
    }

    /**
     * This method receives all possible constraint filters
     *
     * @param filters
     * @return the instance of ColFn
     */
    ColFn<T> filter(Closure<Boolean>... filters) {
        this.filteringConstraints = { fs, e -> fs*.call(e).every { x -> x } }.curry(filters as List)
        return this
    }

    /**
     * This method returns the result when applying filters
     *
     * @return a new list with the filtered elements
     */
    List<T> getResult() {
        return { pred, col -> col.findAll(pred) }.curry(filteringConstraints).call(immutableList)
    }

}
