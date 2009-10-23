package net.zorched.constraints

class ComparisonConstraint {
    
    static name = "compareTo"
    static expectsParams = true
    static defaultMessage = "Property [{0}] of class [{1}] with value [{2}] does not match the property [{3}]"

    def validate = { val, target ->
        def compareVal = target."${params}"
        return val == compareVal
    }
}
