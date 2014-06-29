package groovyfp.streams

final class Stream {

    final Collection<?> col
    Closure<Boolean> filter
    Closure<?> transformer

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

    Collection<?> collect() {
        return col.inject([]){ acc, val ->
            if (filter(val)) {
                acc << transformer(val)
            }
            acc
        }
    }

}
