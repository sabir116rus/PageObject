package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.getFirstCardInfo;
import static ru.netology.data.DataHelper.getSecondCardInfo;

public class MoneyTransferTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyFromSecondCard() {
        val LoginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = LoginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo().getCardNumber());
        val secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo().getCardNumber());
        val transferPage = dashboardPage.depositToFirstCard();
        val amount = DataHelper.getTransferAmount(secondCardBalance);
        transferPage.transferMoney(amount, DataHelper.getSecondCardInfo());
        val expectedFirstCardBalanceAfter = firstCardBalance + amount;
        val expectedSecondCardBalanceAfter = secondCardBalance - amount;
        Assertions.assertEquals(expectedFirstCardBalanceAfter, dashboardPage.getCardBalance(getFirstCardInfo().getCardNumber()));
        Assertions.assertEquals(expectedSecondCardBalanceAfter, dashboardPage.getCardBalance(getSecondCardInfo().getCardNumber()));
    }

    @Test
    void shouldTransferMoneyFromFirstCard() {
        val LoginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = LoginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo().getCardNumber());
        val secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo().getCardNumber());
        val transferPage = dashboardPage.depositToSecondCard();
        val amount = DataHelper.getTransferAmount(firstCardBalance);
        transferPage.transferMoney(amount, DataHelper.getFirstCardInfo());
        val expectedFirstCardBalanceAfter = firstCardBalance - amount;
        val expectedSecondCardBalanceAfter = secondCardBalance + amount;
        Assertions.assertEquals(expectedFirstCardBalanceAfter, dashboardPage.getCardBalance(getFirstCardInfo().getCardNumber()));
        Assertions.assertEquals(expectedSecondCardBalanceAfter, dashboardPage.getCardBalance(getSecondCardInfo().getCardNumber()));
    }

    @Test
    void shouldNotTransferMoneyFromSecondCardMoreThanDeposit() {
        val LoginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = LoginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val firstCardBalance = dashboardPage.getCardBalance(getFirstCardInfo().getCardNumber());
        val secondCardBalance = dashboardPage.getCardBalance(getSecondCardInfo().getCardNumber());
        val transferPage = dashboardPage.depositToFirstCard();
        val amount = DataHelper.getTransferAmount(secondCardBalance) * 2;
        transferPage.transferMoney(amount, DataHelper.getSecondCardInfo());
        transferPage.transferError();
    }
}