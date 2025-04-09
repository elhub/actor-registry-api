package no.elhub.devxp.utils

fun String.collapseJson(): String {
    val regex = Regex("""("[^"]*")|(\s+)""")
    return regex.replace(this) { matchResult ->
        if (matchResult.groupValues[1].isNotEmpty()) {
            matchResult.groupValues[1]
        } else {
            ""
        }
    }
}
