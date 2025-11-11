@file:JvmName("StringExt")

package com.github.chiamingmai.androidutil.extensions.kotlin

import com.github.chiamingmai.androidutil.utils.StringMaskUtils


/** 是否包含中文的"其他"字詞 */
val CharSequence.containsOtherWordInChinese
    get() = contains("其他", true) ||
            contains("其它", true) ||
            contains("其她", true) ||
            contains("其牠", true)

/**
 * Masks a portion of the given string.
 *
 * @param start Starting index to begin masking. Negative values treated as 0.
 * @param length Number of characters to mask. Negative values treated as 1.
 * @param symbol Character used for masking, default is `*`.
 */
fun String.mask(start: Int, length: Int, symbol: Char = '*'): String =
    StringMaskUtils.mask(input = this, start = start, length = length, symbol = symbol)

/**
 * Masks the middle portion of a string while keeping start and end characters.
 *
 * @param keepStart Number of characters to keep at the start.
 * @param keepEnd Number of characters to keep at the end.
 * @param symbol Character used for masking, default is `*`.
 */
fun String.maskMiddle(keepStart: Int = 2, keepEnd: Int = 2, symbol: Char = '*'): String =
    StringMaskUtils.maskMiddle(input = this, keepStart, keepEnd, symbol = symbol)

/**
 * Masks an email address, keeping the first and last character of the username.
 *
 * Example: `test@example.com` -> `t**t@example.com`
 *
 * @param symbol Character used for masking, default is `*`.
 */
fun String.maskEmail(symbol: Char = '*'): String =
    StringMaskUtils.maskEmail(mailAddress = this, symbol = symbol)

/**
 * Masks a user identity string.
 *
 * Example: `A123456789` -> `A12*****89`
 *
 * @param symbol Character used for masking, default is `*`.
 */
fun String.maskIdentity(symbol: Char = '*'): String =
    StringMaskUtils.maskUserIdentity(this, symbol = symbol)

/**
 * Masks a landline number.
 *
 * Example:
 * - `07-5555555` -> `07-****555`
 * - `5555555` -> `****555`
 *
 * @param symbol Character used for masking, default is `*`.
 */
fun String.maskLandline(symbol: Char = '*'): String =
    StringMaskUtils.maskLandlineNumber(this, symbol = symbol)

/**
 * Masks a mobile phone number.
 *
 * Example: `0912345678` -> `091****678`
 *
 * @param symbol Character used for masking, default is `*`.
 */
fun String.maskMobile(symbol: Char = '*'): String =
    StringMaskUtils.maskMobilePhoneNumber(this, symbol = symbol)
