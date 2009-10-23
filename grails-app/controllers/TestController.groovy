import net.zorched.test.Address

class TestController {
    def index = {}

    def create = {
        [address: new Address()]
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
}
