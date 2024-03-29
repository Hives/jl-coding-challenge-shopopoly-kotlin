import kotlin.math.abs

class GBP(val _value: Int) {
    val value: Int

    init {
        value = abs(_value)
    }

    override fun equals(other: Any?): Boolean = other is GBP && other.value == this.value
}

interface Balance {
    val value: GBP
}

data class Credit(override val value: GBP) : Balance
data class Debit(override val value: GBP) : Balance
