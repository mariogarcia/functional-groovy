package groovyfp.oop2fn

class Option<T> {

    T value

    Option(T value) {
        this.value = value
    }

    static Option from(T value) {
        return new Option(value)
    }

    Boolean hasValue() {
        return value ? true : false
    }

    T getOrElse(T defaultValue) {
        return value ?: defaultValue
    }

    Option<T> map(Closure<T> transformation) {
        return Option.from(
            value
                .collect(transformation)
                .findResult { it }
            )
    }

}
