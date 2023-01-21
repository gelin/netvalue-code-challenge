package nz.netvalue.codechallenge.web.customer.chargingsession

import nz.netvalue.codechallenge.core.customer.chargingsession.CreateChargingSessionService
import nz.netvalue.codechallenge.web.security.AuthRoleRequired
import nz.netvalue.codechallenge.web.view.ChargingSessionView
import nz.netvalue.codechallenge.web.view.ResponseView
import nz.netvalue.codechallenge.web.view.toView
import org.springframework.web.bind.annotation.*

/**
 * Creates new Charging Session for Customer.
 */
@RestController
@RequestMapping("/customer/charging-sessions")
class CustomerCreateChargingSessionController(
    private val service: CreateChargingSessionService
) {

    /**
     * Creates a new Charging Session.
     * @param form form
     */
    // TODO: if another rules of the session creation are required, the API may be changed
    @PostMapping()
    @AuthRoleRequired("CUSTOMER")
    fun createSession(
        @RequestBody form: CreateChargingSessionForm
    ): ResponseView<ChargingSessionView?> {
        val session = service.createNewSession(
            chargePointId = form.chargePointId,
            connectorNumber = form.connectorNumber.toString(),
            rfidTagNumber = form.rfidTagNumber,
            meterValue = form.meterValue,
        )
        return ResponseView(
            result = session.toView()
        )
    }

}

data class CreateChargingSessionForm(
    val chargePointId: String,
    val connectorNumber: Int,
    val rfidTagNumber: String,
    val meterValue: Int,
)
