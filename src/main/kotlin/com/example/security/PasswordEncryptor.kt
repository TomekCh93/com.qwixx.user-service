import at.favre.lib.crypto.bcrypt.BCrypt

private const val WORK_FACTOR= 12
fun hash(password: String): String {
    return BCrypt.withDefaults().hashToString(WORK_FACTOR, password.toCharArray())
}
