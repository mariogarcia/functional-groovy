package groovyfp.streams

final class Stream {

    final Collection<?> col
    Closure<Boolean> filter
    Closure<?> transformer
    Integer howMany

    Stream(Collection<?> col) {
       this.col = col
    }

    static Stream from(Collection<?> source) {
        return new Stream(source)
    }

    Stream filter(Closure<Boolean> filter) {
        this.filter = filter
        this
    }

    Stream map(Closure<?> transformer) {
        this.transformer = transformer
        this
    }

    Stream take(Integer howMany) {
        this.howMany = howMany
        this
    }

    Collection<?> collect() {
        def result = []

        col.takeWhile { val ->
            if (filter(val)) {
                result << transformer(val)
            }
            howMany ? (result.size() < howMany) : true
        }

        return result
    }

}
