package groovyfp.option

class Option<T> {

    T value

    Boolean hasValue() {
        return value ? true : false
    }

    T getOrElse(T defaultValue) {
        return value ?: defaultValue
    }

}
