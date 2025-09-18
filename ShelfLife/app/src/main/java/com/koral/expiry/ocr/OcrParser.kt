package com.koral.expiry.ocr

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object OcrParser {
    private val dateRegex = Regex(
        """\b((\d{4})[-.\/(](\d{1,2})[-.\/(](\d{1,2})|(\d{1,2})[-.\/(](\d{1,2})[-.\/(](\d{2,4})|(\d{1,2})[-.\/(](\d{2})))\b"""
    )
    fun extractCandidateDates(text: String): List<String> =
        dateRegex.findAll(text).map { it.value }.distinct().toList()

    fun normalizeDate(token: String): LocalDate? {
        val trimmed = token.replace("\\s".toRegex(), "")
        val fmts = listOf(
            "yyyy-MM-dd","yyyy.MM.dd","yyyy/MM/dd",
            "dd-MM-yyyy","dd.MM.yyyy","dd/MM/yyyy",
            "d-M-yyyy","d.M.yyyy","d/M/yyyy",
            "dd-MM-yy","dd.MM.yy","dd/MM/yy",
            "MM/yy","M/yy"
        ).map { DateTimeFormatter.ofPattern(it).withLocale(Locale.GERMANY) }

        for (fmt in fmts) {
            try {
                if (fmt.pattern() == "MM/yy" || fmt.pattern() == "M/yy") {
                    val parts = trimmed.replace('.', '/').replace('-', '/').split('/')
                    val m = parts[0].toInt()
                    val y = 2000 + parts.last().takeLast(2).toInt()
                    val first = LocalDate.of(y, m, 1)
                    return first.withDayOfMonth(first.lengthOfMonth())
                }
                return LocalDate.parse(trimmed, fmt)
            } catch (_: DateTimeParseException) { }
        }
        return null
    }

    fun guessName(allText: String): String {
        val candidates = allText.lines().map { it.trim() }.filter { it.length in 4..60 }
        return candidates.maxByOrNull { it.count { c -> c.isUpperCase() } + it.length / 2 } ?: "Ürün"
    }
}
