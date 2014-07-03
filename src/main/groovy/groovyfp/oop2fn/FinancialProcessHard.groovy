package groovyfp.oop2fn

class FinancialProcessHard extends AbstractFinancialProcess {

    Double calculateTaxes(Double amount) {
        return amount * 0.10
    }

    Double calculateExtras(Double amount) {
        return amount * 0.5
    }

}

