package groovyfp.oop2fn

abstract class AbstractFinancialProcess {

    abstract Double calculateTaxes(Double amount)
    abstract Double calculateExtras(Double amount)

    Double calculate(Double amount) {
        return amount +
            calculateTaxes(amount) +
            calculateExtras(amount)
    }

}
