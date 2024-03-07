package team.b2.bingojango.domain.purchase.model

import com.fasterxml.jackson.annotation.JsonCreator
import org.apache.commons.lang3.EnumUtils

enum class PurchaseSort {
    CREATED_AT;

    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun parse(name: String?): PurchaseSort? =
            name?.let { EnumUtils.getEnumIgnoreCase(PurchaseSort::class.java, it.trim()) }
    }
}