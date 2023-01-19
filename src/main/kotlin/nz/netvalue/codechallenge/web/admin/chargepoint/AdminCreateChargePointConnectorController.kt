package nz.netvalue.codechallenge.web.admin.chargepoint

import nz.netvalue.codechallenge.core.admin.chargepoint.CreateChargePointConnectorService
import nz.netvalue.codechallenge.web.security.AuthRoleRequired
import nz.netvalue.codechallenge.web.view.ConnectorView
import nz.netvalue.codechallenge.web.view.ResponseView
import nz.netvalue.codechallenge.web.view.toView
import org.springframework.web.bind.annotation.*
import java.time.LocalTime
import java.time.ZonedDateTime

/**
 * Holds methods to access Charge Point Connectors for Admin.
 */
@RestController
@RequestMapping("/admin/charge-points")
class AdminCreateChargePointConnectorController(
    private val service: CreateChargePointConnectorService
) {

    /**
     * Creates a new Connector for the Charge Point.
     * The connector (next sequential) number is automatically assigned.
     * @param chargePointId id of the Charge Point
     */
    // TODO: if another rules of the connector number creation are required, the API should be changed
    @PostMapping("{chargePointId}/connectors")
    @AuthRoleRequired("ADMIN")
    fun createConnector(
        @PathVariable chargePointId: String
    ): ResponseView<ConnectorView?> {
        val connector = service.createNewConnector(chargePointId)
        return ResponseView(
            result = connector.toView()
        )
    }

}
