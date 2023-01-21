package nz.netvalue.codechallenge.core.customer.chargingsession

import nz.netvalue.codechallenge.core.admin.chargingsession.RfidTagModel

/**
 * Gets RFID Tag from database.
 */
interface GetRfidTagRepository {
    /**
     * Find RFID Tag by its unique number.
     */
    fun getRfidTagByNumber(tagNumber: String): RfidTagModel?
}
