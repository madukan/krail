package uk.q3c.krail.core.form

import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

/**  At the moment this just contains some early thoughts on a DSL - it is not useful as it is
 * Created by David Sowerby on 22 Jun 2018
 */


fun FormConfiguration.section(name: String = "standard", init: FormSectionConfiguration.() -> Unit): FormSectionConfiguration {
    val config = FormSectionConfiguration(this)
    config.init()
    sections.add(config)
    return config
}

fun PropertyConfiguration.max(max: Byte) {
    validators.add(MaxByte(max))
}

fun PropertyConfiguration.max(max: Short) {
    validators.add(MaxShort(max))
}

fun PropertyConfiguration.min(min: Short) {
    validators.add(MinShort(min))
}

fun PropertyConfiguration.min(min: Byte) {
    validators.add(MinByte(min))
}

fun PropertyConfiguration.max(max: Int) {
    validators.add(MaxInt(max))
}

fun PropertyConfiguration.min(min: Int): PropertyConfiguration {
    validators.add(MinInt(min))
    return this
}

fun PropertyConfiguration.max(max: Long) {
    validators.add(MaxLong(max))
}

fun PropertyConfiguration.min(min: Long) {
    validators.add(MinLong(min))
}


fun PropertyConfiguration.assertTrue() {
    validators.add(MustBeTrue())
}

fun PropertyConfiguration.assertFalse() {
    validators.add(MustBeFalse())
}


fun PropertyConfiguration.null_() {
    validators.add(MustBeNull())
}

fun PropertyConfiguration.notNull() {
    validators.add(MustNotBeNull())
}

fun PropertyConfiguration.pattern(pattern: String, vararg flags: javax.validation.constraints.Pattern.Flag) {
    validators.add(MustMatch(pattern, *flags))
}

fun PropertyConfiguration.past(dateTime: LocalDateTime = LocalDateTime.now()) {
    validators.add(PastLocalDateTime(dateTime))
}

fun PropertyConfiguration.past(dateTime: OffsetDateTime = OffsetDateTime.now()) {
    validators.add(PastOffsetDateTime(dateTime))
}

fun PropertyConfiguration.past(date: LocalDate = LocalDate.now()) {
    validators.add(PastLocalDate(date))
}

fun PropertyConfiguration.future(date: Date = Date.from(Instant.now())) {
    validators.add(FutureDate(date))
}

fun PropertyConfiguration.future(date: LocalDate = LocalDate.now()) {
    validators.add(FutureLocalDate(date))
}

fun PropertyConfiguration.future(dateTime: OffsetDateTime = OffsetDateTime.now()) {
    validators.add(FutureOffsetDateTime(dateTime))
}

fun PropertyConfiguration.future(dateTime: LocalDateTime = LocalDateTime.now()) {
    validators.add(FutureLocalDateTime(dateTime))
}

/**
 * BigDecimal
BigInteger
String
byte, short, int, long, and their respective wrappers
 */
fun PropertyConfiguration.decimalMax(max: BigDecimal) {
    validators.add(MaxBigDecimal(max))
}

fun PropertyConfiguration.decimalMax(max: BigInteger) {
    validators.add(MaxBigInteger(max))
}

fun PropertyConfiguration.decimalMax(max: String) {
    val bd: BigDecimal = max.toBigDecimal()
    validators.add(MaxBigDecimal(bd))
}

fun PropertyConfiguration.decimalMax(max: Int) {
    max(max)
}

fun PropertyConfiguration.decimalMax(max: Long) {
    max(max)
}

fun PropertyConfiguration.decimalMax(max: Short) {
    max(max)
}

fun PropertyConfiguration.decimalMax(max: Byte) {
    max(max)
}

////////////////////////////////////

fun PropertyConfiguration.decimalMin(min: BigDecimal) {
    validators.add(MinBigDecimal(min))
}

fun PropertyConfiguration.decimalMin(min: BigInteger) {
    validators.add(MinBigInteger(min))
}

fun PropertyConfiguration.decimalMin(min: String) {
    val bd: BigDecimal = min.toBigDecimal()
    validators.add(MinBigDecimal(bd))
}

fun PropertyConfiguration.decimalMin(min: Int) {
    min(min)
}

fun PropertyConfiguration.decimalMin(min: Long) {
    min(min)
}

fun PropertyConfiguration.decimalMin(min: Short) {
    min(min)
}

fun PropertyConfiguration.decimalMin(min: Byte) {
    min(min)
}


fun PropertyConfiguration.size(min: Int = 0, max: Int = Int.MAX_VALUE) {
    if (propertyValueClass == String::class) {
        validators.add(StringSize(min = min, max = max))
    } else {
        if (Collection::class.java.isAssignableFrom(propertyValueClass)) {
            validators.add(CollectionSize(min = min, max = max))
        }
    }
    throw InvalidTypeForValidator(propertyValueClass.kotlin, "size")
}


fun PropertyConfiguration.mustBeNull() {
    null_()
}

fun PropertyConfiguration.mustNotBeNull() {
    notNull()
}


fun PropertyConfiguration.mustBeTrue() {
    assertTrue()
}

fun PropertyConfiguration.mustBeFalse() {
    assertFalse()
}

fun PropertyConfiguration.mustBeInThePast(dateTime: LocalDateTime = LocalDateTime.now()) {
    past(dateTime = dateTime)
}

fun PropertyConfiguration.mustBeInThePast(dateTime: OffsetDateTime = OffsetDateTime.now()) {
    past(dateTime = dateTime)
}

fun PropertyConfiguration.mustBeInThePast(date: LocalDate = LocalDate.now()) {
    past(date = date)
}

fun PropertyConfiguration.mustBeInTheFuture(dateTime: LocalDateTime = LocalDateTime.now()) {
    future(dateTime = dateTime)
}

fun PropertyConfiguration.mustBeInTheFuture(dateTime: OffsetDateTime = OffsetDateTime.now()) {
    future(dateTime = dateTime)
}

fun PropertyConfiguration.mustBeInTheFuture(date: LocalDate = LocalDate.now()) {
    future(date = date)
}

fun PropertyConfiguration.mustMatch(pattern: String, vararg flags: javax.validation.constraints.Pattern.Flag) {
    pattern(pattern, *flags)
}


fun FormSectionConfiguration.property(name: String, init: PropertyConfiguration.() -> Unit): PropertyConfiguration {
    val propertyConfiguration = PropertyConfiguration(name = name, parentConfiguration = this)
    propertyConfiguration.init()
    properties[name] = propertyConfiguration
    return propertyConfiguration
}

fun FormConfigurationCommon.style(init: StyleAttributes.() -> Unit): StyleAttributes {
    val sa = StyleAttributes()
    sa.init()
    styleAttributes = sa
    return sa
}