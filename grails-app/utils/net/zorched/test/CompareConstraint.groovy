package net.zorched.test

import org.codehaus.groovy.runtime.ArrayUtil

/**
 * Use it by doing something like:
 * endDate(compare: [op: '>', field:'startDate'])
 * lowerRange(compare: [op: '<=', field:'upperRange'])
 */
class CompareConstraint {
    static expectsParams = ['op', 'field']

    def validate = { val, target, errors ->
        def compareVal = target."$params.field"
        def result = false
        if (val && compareVal) {
            switch (params.op) {
                case ">": result = val.compareTo(compareVal) > 0; break
                case ">=": result = val.compareTo(compareVal) >= 0; break
                case "==": result = val.compareTo(compareVal) == 0; break
                case "<=": result = val.compareTo(compareVal) <= 0; break
                case "<": result = val.compareTo(compareVal) < 0; break
                default: throw new IllegalArgumentException("Invalid operation: " + params.op)
            }
        }

        if (!result) {
            //TODO: constraintPropertyName doesn't seem to get set? not sure why not.
            def args = ArrayUtil.createArray(constraintPropertyName, val, params.op, params.field)
            errors.rejectValue(constraintPropertyName, 'default.invalid.compare.message', args, 'Invalid value')
        }

        return true //always return true, since we are manually inserting the error into the errors obj
    }
}
