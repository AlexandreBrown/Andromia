package cstj.qc.ca.andromia.models

import java.util.regex.Matcher
import java.util.regex.Pattern

class Validator {
    companion object {
        fun emailValidator(email: String): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(email)
            return matcher.matches()
        }
    }
}