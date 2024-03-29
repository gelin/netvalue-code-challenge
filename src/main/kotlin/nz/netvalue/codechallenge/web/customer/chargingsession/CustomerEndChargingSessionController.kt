package nz.netvalue.codechallenge.web.customer.chargingsession

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nz.netvalue.codechallenge.core.customer.chargingsession.EndChargingSessionService
import nz.netvalue.codechallenge.web.security.AuthRoleRequired
import nz.netvalue.codechallenge.web.view.ChargingSessionView
import nz.netvalue.codechallenge.web.view.ResponseView
import nz.netvalue.codechallenge.web.view.toView
import org.springframework.web.bind.annotation.*

/**
 * Ends the Charging Session for Customer.
 */
@RestController
@RequestMapping("/customer/charging-sessions")
@SecurityRequirement(name = "bearer")
class CustomerEndChargingSessionController(
    private val service: EndChargingSessionService
) {

    /**
     * Ends the Charging Session.
     * @param sessionId Charging Session id
     * @param form form
     */
    // TODO: if another rules of the session termination are required, the API may be changed
    @PostMapping("{sessionId}/end", consumes = ["application/json"], produces = ["application/json"])
    @AuthRoleRequired("CUSTOMER")
    @Operation(operationId = "customer-end-charging-sessions",
        description = "Ends the Charging Session",
        tags = ["customer", "charging-session"])
    fun endSession(
        @PathVariable("sessionId") sessionId: String,
        @RequestBody form: EndChargingSessionForm
    ): ResponseView<ChargingSessionView?> {
        val session = service.endSession(
            sessionId = sessionId,
            meterValue = form.meterValue,
        )
        return ResponseView(
            result = session.toView()
        )
    }

}

data class EndChargingSessionForm(
    val meterValue: Int,
)
