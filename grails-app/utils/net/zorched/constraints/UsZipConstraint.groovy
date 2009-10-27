/*
* Copyright 2009 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.zorched.constraints

class UsZipConstraint {

    static defaultMessage = "Property [{0}] of class [{1}] with value [{2}] is not a valid US Zipcode"
    
    def supports = { type ->
        return type!= null && String.class.isAssignableFrom(type);
    }

    def validate = { val ->
        if (! params)
            return true
        return val ==~ /^\d{5}(\-\d{4})?$/
    }
}