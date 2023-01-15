package nz.netvalue.codechallenge.web.security

/**
 * Annotation for check by one of roles existence
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class AuthRoleRequired(
    /**
     * Returns a role which is required to access the method
     * @return the role name
     */
    val value: String
)
