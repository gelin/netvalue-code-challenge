package nz.netvalue.codechallenge.web.customer.chargingsession

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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
@SecurityRequirement(name = "bearer")
class CustomerCreateChargingSessionController(
    private val service: CreateChargingSessionService
) {

    /**
     * Creates a new Charging Session.
     * @param form form
     */
    // TODO: if another rules of the session creation are required, the API may be changed
    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    @AuthRoleRequired("CUSTOMER")
    @Operation(operationId = "customer-create-charging-session",
        description = "Creates a new Charging Session",
        tags = ["customer", "charging-session"])
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
