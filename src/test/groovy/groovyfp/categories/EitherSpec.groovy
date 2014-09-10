package groovyfp.categories

import static Either.right
import static Either.left

import spock.lang.Specification

class EitherSpec extends Specification {
    
    void 'Applicative: Either applicative implementation'() {
        when:
            def inc = { Integer v -> v + 1 }
        then:
            left(null).fapply(right(1)).value.value == null
    }
    
    // tag::functor2[]
    void 'Either functor implementation'() {
        given: 'a function we want to apply'
            def inc = { Integer v -> v + 1 }
        expect: 'to return the result of applying the function when RIGHT'
            right(1).fmap(inc).value.value == 2
            right(2).fmap(inc).value.value == 3
        and: 'to return the same input when LEFT'
            left(null).fmap(inc).value.value == null
    }
    // end::functor2[]

    // tag::functor3[]
    void 'first law'() {
        when: 'mapping identity function overy every item in a container'
            def identity = { Integer v -> v }
        then: 'it has no effect'
            right(1).fmap(identity).value.value == 1
    }
    // end::functor3[]

    // tag::functor4[]
    void 'second law'() {
        when: 'Mapping a composition of two functions over every item in a container'
            def inc = { Integer v -> v + 1 }
            def twoTimes = { Integer v -> v * 2 }
            def composition = inc >> twoTimes
        then: 'the same as first mapping one function'
        and: 'then mapping the other'
            right(1)
                .fmap(composition)
                .value.value == right(right(1).fmap(inc).value.value)
                            .fmap(twoTimes)
                            .value.value
    }
    // end::functor4[]
    
    // tag::eithermonad1[]
    void 'Either monad'() {
        when: 'having a fmap function'
            def inc = { Integer v -> v + 1 }
        and: 'the monad bind function'
            def mfn = { x -> Either.right(inc(x)) }
        then: 'when applying to a right the monad will progress'
            right(1).bind(mfn).value.value == 2
        and: 'when applying to a left it wont go any further'
            left(1).bind(mfn).value.value == 1
    }
    // end::eithermonad1[]
    
    void 'Either monad when some function returns a left value'() {
        when: 'having a function that could return a left value'
            def div = { Integer v -> 
                return v == 0 ? Either.left(v)  : Either.right(1/v) 
            }
        then: 'if apply a valid value then the function will be applied'
            right(1).bind(div).value.value == 1
        and: 'otherwise if using 0 I will get a left zero'
            with(left(0).bind(div)) {
                value.value == 0
            }
    }
  
    void 'using Maybe to chain failback searchs'() {
        given:'a base function to search by a certain criteria'
            def baseSearch = { Closure<Boolean> search ->
                return { List sample ->
                    def pr = sample.find(search)
                    // if found then return left to shortcircuit further
                    // searchs
                    pr ? Either.left(pr) : Either.right(sample)
                }
            }
        and: 'composed criterias on top of the base function'
            // they become Function<A,Monad<B>>
            def lookByNameJohn = baseSearch { it.name == 'john' }
            def lookByAgeGreaterThan = baseSearch { it.age > 50 }
            def lookByCity = baseSearch { it.city == 'dublin' }
        when: 'chaining all search criterias'
            Either result = 
                Either.right(sample)
                    .bind(lookByNameJohn)
                    .bind(lookByAgeGreaterThan)
                    .bind(lookByCity)
        then: 'there should be only one item'
            result.isLeft()
            result.value.value.name == name_of_the_result
        where: 'samples used in this spec are'
            sample          |   name_of_the_result
            firstSample     |       'john'
            secondSample    |       'peter'
            thirdSample     |       'rob'
    }
    
    List<Map> getFirstSample() {
        return [
            [name: 'john', age: 32, city: 'barcelona'],
            [name: 'peter', age: 51, city: 'london'],
            [name: 'rob', age: 32, city: 'dublin']
        ]
    }
    
    List<Map> getSecondSample() {
        return [
            [name: 'peter', age: 51, city: 'london'],
            [name: 'rob', age: 32, city: 'dublin'],
            [name: 'johnny', age: 32, city: 'barcelona']
        ]
    }
    
    List<Map> getThirdSample() {
        return [            
            [name: 'rob', age: 32, city: 'dublin'],
            [name: 'johnny', age: 32, city: 'barcelona'],            
            [name: 'peter', age: 50, city: 'london']
        ]
    }
        
}

