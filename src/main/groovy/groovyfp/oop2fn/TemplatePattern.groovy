package groovyfp.oop2fn

final class TemplatePattern {

    static Double calculateWithVatAndDiscount(Double price, Closure<Double> vat, Closure<Double> discount) {
        return (discount << vat << price)
    }

}
