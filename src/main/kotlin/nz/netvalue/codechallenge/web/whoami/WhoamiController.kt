package nz.netvalue.codechallenge.web.whoami

import nz.netvalue.codechallenge.web.security.UserCredentials
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Shows the current logged user data.
 */
@RestController
class WhoamiController {

    @GetMapping("/whoami")
    fun getWhoami(user: UserCredentials?): WhoamiView {
        return WhoamiView(
            name = user?.username ?: "unauthorized",
            roles = user?.roles ?: setOf()
        )
    }

}

data class WhoamiView(
    val name: String,
    val roles: Set<String>
)
