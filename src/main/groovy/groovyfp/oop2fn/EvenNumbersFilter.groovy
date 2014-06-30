package groovyfp.oop2fn

class EvenNumbersFilter implements Filter<Integer> {
    Boolean accept(Integer number) {
        return number % 2 == 0
    }
}
