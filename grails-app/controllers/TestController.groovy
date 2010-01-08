import net.zorched.test.Address

class TestController {
    def index = {}

    def create = {
        [address: new Address()]
    }

    def createCmd = {
        [address: new AddressCommand()]
    }

    def save = {
        println params
        def address = new Address(street:params.street, zip:params.zip, phone:params.phone)

        if (! address.hasErrors() && address.save()) {
            flash.message = "Validation passed"
            redirect(action:'index')
        } else {
            render(view:'create', model:[address:address])
        }
    }

    def saveCmd = { AddressCommand cmd ->
        println params
        
        if (! cmd.hasErrors()) {
            flash.message = "Validation passed"
            redirect(action:'index')
        } else {
            render(view:'create', model:[address:cmd])
        }
    }
}
