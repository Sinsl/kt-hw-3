package ru.netology

fun main() {
    task1()
    task2()
}

fun task1() {
//    val timeAgo = 30
//    val timeAgo = 60 * 54
    val timeAgo = 60 * 60 * 1 + 100
//    val timeAgo = 24 * 60 * 60 - 100
//    val timeAgo = 24 * 3 * 60 * 60 - 120
//    val timeAgo = 24 * 4 * 60 * 60
    agoToText(timeAgo)
}

fun agoToText(timeAgo: Int) {
    val str = "был(а) " + when (timeAgo) {
        in 0..60 -> "только что"
        in 61..(60 * 60) -> "${strMinutes(timeAgo / 60)} назад"
        in (60 * 60 + 1)..(24 * 60 * 60) -> "${strHours(timeAgo / (60 * 60))} назад"
        in (24 * 60 * 60 + 1)..(24 * 2 * 60 * 60) -> "вчера"
        in (24 * 2 * 60 * 60 + 1)..(24 * 3 * 60 * 60) -> "позавчера"
        else -> "давно"
    }
    println(str)
}

fun strMinutes(minutes: Int): String = when {
    (minutes in 11..14) -> "$minutes минут"
    (minutes % 10 == 1) -> "$minutes минуту"
    (minutes % 10 == 2 || minutes % 10 == 3 || minutes % 10 == 4) -> "$minutes минуты"
    else -> "$minutes минут"
}

fun strHours(hours: Int): String = when (hours) {
    1, 21 -> "$hours час"
    2, 3, 4, 22, 23, 24 -> "$hours часа"
    else -> "$hours часов"
}

fun task2() {
    var sumTransferMonthMir = 0
    var sumTransferMonthVisa = 0
    var sumTransferMonthMC = 0

    sumTransferMonthMir += checkTransfer(sumTransferMonth = sumTransferMonthMir, currentTransfer = 60_000)
    sumTransferMonthMir += checkTransfer(sumTransferMonth = sumTransferMonthMir, currentTransfer = 150_000)
    sumTransferMonthMir += checkTransfer(sumTransferMonth = sumTransferMonthMir, currentTransfer = 150_000)
    sumTransferMonthMir += checkTransfer(sumTransferMonth = sumTransferMonthMir, currentTransfer = 150_000)
    printBalans(sumTransferMonthMir, sumTransferMonthVisa, sumTransferMonthMC)
    sumTransferMonthMir += checkTransfer(sumTransferMonth = sumTransferMonthMir, currentTransfer = 100_000)
    printBalans(sumTransferMonthMir, sumTransferMonthVisa, sumTransferMonthMC)

    sumTransferMonthVisa += checkTransfer("Visa", sumTransferMonth = sumTransferMonthVisa, currentTransfer = 20_000)
    sumTransferMonthVisa += checkTransfer("Visa", sumTransferMonth = sumTransferMonthVisa, currentTransfer = 45_000)
    sumTransferMonthVisa += checkTransfer("Visa", sumTransferMonth = sumTransferMonthVisa, currentTransfer = 11_000)
    sumTransferMonthVisa += checkTransfer("Visa", sumTransferMonth = sumTransferMonthVisa, currentTransfer = 20_000)
    printBalans(sumTransferMonthMir, sumTransferMonthVisa, sumTransferMonthMC)
    sumTransferMonthVisa += checkTransfer("Visa", sumTransferMonth = sumTransferMonthVisa, currentTransfer = 170_000)
    printBalans(sumTransferMonthMir, sumTransferMonthVisa, sumTransferMonthMC)

    sumTransferMonthMC += checkTransfer("MasterCard", sumTransferMonth = sumTransferMonthMC, currentTransfer = 20_000)
    sumTransferMonthMC += checkTransfer("MasterCard", sumTransferMonth = sumTransferMonthMC, currentTransfer = 45_000)
    sumTransferMonthMC += checkTransfer("MasterCard", sumTransferMonth = sumTransferMonthMC, currentTransfer = 11_000)
    sumTransferMonthMC += checkTransfer("MasterCard", sumTransferMonth = sumTransferMonthMC, currentTransfer = 20_000)
    printBalans(sumTransferMonthMir, sumTransferMonthVisa, sumTransferMonthMC)
    sumTransferMonthMC += checkTransfer("MasterCard", sumTransferMonth = sumTransferMonthMC, currentTransfer = 170_000)

}

fun printBalans(mir: Int, visa: Int, mc: Int) {
    println("Суммы переводов за месяц по картам: мир - $mir, visa - $visa, mastercard - $mc")
}

fun checkTransfer(typeCard: String = "Mir", sumTransferMonth: Int = 0, currentTransfer: Int): Int {
    if (isBlockedTransfer(sumTransferMonth, currentTransfer)) {
        println("Операция на сумму $currentTransfer невозможна из-за превышения лимитов.")
        return 0
    }
    val commission = calcTransferFee(typeCard, sumTransferMonth = sumTransferMonth, currentTransfer = currentTransfer)
    println("Перевод на сумму $currentTransfer выполнен успешно. Комиссия: $commission руб")
    return currentTransfer
}

fun isBlockedTransfer(sumTransferMonth: Int, currentTransfer: Int): Boolean {
    val limitDay = 150_000
    val limitMonth = 600_000
    return (currentTransfer > limitDay || sumTransferMonth + currentTransfer > limitMonth)
}

fun calcTransferFee(typeCard: String, sumTransferMonth: Int = 0, currentTransfer: Int): Int {
    val freeLimit = 75_000
    val taxMC = 0.006
    val taxVisa = 0.0075
    if (typeCard == "Mir") {
        return 0
    }
    if (sumTransferMonth + currentTransfer < freeLimit) {
        return 0
    }
    val sumForCommission = currentTransfer - 0.coerceAtLeast(freeLimit - sumTransferMonth)
    return when (typeCard) {
        "MasterCard" -> (sumForCommission * taxMC).toInt() + 20
        "Visa" -> 35.coerceAtLeast((sumForCommission * taxVisa).toInt())
        else -> 0
    }
}