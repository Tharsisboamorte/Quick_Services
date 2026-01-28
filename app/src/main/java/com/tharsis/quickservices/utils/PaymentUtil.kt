package com.tharsis.quickservices.utils

object PaymentUtil {
    fun getInstallmentOptions(amount: Double): List<InstallmentOption> {
        val options = mutableListOf<InstallmentOption>()

        options.add(
            InstallmentOption(
                installments = 1,
                installmentAmount = amount,
                totalAmount = amount
            )
        )

        // Add installment options (2x to 12x)
        for (installments in 2..12) {
            val installmentAmount = amount / installments
            options.add(
                InstallmentOption(
                    installments = installments,
                    installmentAmount = installmentAmount,
                    totalAmount = amount
                )
            )
        }

        return options
    }
}

data class InstallmentOption(
    val installments: Int,
    val installmentAmount: Double,
    val totalAmount: Double
) {
    fun getDisplayText(): String {
        return if (installments == 1) {
            "À vista - R$ %.2f".format(totalAmount)
        } else {
            "%dx de R$ %.2f".format(installments, installmentAmount)
        }
    }
}